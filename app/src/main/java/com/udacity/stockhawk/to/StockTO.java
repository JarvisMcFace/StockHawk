package com.udacity.stockhawk.to;

import java.io.Serializable;
import java.util.List;

/**
 * Created by David on 12/19/16.
 */
public class StockTO implements Serializable {

    private static final long serialVersionUID = 287067544004875999L;

    private String symbol;
    private String name;
    private float price;
    private float change;
    private float percentChange;
    private List<StockHistoryTO> history;

    public StockTO(String name, String symbol, float price, float absoluteChange, float percentChange, List<StockHistoryTO> history) {
        this.name = name;
        this.symbol = symbol;
        this.price = price;
        this.change = absoluteChange;
        this.percentChange = percentChange;
        this.history = history;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getChange() {
        return change;
    }

    public void setChange(float change) {
        this.change = change;
    }

    public float getPercentChange() {
        return percentChange;
    }

    public void setPercentChange(float percentChange) {
        this.percentChange = percentChange;
    }

    public List<StockHistoryTO> getHistory() {
        return history;
    }

    public void setHistory(List<StockHistoryTO> history) {
        this.history = history;
    }
}
