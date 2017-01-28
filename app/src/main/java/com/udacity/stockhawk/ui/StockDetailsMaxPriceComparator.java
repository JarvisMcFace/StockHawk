package com.udacity.stockhawk.ui;

import java.util.Comparator;

import yahoofinance.histquotes.HistoricalQuote;

/**
 * Created by David on 12/23/16.
 */
public class StockDetailsMaxPriceComparator  implements Comparator<HistoricalQuote> {

    @Override
    public int compare(HistoricalQuote first, HistoricalQuote second) {
        return Float.compare(first.getClose().floatValue(), second.getClose().floatValue());
    }
}
