package com.udacity.stockhawk.to;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by David on 12/19/16.
 */
public class StockHistoryTO implements Serializable  {

    private static final long serialVersionUID = -628281971449147515L;

    @SerializedName("adjClose")
    private Double adjClose;
    @SerializedName("close")
    private Double close;
    @SerializedName("date")
    private StockHistoryDateTO date;
    @SerializedName("high")
    private Double high;
    @SerializedName("low")
    private Double low;
    @SerializedName("open")
    private Double open;
    @SerializedName("symbol")
    private String symbol;
    @SerializedName("volume")
    private Integer volume;

    public Double getAdjClose() {
        return adjClose;
    }

    public void setAdjClose(Double adjClose) {
        this.adjClose = adjClose;
    }

    public Double getClose() {
        return close;
    }

    public void setClose(Double close) {
        this.close = close;
    }

    public StockHistoryDateTO getDate() {
        return date;
    }

    public void setDate(StockHistoryDateTO date) {
        this.date = date;
    }

    public Double getHigh() {
        return high;
    }

    public void setHigh(Double high) {
        this.high = high;
    }

    public Double getLow() {
        return low;
    }

    public void setLow(Double low) {
        this.low = low;
    }

    public Double getOpen() {
        return open;
    }

    public void setOpen(Double open) {
        this.open = open;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Integer getVolume() {
        return volume;
    }

    public void setVolume(Integer volume) {
        this.volume = volume;
    }
}
