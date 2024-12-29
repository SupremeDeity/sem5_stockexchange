package com.stockexchangeapp.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 *
 * @author blazej
 */
public class Transaction {
    
    double price;
    int amount;
    String time;
    
    public Transaction(double price, int amount){
        this.price = price;
        this.amount = amount;
        
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        this.time = sdf.format(cal.getTime());
    }
    
    public Transaction(){
        
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
    
    public String getTime() {
        return time;
    }
    
    public void setTime(String time) {
        this.time = time;
    }
}
