package com.udacity.stockhawk.to;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by David on 12/20/16.
 */

public class StockStatsTO implements Serializable {

    private static final long serialVersionUID = -2878849700959337131L;

    private Double eBITDA;
    private Double bookValuePerShare;
    private Double eps;
    private Double epsEstimateCurrentYear;
    private Double epsEstimateNextQuarter;
    private Double epsEstimateNextYear;
    private Double marketCap;
    private Double oneYearTargetPrice;
    private Double pe;
    private Double peg;
    private Double priceBook;
    private Double priceSales;
    private Double revenue;
    private Integer sharesFloat;
    private Integer sharesOutstanding;
    private Double shortRatio;
    private String symbol;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Double getEBITDA() {
        return eBITDA;
    }

    public void setEBITDA(Double eBITDA) {
        this.eBITDA = eBITDA;
    }

    public Double getBookValuePerShare() {
        return bookValuePerShare;
    }

    public void setBookValuePerShare(Double bookValuePerShare) {
        this.bookValuePerShare = bookValuePerShare;
    }

    public Double getEps() {
        return eps;
    }

    public void setEps(Double eps) {
        this.eps = eps;
    }

    public Double getEpsEstimateCurrentYear() {
        return epsEstimateCurrentYear;
    }

    public void setEpsEstimateCurrentYear(Double epsEstimateCurrentYear) {
        this.epsEstimateCurrentYear = epsEstimateCurrentYear;
    }

    public Double getEpsEstimateNextQuarter() {
        return epsEstimateNextQuarter;
    }

    public void setEpsEstimateNextQuarter(Double epsEstimateNextQuarter) {
        this.epsEstimateNextQuarter = epsEstimateNextQuarter;
    }

    public Double getEpsEstimateNextYear() {
        return epsEstimateNextYear;
    }

    public void setEpsEstimateNextYear(Double epsEstimateNextYear) {
        this.epsEstimateNextYear = epsEstimateNextYear;
    }

    public Double getMarketCap() {
        return marketCap;
    }

    public void setMarketCap(Double marketCap) {
        this.marketCap = marketCap;
    }

    public Double getOneYearTargetPrice() {
        return oneYearTargetPrice;
    }

    public void setOneYearTargetPrice(Double oneYearTargetPrice) {
        this.oneYearTargetPrice = oneYearTargetPrice;
    }

    public Double getPe() {
        return pe;
    }

    public void setPe(Double pe) {
        this.pe = pe;
    }

    public Double getPeg() {
        return peg;
    }

    public void setPeg(Double peg) {
        this.peg = peg;
    }

    public Double getPriceBook() {
        return priceBook;
    }

    public void setPriceBook(Double priceBook) {
        this.priceBook = priceBook;
    }

    public Double getPriceSales() {
        return priceSales;
    }

    public void setPriceSales(Double priceSales) {
        this.priceSales = priceSales;
    }

    public Double getRevenue() {
        return revenue;
    }

    public void setRevenue(Double revenue) {
        this.revenue = revenue;
    }

    public Integer getSharesFloat() {
        return sharesFloat;
    }

    public void setSharesFloat(Integer sharesFloat) {
        this.sharesFloat = sharesFloat;
    }

    public Integer getSharesOutstanding() {
        return sharesOutstanding;
    }

    public void setSharesOutstanding(Integer sharesOutstanding) {
        this.sharesOutstanding = sharesOutstanding;
    }

    public Double getShortRatio() {
        return shortRatio;
    }

    public void setShortRatio(Double shortRatio) {
        this.shortRatio = shortRatio;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}
