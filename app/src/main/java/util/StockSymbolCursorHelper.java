package util;

import android.database.Cursor;
import android.util.Log;

import com.google.gson.Gson;
import com.udacity.stockhawk.data.QuoteContract;
import com.udacity.stockhawk.fragment.StockDetailsFragment;
import com.udacity.stockhawk.to.StockDividendTO;
import com.udacity.stockhawk.to.StockHistoryTO;
import com.udacity.stockhawk.to.StockStatsTO;
import com.udacity.stockhawk.to.StockTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by David on 11/7/16.
 */
public class StockSymbolCursorHelper {

    public static StockTO retrieveStockHistory(Cursor cursor) {

        StockTO stockTO = null;

        if (cursor != null) {
            cursor.moveToFirst();
            try {
                stockTO = getStockHistoryDetailsTO(cursor);
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        return stockTO;
    }


    private static StockTO getStockHistoryDetailsTO(Cursor cursor) {

        try {

            String name = cursor.getString(QuoteContract.Quote.POSITION_NAME);
            String symbol = cursor.getString(QuoteContract.Quote.POSITION_SYMBOL);
            float price = Float.parseFloat(cursor.getString(QuoteContract.Quote.POSITION_PRICE));
            float absoluteChange = Float.parseFloat(cursor.getString(QuoteContract.Quote.POSITION_ABSOLUTE_CHANGE));
            float percentChange = Float.parseFloat(cursor.getString(QuoteContract.Quote.POSITION_PERCENTAGE_CHANGE));
            String history = cursor.getString(QuoteContract.Quote.POSITION_HISTORY);
            String dividend = cursor.getString(QuoteContract.Quote.POSITION_DIVIDEND);
            String stats = cursor.getString(QuoteContract.Quote.POSITION_STATS);
            String currency = cursor.getString(QuoteContract.Quote.POSITION_CURRENCY);

            Gson gson = new Gson();
            List<StockHistoryTO> stockHistoryTOs = gson.fromJson(history, ArrayList.class);
            StockDividendTO dividendTO = gson.fromJson(dividend, StockDividendTO.class);
            StockStatsTO statsTO = gson.fromJson(stats, StockStatsTO.class);

            StockTO stockTO = new StockTO(name, symbol, price, absoluteChange, percentChange, stockHistoryTOs, dividendTO, statsTO, currency);

            return stockTO;

        } catch (Exception ex) {
            Log.d(StockDetailsFragment.class.getSimpleName(), "StockSymbolCursorHelper getStockHistoryTO: ");
            return null;
        }
    }
}
