package com.udacity.stockhawk.sync;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.udacity.stockhawk.data.PreferencesUtils;
import com.udacity.stockhawk.data.QuoteContract;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import timber.log.Timber;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;
import yahoofinance.quotes.stock.StockQuote;

public final class QuoteSyncJob {

    private static final int ONE_OFF_ID = 2;
    private static final String ACTION_DATA_UPDATED = "com.udacity.stockhawk.ACTION_DATA_UPDATED";
    private static final int PERIOD = 300000;
    private static final int INITIAL_BACKOFF = 10000;
    private static final int PERIODIC_ID = 1;
    private static final int YEARS_OF_HISTORY = 2;

    private QuoteSyncJob() {
    }

    static void getQuotes(Context context) {

        Timber.d("Running sync job");

        Calendar from = Calendar.getInstance();
        Calendar to = Calendar.getInstance();
        from.add(Calendar.YEAR, -YEARS_OF_HISTORY);

        try {

            Set<String> stockPref = PreferencesUtils.getStocks(context);
            Set<String> stockCopy = new HashSet<>();
            stockCopy.addAll(stockPref);
            String[] stockArray = stockPref.toArray(new String[stockPref.size()]);

            Timber.d(stockCopy.toString());

            if (stockArray.length == 0) {
                return;
            }

            Calendar calendar = Calendar.getInstance();
            String lastUpdatedTime = String.valueOf(calendar.getTimeInMillis());

            Map<String, Stock> quotes = YahooFinance.get(stockArray);
            Iterator<String> iterator = stockCopy.iterator();

            Timber.d(quotes.toString());
            ArrayList<ContentValues> quoteContentValues = new ArrayList<>();

            while (iterator.hasNext()) {
                String stockSymbol = iterator.next();

                Stock stock = quotes.get(stockSymbol);
                StockQuote quote = stock.getQuote();

                if (quote.getPrice() == null) {
                    continue;
                }
                final String currency = stock.getCurrency();
                final String stockName = stock.getName();
                float price = quote.getPrice().floatValue();
                float change = quote.getChange().floatValue();
                float percentChange = quote.getChangeInPercent().floatValue();

                // WARNING! Don't request historical data for a stock that doesn't exist!
                // The request will hang forever X_x
                List<HistoricalQuote> history = stock.getHistory(from, to, Interval.WEEKLY);
                Gson gson = new Gson();
                String stockHistoryJson = gson.toJson(history);
                String dividends = gson.toJson(stock.getDividend());
                String stats = gson.toJson(stock.getStats());

                ContentValues quoteCV = getContentValues(lastUpdatedTime, stockSymbol, currency, stockName, price, change, percentChange, stockHistoryJson, dividends, stats);
                quoteContentValues.add(quoteCV);

                updateLastUpdateDate(context, lastUpdatedTime);
            }

            context.getContentResolver()
                    .bulkInsert(
                            QuoteContract.Quote.URI,
                            quoteContentValues.toArray(new ContentValues[quoteContentValues.size()]));

            Intent dataUpdatedIntent = new Intent(ACTION_DATA_UPDATED);
            context.sendBroadcast(dataUpdatedIntent);

        } catch (IOException exception) {
            Timber.e(exception, "Error fetching stock quotes");
        }
    }

    @NonNull
    private static ContentValues getContentValues(String lastUpdatedTime, String stockSymbol, String currency, String stockName, float price, float change, float percentChange, String stockHistoryJson, String dividends, String stats) {
        ContentValues quoteCV = new ContentValues();
        quoteCV.put(QuoteContract.Quote.COLUMN_SYMBOL, stockSymbol);
        quoteCV.put(QuoteContract.Quote.COLUMN_PRICE, price);
        quoteCV.put(QuoteContract.Quote.COLUMN_PERCENTAGE_CHANGE, percentChange);
        quoteCV.put(QuoteContract.Quote.COLUMN_ABSOLUTE_CHANGE, change);
        quoteCV.put(QuoteContract.Quote.COLUMN_HISTORY, stockHistoryJson.toString());
        quoteCV.put(QuoteContract.Quote.COLUMN_NAME, stockName);
        quoteCV.put(QuoteContract.Quote.COLUMN_DIVIDEND, dividends);
        quoteCV.put(QuoteContract.Quote.COLUMN_STATS, stats);
        quoteCV.put(QuoteContract.Quote.COLUMN_CURRENCY, currency);
        quoteCV.put(QuoteContract.Quote.COLUMN_LAST_UPDATED, lastUpdatedTime);
        return quoteCV;
    }

    private static void updateLastUpdateDate(Context context, String lastUpdatedTime) {
        PreferencesUtils.setLastUpdatedDate(context, lastUpdatedTime);
    }

    private static void schedulePeriodic(Context context) {
        Timber.d("Scheduling a periodic task");

        JobInfo.Builder builder = new JobInfo.Builder(PERIODIC_ID, new ComponentName(context, QuoteJobService.class));
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setPeriodic(PERIOD)
                .setBackoffCriteria(INITIAL_BACKOFF, JobInfo.BACKOFF_POLICY_EXPONENTIAL);

        JobScheduler scheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        scheduler.schedule(builder.build());
    }


    public static synchronized void initialize(final Context context) {

        schedulePeriodic(context);
        syncImmediately(context);

    }

    public static synchronized void syncImmediately(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
            Intent nowIntent = new Intent(context, QuoteIntentService.class);
            context.startService(nowIntent);
        } else {

            JobInfo.Builder builder = new JobInfo.Builder(ONE_OFF_ID, new ComponentName(context, QuoteJobService.class));

            builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY).setBackoffCriteria(INITIAL_BACKOFF, JobInfo.BACKOFF_POLICY_EXPONENTIAL);

            JobScheduler scheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
            scheduler.schedule(builder.build());
        }
    }
}
