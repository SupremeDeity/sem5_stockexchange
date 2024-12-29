package com.stockexchangeapp.model;

import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author blazej
 */
public class StockExchange extends Market {

    private StringProperty symbol;
    private Currency currency;
    private StringProperty country;
    private StringProperty city;
    private StringProperty address;

    private List<Company> companies;
    private List<Investor> investors;

    public StockExchange(String name, Double fee, String symbol, Currency currency, String country, String city,
            String address) {
        super(name, fee);
        this.symbol = new SimpleStringProperty(symbol);
        this.currency = currency;
        this.country = new SimpleStringProperty(country);
        this.city = new SimpleStringProperty(city);
        this.address = new SimpleStringProperty(address);
        this.companies = new ArrayList<>();
        this.investors = new ArrayList<>();

    }

    public StockExchange() {
        this(null, 0.0, null, null, null, null, null);
    }

    public String toString() {
        return this.getName();
    }

    public final String getSymbol() {
        return symbol.get();
    }

    public final void setSymbol(String value) {
        symbol.set(value);
    }

    public StringProperty symbolProperty() {
        return symbol;
    }

    public final String getCountry() {
        return country.get();
    }

    public final void setCountry(String value) {
        country.set(value);
    }

    public StringProperty countryProperty() {
        return this.country;
    }

    public final String getCity() {
        return city.get();
    }

    public final void setCity(String value) {
        city.set(value);
    }

    public StringProperty cityProperty() {
        return city;
    }

    public final String getAddress() {
        return address.get();
    }

    public final void setAddress(String value) {
        address.set(value);
    }

    public StringProperty addressProperty() {
        return address;
    }

    public Currency getCurrency() {
        return this.currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public List<Company> getCompanies() {
        return companies;
    }

    public void addCompanyToCompanies(Company company) {
        this.companies.add(company);
    }

    public List<Investor> getInvestors() {
        return investors;
    }

}
