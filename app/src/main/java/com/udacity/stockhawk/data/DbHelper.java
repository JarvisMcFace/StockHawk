package com.udacity.stockhawk.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



class DbHelper extends SQLiteOpenHelper {

    static final String NAME = "StockHawk.db";
    private static final int VERSION = 1;


    DbHelper(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String builder = "CREATE TABLE " +  QuoteContract.Quote.TABLE_NAME + " ("
                + QuoteContract.Quote._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                +  QuoteContract.Quote.COLUMN_SYMBOL + " TEXT NOT NULL, "
                +  QuoteContract.Quote.COLUMN_PRICE + " REAL NOT NULL, "
                +  QuoteContract.Quote.COLUMN_ABSOLUTE_CHANGE + " REAL NOT NULL, "
                +  QuoteContract.Quote.COLUMN_PERCENTAGE_CHANGE + " REAL NOT NULL, "
                +  QuoteContract.Quote.COLUMN_HISTORY + " TEXT NOT NULL, "
                +  QuoteContract.Quote.COLUMN_NAME + " TEXT NOT NULL, "
                + "UNIQUE (" + QuoteContract.Quote.COLUMN_SYMBOL + ") ON CONFLICT REPLACE);";
        db.execSQL(builder);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL(" DROP TABLE IF EXISTS " +  QuoteContract.Quote.TABLE_NAME);
        onCreate(db);
    }
}
