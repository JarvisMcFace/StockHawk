package util;

import android.os.AsyncTask;

import java.io.IOException;
import java.lang.ref.WeakReference;

import timber.log.Timber;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.quotes.stock.StockQuote;

/**
 * Created by David on 9/24/16.
 */
public class SymbolLookup extends AsyncTask<String, Void, Boolean> {

    private WeakReference<CallbackWeakReference> callbackWeakReference;

    public SymbolLookup(WeakReference<CallbackWeakReference> callback) {
        this.callbackWeakReference = callback;
    }


    @Override
    protected Boolean doInBackground(String... params) {

        Stock stock;
        String symbol = params[0];

        try {
            stock = YahooFinance.get(symbol);
        } catch (IOException exception) {
            Timber.e(exception, "Error fetching stock quotes");
            return false;
        }
        if (stock == null) {
            return false;
        }

        if (StringUtils.isEmpty(stock.getName())) {
            return false;
        }

        StockQuote quote = stock.getQuote();

        if (quote == null) {
            return false;
        }

        if (quote.getPrice() == null) {
            return false;
        }

        return true;
    }

    @Override
    protected void onPostExecute(Boolean results) {
        CallbackWeakReference callback = callbackWeakReference.get();
        callback.symbolAvailable(results);
    }
}
