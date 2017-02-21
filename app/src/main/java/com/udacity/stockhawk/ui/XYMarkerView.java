
package com.udacity.stockhawk.ui;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.udacity.stockhawk.R;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import yahoofinance.histquotes.HistoricalQuote;

/**
 * Custom implementation of the MarkerView.
 *
 * @author David
 */
public class XYMarkerView extends MarkerView {

    private TextView closeDateView;
    private TextView closeAmount;
    private DecimalFormat format;
    private List<HistoricalQuote> historicalQuotes;

    public XYMarkerView(Context context, List<HistoricalQuote> historicalQuotes) {
        super(context, R.layout.custom_marker_view);
        this.historicalQuotes = historicalQuotes;
        closeDateView = (TextView) findViewById(R.id.close_date);
        closeAmount = (TextView) findViewById(R.id.close_price);
        format = new DecimalFormat("###.00");
    }


    @Override
    public void refreshContent(Entry entry, Highlight highlight) {

        int index = (int) entry.getX();
        Calendar transactionCalendar = historicalQuotes.get(index).getDate();
        SimpleDateFormat formatDate = new SimpleDateFormat("MM/dd/yyyy");
        String closeDate = formatDate.format(transactionCalendar.getTime());
        String closePrice = "$" + format.format(entry.getY());
        closeDateView.setText(closeDate);
        closeAmount.setText(closePrice);
        super.refreshContent(entry, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }
}
