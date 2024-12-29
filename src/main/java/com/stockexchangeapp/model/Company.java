package com.stockexchangeapp.model;

import static java.lang.Thread.sleep;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;

/**
 *
 * @author blazej
 */
public class Company implements Runnable {

    private StringProperty name;
    private StringProperty abbreviation;
    private StringProperty chairman;
    private StringProperty firstListingDate;
    private DoubleProperty min;
    private DoubleProperty max;
    private DoubleProperty open;
    private DoubleProperty close;
    private DoubleProperty current;
    private DoubleProperty bid;
    private DoubleProperty offer;
    private DoubleProperty turnoverValue; // during current day, approx. volume * current
    private DoubleProperty marketValue; // shares_count * current
    private IntegerProperty sharesCount;
    private IntegerProperty sharesForSale;
    private IntegerProperty volume; // during current

    private DoubleProperty change;

    private StockExchange memberOfStockExchange;

    private List<Transaction> transactionList;
    private Set<Investor> investorSet;

    public Company(String name, String abbreviation, String chairman, String firstListingDate, Double min, Double max,
            Double open, Double close,
            Double current, Double bid, Double offer, Double turnoverValue, Double marketValue, Integer volume,
            Integer sharesCount, Integer sharesForSale,
            Double change, StockExchange memberOfStockExchange) {

        this.name = new SimpleStringProperty(name);
        this.abbreviation = new SimpleStringProperty(abbreviation);
        this.chairman = new SimpleStringProperty(chairman);
        this.firstListingDate = new SimpleStringProperty(firstListingDate);
        this.min = new SimpleDoubleProperty(min);
        this.max = new SimpleDoubleProperty(max);
        this.open = new SimpleDoubleProperty(open);
        this.close = new SimpleDoubleProperty(close);
        this.current = new SimpleDoubleProperty(current);
        this.bid = new SimpleDoubleProperty(bid);
        this.offer = new SimpleDoubleProperty(offer);
        this.turnoverValue = new SimpleDoubleProperty(turnoverValue);
        this.marketValue = new SimpleDoubleProperty(marketValue);
        this.volume = new SimpleIntegerProperty(volume);
        this.sharesCount = new SimpleIntegerProperty(sharesCount);
        this.sharesForSale = new SimpleIntegerProperty(sharesForSale);
        this.change = new SimpleDoubleProperty(change);
        this.memberOfStockExchange = memberOfStockExchange;

        this.transactionList = new LinkedList<>();
        this.investorSet = new HashSet<>();

    }

    public Company() {
        this(null, null, null, null, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0, 0, 0, 0.0, null);
    }

    private volatile boolean running = true;

    public void terminate() {
        running = false;
    }

