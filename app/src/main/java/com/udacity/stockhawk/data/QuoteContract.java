package com.udacity.stockhawk.data;


import android.net.Uri;
import android.provider.BaseColumns;

public final class QuoteContract {

    static final String AUTHORITY = "com.udacity.stockhawk";
    static final String PATH_QUOTE = "quote";
    static final String PATH_QUOTE_WITH_SYMBOL = "quote/*";
    private static final Uri BASE_URI = Uri.parse("content://" + AUTHORITY);

    private QuoteContract() {
    }

    public static final class Quote implements BaseColumns {

        public static final Uri URI = BASE_URI.buildUpon().appendPath(PATH_QUOTE).build();

        public static final String COLUMN_SYMBOL = "symbol";
        public static final String COLUMN_PRICE = "price";
        public static final String COLUMN_ABSOLUTE_CHANGE = "absolute_change";
        public static final String COLUMN_PERCENTAGE_CHANGE = "percentage_change";
        public static final String COLUMN_HISTORY = "history";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_DIVIDEND = "dividend";
        public static final String COLUMN_STATS = "stats";
        public static final String COLUMN_CURRENCY = "currency";
        public static final String COLUMN_LAST_UPDATED = "lastupdated";

        public static final int POSITION_ID = 0;
        public static final int POSITION_SYMBOL = 1;
        public static final int POSITION_PRICE = 2;
        public static final int POSITION_ABSOLUTE_CHANGE = 3;
        public static final int POSITION_PERCENTAGE_CHANGE = 4;
        public static final int POSITION_HISTORY = 5;
        public static final int POSITION_NAME = 6;
        public static final int POSITION_DIVIDEND  = 7;
        public static final int POSITION_STATS  = 8;
        public static final int POSITION_CURRENCY  = 9;
        public static final int POSITION_LAST_UPDATED  = 10;

        public static final String[] QUOTE_COLUMNS = {
                _ID,
                COLUMN_SYMBOL,
                COLUMN_PRICE,
                COLUMN_ABSOLUTE_CHANGE,
                COLUMN_PERCENTAGE_CHANGE,
                COLUMN_HISTORY,
                COLUMN_NAME,
                COLUMN_DIVIDEND,
                COLUMN_STATS,
                COLUMN_CURRENCY,
                COLUMN_LAST_UPDATED
        };

        static final String TABLE_NAME = "quotes";

        public static Uri makeUriForStock(String symbol) {
            return URI.buildUpon().appendPath(symbol).build();
        }

        static String getStockFromUri(Uri queryUri) {
            return queryUri.getLastPathSegment();
        }


    }

}
