package com.stockexchangeapp.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author blazej
 */
public class Market {

    private final StringProperty name;
    private final DoubleProperty fee;

    public Market(String name, Double fee) {
        this.name = new SimpleStringProperty(name);
        this.fee = new SimpleDoubleProperty(fee);

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

    public final double getFee() {
        return fee.get();
    }

    public final void setFee(double value) {
        fee.set(value);
    }

    public DoubleProperty feeProperty() {
        return fee;
    }

}