    public void run() {
        while (running) {
            try {
                sellShares();

            } catch (InterruptedException ex) {
                Logger.getLogger(Investor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.out.println(this.getName() + " thread terminated");
    }

    public void sellShares() throws InterruptedException {
        int min = 15;
        int max = 20;
        int randomInt = ThreadLocalRandom.current().nextInt(min * 1000, max * 1000);

        sleep(randomInt);
        synchronized (this.getStockExchangeBelonging()) {
            int random = ThreadLocalRandom.current().nextInt(5000, 10000);

            this.setSharesCount(this.getSharesCount() + random);
            this.setSharesForSale(this.getSharesForSale() + random);
            this.setMarketValue(this.getSharesCount() * this.getCurrent());
        }
    }

    public void buyAllShares() {
        this.getInvestorSet().forEach((investor) -> {
            HashMap<Company, Integer> investorSharesOf = investor.getShares();

            synchronized (this.getStockExchangeBelonging()) {
                investor.setBudget(investor.getBudget() + investorSharesOf.get(this) * this.getCurrent());
                investorSharesOf.remove(this);
            }
        });
    }

    public void calculateCurrentPrice() {
        double random = ThreadLocalRandom.current().nextDouble(-15, 15);
        random = (double) Math.round(random * 100) / 100;

        double newPrice = this.getCurrent() * (1 + random / 100);
        newPrice = (double) Math.round(newPrice * 100) / 100;

        if (newPrice > this.getMax()) {
            this.setMax(newPrice);
        } else if (newPrice < this.getMin()) {
            this.setMin(newPrice);
        }

        this.setCurrent(newPrice);
        this.setChange(random);
        this.setMarketValue(this.getSharesCount() * this.getCurrent());
    }

    public String toString() {
        return this.getName();
    }

    @XmlElementWrapper(name = "transactions")
    @XmlElement(name = "transaction")
    public List<Transaction> getTransactionList() {
        return transactionList;
    }

    public void setTransactionList(List<Transaction> transactionList) {
        this.transactionList = transactionList;
    }

    public Set<Investor> getInvestorSet() {
        return investorSet;
    }

    public final String getName() {
        return name.get();
    }

    public final void setName(String value) {
        name.set(value);
    }

    public StringProperty nameProperty() {
        return name;
    }

    public final String getAbbreviation() {
        return abbreviation.get();
    }

    public final void setAbbreviation(String value) {
        abbreviation.set(value);
    }

    public StringProperty abbreviationProperty() {
        return abbreviation;
    }

    public final String getChairman() {
        return chairman.get();
    }

    public final void setChairman(String value) {
        chairman.set(value);
    }

    public StringProperty chairmanProperty() {
        return chairman;
    }

    public final String getFirstListingDate() {
        return firstListingDate.get();
    }

    public final void setFirstListingDate(String value) {
        firstListingDate.set(value);
    }

    public StringProperty firstListingDateProperty() {
        return firstListingDate;
    }

    public final double getMin() {
        return min.get();
    }

    public final void setMin(double value) {
        min.set(value);
    }

    public DoubleProperty minProperty() {
        return min;
    }

    public final double getMax() {
        return max.get();
    }

    public final void setMax(double value) {
        max.set(value);
    }

    public DoubleProperty maxProperty() {
        return max;
    }

    public final double getOpen() {
        return open.get();
    }

    public final void setOpen(double value) {
        open.set(value);
    }

    public DoubleProperty openProperty() {
        return open;
    }

    public final double getClose() {
        return close.get();
    }

    public final void setClose(double value) {
        close.set(value);
    }

    public DoubleProperty closeProperty() {
        return close;
    }

    public final double getCurrent() {
        return current.get();
    }

    public final void setCurrent(double value) {
        current.set(value);
    }

    public DoubleProperty currentProperty() {
        return current;
    }

    public final double getBid() {
        return bid.get();
    }

    public final void setBid(double value) {
        bid.set(value);
    }

    public DoubleProperty bidProperty() {
        return bid;
    }

    public final double getOffer() {
        return offer.get();
    }

    public final void setOffer(double value) {
        offer.set(value);
    }

    public DoubleProperty offerProperty() {
        return offer;
    }

    public final double getTurnoverValue() {
        return turnoverValue.get();
    }

    public final void setTurnoverValue(double value) {
        turnoverValue.set(value);
    }

    public DoubleProperty turnoverValueProperty() {
        return turnoverValue;
    }

    public final double getMarketValue() {
        return marketValue.get();
    }

    public final void setMarketValue(double value) {
        marketValue.set(value);
    }

    public DoubleProperty marketValueProperty() {
        return marketValue;
    }

    public final int getSharesCount() {
        return sharesCount.get();
    }

    public final void setSharesCount(int value) {
        sharesCount.set(value);
    }

    public IntegerProperty sharesCountProperty() {
        return sharesCount;
    }

    public final int getSharesForSale() {
        return sharesForSale.get();
    }

    public final void setSharesForSale(int value) {
        sharesForSale.set(value);
    }

    public IntegerProperty sharesForSaleProperty() {
        return sharesForSale;
    }

    public final int getVolume() {
        return volume.get();
    }

    public final void setVolume(int value) {
        volume.set(value);
    }

    public IntegerProperty volumeProperty() {
        return volume;
    }

    public final double getChange() {
        return change.get();
    }

    public final void setChange(double value) {
        change.set(value);
    }

    public DoubleProperty changeProperty() {
        return change;
    }

    public final StockExchange getStockExchangeBelonging() {
        return memberOfStockExchange;
    }

    public final void setStockExchangeBelonging(StockExchange stockExchange) {
        memberOfStockExchange = stockExchange;
    }

}
