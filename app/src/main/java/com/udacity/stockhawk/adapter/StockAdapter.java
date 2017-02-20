package com.udacity.stockhawk.adapter;


import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.PreferencesUtils;
import com.udacity.stockhawk.data.QuoteContract;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StockAdapter extends RecyclerView.Adapter<StockAdapter.StockViewHolder> {

    private final Context context;
    private final DecimalFormat dollarFormatWithPlus;
    private final DecimalFormat dollarFormat;
    private final DecimalFormat percentageFormat;
    private Cursor cursor;
    private StockAdapterOnClickHandler clickHandler;

    public StockAdapter(Context context, StockAdapterOnClickHandler clickHandler) {
        this.context = context;
        this.clickHandler = clickHandler;

        dollarFormat = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);
        dollarFormatWithPlus = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);
        dollarFormatWithPlus.setPositivePrefix("+$");
        percentageFormat = (DecimalFormat) NumberFormat.getPercentInstance(Locale.getDefault());
        percentageFormat.setMaximumFractionDigits(2);
        percentageFormat.setMinimumFractionDigits(2);
        percentageFormat.setPositivePrefix("+");
    }

    public void setCursor(Cursor cursor) {
        this.cursor = cursor;
        notifyDataSetChanged();
    }

    public String getSymbolAtPosition(int position) {

        cursor.moveToPosition(position);
        return cursor.getString(QuoteContract.Quote.POSITION_SYMBOL);
    }


    @Override
    public StockViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View item = LayoutInflater.from(context).inflate(R.layout.list_item_quote, parent, false);

        return new StockViewHolder(item);
    }

    @Override
    public void onBindViewHolder(StockViewHolder holder, int position) {

        cursor.moveToPosition(position);

        String stockName = cursor.getString(QuoteContract.Quote.POSITION_NAME);
        String stockSymbol = cursor.getString(QuoteContract.Quote.POSITION_SYMBOL);
        String price = dollarFormat.format(cursor.getFloat(QuoteContract.Quote.POSITION_PRICE));

        holder.symbol.setText(stockSymbol);

        String symbolContentDescription = context.getString(R.string.stockSymbolAdapterContentDesc, stockName, stockSymbol);
        holder.symbol.setContentDescription(symbolContentDescription);

        holder.name.setText(stockName);
        holder.price.setText(price);
        String closePriceContentDescription = context.getString(R.string.stockClosePriceAdapterContentDesc, price);
        holder.price.setContentDescription(closePriceContentDescription);

        float rawAbsoluteChange = cursor.getFloat(QuoteContract.Quote.POSITION_ABSOLUTE_CHANGE);
        float percentageChange = cursor.getFloat(QuoteContract.Quote.POSITION_PERCENTAGE_CHANGE);

        if (rawAbsoluteChange > 0) {
            holder.change.setBackgroundResource(R.drawable.percent_change_pill_green);
        } else {
            holder.change.setBackgroundResource(R.drawable.percent_change_pill_red);
        }

        String change = dollarFormatWithPlus.format(rawAbsoluteChange);
        String percentage = percentageFormat.format(percentageChange / 100);
        String changeDirection = "";



        if (PreferencesUtils.getDisplayMode(context).equals(context.getString(R.string.pref_display_mode_absolute_key))) {
            holder.change.setText(change);
            String dollarChangeContentDescription = context.getString(R.string.stockDarllarChangeAdapterContentDesc, change);
            holder.change.setContentDescription(dollarChangeContentDescription);

        } else {
            holder.change.setText(percentage);
            String percentChangeContentDescription = context.getString(R.string.stockPercentChangeAdapterContentDesc, percentage);
            holder.change.setContentDescription(percentChangeContentDescription);
        }
    }

    @Override
    public int getItemCount() {
        int count = 0;
        if (cursor != null) {
            count = cursor.getCount();
        }
        return count;
    }


    public interface StockAdapterOnClickHandler {
        void onClick(String symbol);
    }

    class StockViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.symbol)
        TextView symbol;

        @BindView(R.id.price)
        TextView price;

        @BindView(R.id.change)
        TextView change;
        @BindView(R.id.name)
        TextView name;

        StockViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            cursor.moveToPosition(adapterPosition);
            int symbolColumn = cursor.getColumnIndex(QuoteContract.Quote.COLUMN_SYMBOL);
            clickHandler.onClick(cursor.getString(symbolColumn));

        }


    }
}
