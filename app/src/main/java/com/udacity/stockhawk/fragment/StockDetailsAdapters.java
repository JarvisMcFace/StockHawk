package com.udacity.stockhawk.fragment;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;
import android.widget.TextView;

import com.udacity.stockhawk.R;

/**
 * Created by David on 12/21/16.
 */

public class StockDetailsAdapters {

    @BindingAdapter("stockPrice")
    public static void setStockPrice(TextView textView, float stockPrice) {

        if (stockPrice > 0) {
            String price = String.valueOf(stockPrice);
            textView.setText(price);
        }
    }

    @BindingAdapter("stockChange")
    public static void setStockChange(TextView textView, float stockPrice) {

        Context context = textView.getContext();

        if (stockPrice > 0) {
            int color = ContextCompat.getColor(context, R.color.material_green_700);
            textView.setTextColor(color);
        } else {
            int color = ContextCompat.getColor(context, R.color.material_red_700);
            textView.setTextColor(color);
        }
        String change = String.valueOf(stockPrice);
        textView.setText(change);
    }

    @BindingAdapter("stockChangePercent")
    public static void setStockChangePercent(TextView textView, float stockPrice) {

        Context context = textView.getContext();

        if (stockPrice > 0) {
            int color = ContextCompat.getColor(context, R.color.material_green_700);
            textView.setTextColor(color);
        } else {
            stockPrice = stockPrice * -1;
            int color = ContextCompat.getColor(context, R.color.material_red_700);
            textView.setTextColor(color);
        }
        String percent = "(" + String.valueOf(stockPrice) + "%)";
        textView.setText(percent);
    }

    @BindingAdapter("stockDirectionArrow")
    public static void setStockDirectionArrow(ImageView imageView, float stockPrice) {

        Context context = imageView.getContext();

        if (stockPrice > 0) {
            imageView.setImageResource(R.drawable.ic_arrow_upward);
        } else {
            imageView.setImageResource(R.drawable.ic_arrow_downward);
        }
    }
}
