/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stockexchangeapp.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author blazej
 */
public class Currency {

    private StringProperty name;
    private StringProperty code;

    public Currency(String name, String code) {
        this.name = new SimpleStringProperty(name);
        this.code = new SimpleStringProperty(code);
    }

    public Currency() {
        this(null, null);
    }

    public String toString() {
        return this.getCode();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getName() {
        return this.name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setCode(String code) {
        this.code.set(code);
    }

    public String getCode() {
        return this.code.get();
    }

    public StringProperty codeProperty() {
        return code;
    }

}
