
package com.udacity.stockhawk.to;

import java.util.HashMap;
import java.util.Map;

public class StockDividendTO {

    private Double annualYield;
    private Double annualYieldPercent;
    private ExDateTO exDate;
    private PayDateTO payDate;
    private String symbol;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Double getAnnualYield() {
        return annualYield;
    }

    public void setAnnualYield(Double annualYield) {
        this.annualYield = annualYield;
    }

    public Double getAnnualYieldPercent() {
        return annualYieldPercent;
    }

    public void setAnnualYieldPercent(Double annualYieldPercent) {
        this.annualYieldPercent = annualYieldPercent;
    }

    public ExDateTO getExDate() {
        return exDate;
    }

    public void setExDate(ExDateTO exDate) {
        this.exDate = exDate;
    }

    public PayDateTO getPayDate() {
        return payDate;
    }

    public void setPayDate(PayDateTO payDate) {
        this.payDate = payDate;
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
