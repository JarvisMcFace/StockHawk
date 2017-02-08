package com.udacity.stockhawk.data;

import android.database.Cursor;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.udacity.stockhawk.fragment.StockChartDetailsFragment;
import com.udacity.stockhawk.to.StockTO;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import yahoofinance.histquotes.HistoricalQuote;

/**
 * Created by David on 2/7/17.
 */

public class StockCursorHelper {

    public static List<StockTO> retrieveAllStocks(Cursor cursor) {

        List<StockTO> stockTOs = new ArrayList<StockTO>();
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast() && cursor.getCount() > 0) {
                StockTO stockTO = getStockData(cursor);
                if (stockTO != null) {
                    stockTOs.add(stockTO);
                }
                cursor.moveToNext();
            }
            cursor.close();
        }
        return stockTOs;
    }

    private static StockTO getStockData(Cursor cursor) {

        final int symbolIndex = cursor.getColumnIndex(QuoteContract.Quote.COLUMN_SYMBOL);
        final int priceIndex = cursor.getColumnIndex(QuoteContract.Quote.COLUMN_PRICE);
        final int nameIndex = cursor.getColumnIndex(QuoteContract.Quote.COLUMN_NAME);
        final int historyIndex = cursor.getColumnIndex(QuoteContract.Quote.COLUMN_HISTORY);
        try {

            String symbol = cursor.getString(symbolIndex);
            String price = cursor.getString(priceIndex);
            String name = cursor.getString(nameIndex);
            String history = cursor.getString(historyIndex);

            Gson gson = new Gson();
            Type listOfTestObject = new TypeToken<List<HistoricalQuote>>() {
            }.getType();
            List<HistoricalQuote> stockHistoryTOs = gson.fromJson(history, listOfTestObject);


            StockTO stockTO = new StockTO();
            stockTO.setSymbol(symbol);
            stockTO.setName(name);
            stockTO.setPrice(Float.valueOf(priceIndex));
            stockTO.setHistory(stockHistoryTOs);

            Log.d("Blah", "David: " + "getStockData() called with: cursor = [" + cursor + "]");
            return stockTO;
        } catch (Exception ex) {
            Log.d(StockChartDetailsFragment.class.getSimpleName(), "StockSymbolCursorHelper getStockHistoryTO: ");
            return null;
        }

    }


}
