package com.udacity.stockhawk.service;

import android.app.Application;
import android.appwidget.AppWidgetManager;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.QuoteContract;
import com.udacity.stockhawk.data.StockCursorHelper;
import com.udacity.stockhawk.to.StockTO;

import java.text.DecimalFormat;
import java.util.List;

import util.ListUtils;
import util.StringUtils;

/**
 * Created by David on 2/7/17.
 */

public class StockRemoteViewFactory implements RemoteViewsService.RemoteViewsFactory {

    private Application application;
    private int appWidgetId;
    private List<StockTO> stockTOs;


    public StockRemoteViewFactory(Application application, Intent intent) {
        this.application = application;
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        setWidgetStocks();
    }

    @Override
    public void onCreate() {
        stockTOs.clear();
    }

    @Override
    public void onDataSetChanged() {

        setWidgetStocks();

    }

    @Override
    public void onDestroy() {
        stockTOs.clear();
    }

    @Override
    public int getCount() {
        if (stockTOs == null) {
            return 0;
        }
        return stockTOs.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {

        RemoteViews remoteViews = new RemoteViews(application.getPackageName(), R.layout.widget_stock_info_item);

        if (ListUtils.isEmpty(stockTOs)) {
            return remoteViews;
        }


        StockTO stockTO = stockTOs.get(position);

        String stockName = stockTO.getName();
        String stockSymbol = stockTO.getSymbol();
        float stockPrice = stockTO.getPrice();

        if (StringUtils.isNotEmpty(stockName)) {
            remoteViews.setTextViewText(R.id.widget_stock_name, stockName);
        }

        if (StringUtils.isNotEmpty(stockSymbol)) {
            remoteViews.setTextViewText(R.id.widget_stock_symbol, stockSymbol);
        }

        if (!Float.isNaN(stockPrice)) {
            DecimalFormat decimalFormat = new DecimalFormat("0.00");
            String displayedResults = "$" + decimalFormat.format(stockPrice);
            remoteViews.setTextViewText(R.id.widget_stock_price, displayedResults);
        }

        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        RemoteViews remoteViews = new RemoteViews(application.getPackageName(), R.layout.widget_stock_loading_state);
        return remoteViews;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    private void setWidgetStocks() {
        ContentResolver contentResolver = application.getContentResolver();
        Cursor cursor = contentResolver.query(QuoteContract.Quote.URI, null, null, null, null);
        stockTOs = StockCursorHelper.retrieveAllStocks(cursor);
    }
}
