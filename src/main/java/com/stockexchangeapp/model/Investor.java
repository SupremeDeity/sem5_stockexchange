package com.stockexchangeapp.model;

import static java.lang.Thread.sleep;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;

/**
 *
 * @author blazej
 */
public class Investor implements Runnable {
    private StringProperty firstName;
    private StringProperty lastName;
    private StringProperty id;
    private DoubleProperty budget;

    private StockExchange memberOfStockExchange;
    private CommodityMarket memberOfCommodityMarket;

    private HashMap<Company, Integer> shares;
    private HashMap<Commodity, Integer> commodities;

    public Investor(String firstName, String lastName, String id, Double budget, StockExchange memberOfStockExchange) {
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.id = new SimpleStringProperty(id);
        this.budget = new SimpleDoubleProperty(budget);
        this.shares = new HashMap<>();
        this.commodities = new HashMap<>();
        this.memberOfStockExchange = memberOfStockExchange;
    }

    public Investor() {
        this(null, null, null, 0.0, null);
    }

    private volatile boolean running = true;

    public void terminate() {
        running = false;
    }

    public void run() {
        while (running) {
            try {
                buyShare();
                raiseBudget();
                sellShares();

                buyCommodity();
                raiseBudget();
                sellCommodities();
            } catch (InterruptedException ex) {
                Logger.getLogger(Investor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.out.println(this.getId() + " investor thread terminated");
    }

    public void raiseBudget() throws InterruptedException {
        int min = 5;
        int max = 7;
        int randomInt = ThreadLocalRandom.current().nextInt(min * 1000, max * 1000);

        sleep(randomInt);
        synchronized (this) {
            this.setBudget(this.getBudget() + randomInt);
        }
        System.out.println(this.getBudget() + " -investorID: " + this.getId());
    }

    public void buyShare() throws InterruptedException {
        int min = 2;
        int max = 7;
        int randomInt = ThreadLocalRandom.current().nextInt(min * 1000, max * 1000);

        sleep(randomInt);
        synchronized (this.getStockExchangeBelonging()) {
            List<Company> companies = this.memberOfStockExchange.getCompanies();
            int randomIndex = ThreadLocalRandom.current().nextInt(0, companies.size());
            Company company = companies.get(randomIndex);

            if (company.getSharesForSale() > 0 && this.getBudget() > 0) {

                int sharesToBuy = (int) ((int) this.getBudget() / company.getCurrent());

                if (company.getSharesForSale() - sharesToBuy >= 0) {
                    double value = sharesToBuy * company.getCurrent();
                    this.getShares().put(company, sharesToBuy);
                    this.setBudget(this.getBudget() - value);

                    company.getTransactionList().add(new Transaction(company.getCurrent(), sharesToBuy));
                    company.calculateCurrentPrice();
                    company.setSharesForSale(company.getSharesForSale() - sharesToBuy);
                    company.setVolume(company.getVolume() + sharesToBuy);
                    company.setTurnoverValue(company.getTurnoverValue() + value);

                    System.out.println(String.valueOf("investor:" + this.getId() + " buys " + sharesToBuy + " for: "
                            + value + " from: " + company.getName()));
                } else {
                    // buy all available shares
                    int availableShares = company.getSharesForSale();
                    double value = availableShares * company.getCurrent();
                    this.getShares().put(company, availableShares);
                    this.setBudget(this.getBudget() - value);

                    company.getTransactionList().add(new Transaction(company.getCurrent(), availableShares));
                    company.calculateCurrentPrice();
                    company.setSharesForSale(0);
                    company.setVolume(company.getVolume() + availableShares);
                    company.setTurnoverValue(company.getTurnoverValue() + value);

                    System.out.println(String.valueOf("investor:" + this.getId() + " buys " + availableShares + " for: "
                            + value + " from: " + company.getName()));
                }

                company.getInvestorSet().add(this);

            } else
                System.out.println("investor: " + this.getId() + "can't buy shares");
        }
    }

    public void sellShares() throws InterruptedException {
        int min = 5;
        int max = 7;
        int randomInt = ThreadLocalRandom.current().nextInt(min * 1000, max * 1000);

        sleep(randomInt);
        synchronized (this.getStockExchangeBelonging()) {
            if (!this.getShares().isEmpty()) {
                Map.Entry<Company, Integer> entry = this.getShares().entrySet().iterator().next();
                Company company = entry.getKey();

                int investorShares = this.shares.get(company);
                double value = investorShares * company.getCurrent();
                double income = value * (1 - this.getStockExchangeBelonging().getFee() / 100);

                company.getTransactionList().add(new Transaction(company.getCurrent(), investorShares));
                company.getInvestorSet().remove(this);
                company.calculateCurrentPrice();
                company.setSharesForSale(company.getSharesForSale() + investorShares);
                company.setVolume(company.getVolume() + investorShares);
                company.setTurnoverValue(company.getTurnoverValue() + value);

                this.setBudget(this.getBudget() + income);
                this.getShares().remove(company);

            }
        }
    }

    public void sellAllShares() {
        synchronized (this.getStockExchangeBelonging()) {
            if (!this.getShares().isEmpty()) {

                Iterator it = this.getShares().entrySet().iterator();
                while (it.hasNext()) {
                    HashMap.Entry pair = (HashMap.Entry) it.next();

                    Company company = (Company) pair.getKey();
                    int investorShares = (int) pair.getValue();

                    double value = investorShares * company.getCurrent();
                    double income = value * (1 - this.getStockExchangeBelonging().getFee() / 100);

                    company.getTransactionList().add(new Transaction(company.getCurrent(), investorShares));
                    company.getInvestorSet().remove(this);
                    company.calculateCurrentPrice();
                    company.setSharesForSale(company.getSharesForSale() + investorShares);
                    company.setVolume(company.getVolume() + investorShares);
                    company.setTurnoverValue(company.getTurnoverValue() + value);

                    this.setBudget(this.getBudget() + income);
                    this.getShares().remove(company);
                }
            }
        }
    }

    public void buyCommodity() throws InterruptedException {
        int min = 2;
        int max = 7;
        int randomInt = ThreadLocalRandom.current().nextInt(min * 1000, max * 1000);

        sleep(randomInt);
        synchronized (this.getCommodityMarketBelonging()) {
            List<Commodity> commodities = this.memberOfCommodityMarket.getCommodities();
            int randomIndex = ThreadLocalRandom.current().nextInt(0, commodities.size());
            Commodity commodity = commodities.get(randomIndex);

            System.out.println(commodity);

            if (commodity.getCommoditiesForSale() > 0 && this.getBudget() > 0) {

                int commoditiesToBuy = (int) ((int) this.getBudget() / commodity.getCurrent());

                if (commodity.getCommoditiesForSale() - commoditiesToBuy >= 0) {
                    double value = commoditiesToBuy * commodity.getCurrent();
                    this.getCommodities().put(commodity, commoditiesToBuy);
                    this.setBudget(this.getBudget() - value);

                    commodity.getTransactionList().add(new Transaction(commodity.getCurrent(), commoditiesToBuy));
                    commodity.calculateCurrentPrice();
                    commodity.setCommoditiesForSale(commodity.getCommoditiesForSale() - commoditiesToBuy);
                    commodity.setVolume(commodity.getVolume() + commoditiesToBuy);
                    commodity.setTurnoverValue(commodity.getTurnoverValue() + value);

                    System.out.println(String.valueOf("investor:" + this.getId() + " buys " + commodity.getName()
                            + " in number of " + commoditiesToBuy + " for: " + value));
                } else {
                    // buy all available commodities
                    int availableCommodities = commodity.getCommoditiesForSale();
                    double value = availableCommodities * commodity.getCurrent();
                    this.getCommodities().put(commodity, availableCommodities);
                    this.setBudget(this.getBudget() - value);

                    commodity.getTransactionList().add(new Transaction(commodity.getCurrent(), availableCommodities));
                    commodity.calculateCurrentPrice();
                    commodity.setCommoditiesForSale(0);
                    commodity.setVolume(commodity.getVolume() + availableCommodities);
                    commodity.setTurnoverValue(commodity.getTurnoverValue() + value);

                    System.out.println(String.valueOf("investor:" + this.getId() + " buys " + commodity.getName()
                            + " in number of " + availableCommodities + " for: " + value));
                }

                commodity.getInvestorSet().add(this);

            } else
                System.out.println("investor: " + this.getId() + "can't buy commodities");
        }
    }

    public void sellCommodities() throws InterruptedException {
        int min = 5;
        int max = 7;
        int randomInt = ThreadLocalRandom.current().nextInt(min * 1000, max * 1000);

        sleep(randomInt);
        synchronized (this.getCommodityMarketBelonging()) {
            if (!this.getCommodities().isEmpty()) {
                Map.Entry<Commodity, Integer> entry = this.getCommodities().entrySet().iterator().next();
                Commodity commodity = entry.getKey();

                int investorCommodities = this.commodities.get(commodity);
                double value = investorCommodities * commodity.getCurrent();
                double income = value * (1 - this.getCommodityMarketBelonging().getFee() / 100);

                commodity.getTransactionList().add(new Transaction(commodity.getCurrent(), investorCommodities));
                commodity.getInvestorSet().remove(this);
                commodity.calculateCurrentPrice();
                commodity.setCommoditiesForSale(commodity.getCommoditiesForSale() + investorCommodities);
                commodity.setVolume(commodity.getVolume() + investorCommodities);
                commodity.setTurnoverValue(commodity.getTurnoverValue() + value);

                this.setBudget(this.getBudget() + income);
                this.getCommodities().remove(commodity);

            }
        }
    }

    public void sellAllCommodities() {
        synchronized (this.getCommodityMarketBelonging()) {
            if (!this.getCommodities().isEmpty()) {

                Iterator it = this.getCommodities().entrySet().iterator();
                while (it.hasNext()) {
                    HashMap.Entry pair = (HashMap.Entry) it.next();

                    Commodity commodity = (Commodity) pair.getKey();
                    int investorCommodities = (int) pair.getValue();

                    double value = investorCommodities * commodity.getCurrent();
                    double income = value * (1 - this.getCommodityMarketBelonging().getFee() / 100);

                    commodity.getTransactionList().add(new Transaction(commodity.getCurrent(), investorCommodities));
                    commodity.getInvestorSet().remove(this);
                    commodity.calculateCurrentPrice();
                    commodity.setCommoditiesForSale(commodity.getCommoditiesForSale() + investorCommodities);
                    commodity.setVolume(commodity.getVolume() + investorCommodities);
                    commodity.setTurnoverValue(commodity.getTurnoverValue() + value);

                    this.setBudget(this.getBudget() + income);
                    this.getCommodities().remove(commodity);
                }
            }
        }
    }

    @XmlElementWrapper(name = "shares")
    @XmlElement(name = "share")
    public HashMap<Company, Integer> getShares() {
        return shares;
    }

    public void setShares(HashMap<Company, Integer> shares) {
        this.shares = shares;
    }

    @XmlElementWrapper(name = "commodities")
    @XmlElement(name = "commodity")
    public HashMap<Commodity, Integer> getCommodities() {
        return commodities;
    }

    public void setCommodities(HashMap<Commodity, Integer> commodities) {
        this.commodities = commodities;
    }

    public final String getFirstName() {
        return firstName.get();
    }

    public final void setFirstName(String value) {
        firstName.set(value);
    }

    public StringProperty firstNameProperty() {
        return firstName;
    }

    public final String getLastName() {
        return lastName.get();
    }

    public final void setLastName(String value) {
        lastName.set(value);
    }

    public StringProperty lastNameProperty() {
        return lastName;
    }

    public final String getId() {
        return id.get();
    }

    public final void setId(String value) {
        id.set(value);
    }

    public StringProperty idProperty() {
        return id;
    }

    public final double getBudget() {
        return budget.get();
    }

    public final void setBudget(double value) {
        budget.set(value);
    }

    public DoubleProperty budgetProperty() {
        return budget;
    }

    public final StockExchange getStockExchangeBelonging() {
        return memberOfStockExchange;
    }

    public final void setStockExchangeBelonging(StockExchange stockExchange) {
        memberOfStockExchange = stockExchange;
    }

    public final CommodityMarket getCommodityMarketBelonging() {
        return memberOfCommodityMarket;
    }

    public final void setCommodityMarketBelonging(CommodityMarket commodityMarket) {
        memberOfCommodityMarket = commodityMarket;
    }

}
