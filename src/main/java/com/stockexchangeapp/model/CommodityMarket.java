package com.stockexchangeapp.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author blazej
 */
public class CommodityMarket extends Market {
    
    private List<Commodity> commodities;
    
    public CommodityMarket(String name, Double fee){
        super(name, fee);
        this.commodities = new ArrayList<>();

    }
    
    public CommodityMarket(){
        this(null, 0.0);
    }
    
    public List<Commodity> getCommodities() {
        return commodities;
    }
}
