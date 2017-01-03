package com.udacity.stockhawk.fragment;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;
import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.QuoteContract;
import com.udacity.stockhawk.databinding.FragmentStockDetailsBinding;
import com.udacity.stockhawk.to.StockTO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import util.StockSymbolCursorHelper;
import yahoofinance.histquotes.HistoricalQuote;

import static android.content.ContentValues.TAG;

/**
 * Created by David on 12/18/16.
 */
public class StockDetailsFragment extends Fragment {

    public static final String STOCK_SYMBOL = "com.udacity.stockhawk.fragment.stock.symbol";
    private View rootView;
    private String symbol;
    private StockTO stockTO;
    private FragmentStockDetailsBinding fragmentStockDetailsBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentStockDetailsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_stock_details, null, false);
        return fragmentStockDetailsBinding.getRoot();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);


        Intent intent = getActivity().getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            symbol = bundle.getString(STOCK_SYMBOL);
        }

        ContentResolver contentResolver = getActivity().getApplication().getContentResolver();
        Cursor cursor = contentResolver.query(QuoteContract.Quote.makeUriForStock(symbol), null, null, null, null);

        if (cursor != null || cursor.getCount() == 1) {
            stockTO = StockSymbolCursorHelper.retrieveStockHistory(cursor);
            cursor.close();
        }

        fragmentStockDetailsBinding.setStockInfo(stockTO);
        Log.d(TAG, "David: " + "onActivityCreated() called with: savedInstanceState = [" + savedInstanceState + "]");

    }


    @Override
    public void onResume() {
        super.onResume();
        populateChart();
    }

    private void populateChart() {
//        fragmentStockDetailsBinding.chart.setOnChartGestureListener(this);
//        fragmentStockDetailsBinding.chart.setOnChartValueSelectedListener(this);

        List<HistoricalQuote> historicalQuotes = stockTO.getHistory();

        float maxClosePrice = getMaxClosePrice(historicalQuotes);
        float minClosePrice = getMinClosePrice(historicalQuotes);


        for (HistoricalQuote historicalQuote : historicalQuotes) {
            Calendar transactionCalendar = historicalQuote.getDate();
            SimpleDateFormat formatDate = new SimpleDateFormat("EEEE, MMMM d, yyyy 'at' h:mm a");
            Log.d(TAG, "David: " + historicalQuote.getClose() + " : " + formatDate.format(transactionCalendar.getTime()));
        }

        fragmentStockDetailsBinding.chart.setDescription(null);
        fragmentStockDetailsBinding.chart.setDrawGridBackground(false);
        // no description text
//        fragmentStockDetailsBinding.chart.getDescription().setEnabled(false);

        // enable touch gestures
        fragmentStockDetailsBinding.chart.setTouchEnabled(true);

        // enable scaling and dragging
        fragmentStockDetailsBinding.chart.setDragEnabled(true);
        fragmentStockDetailsBinding.chart.setScaleEnabled(true);
        // mChart.setScaleXEnabled(true);
        // mChart.setScaleYEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        fragmentStockDetailsBinding.chart.setPinchZoom(true);

        // set an alternative background color
        // mChart.setBackgroundColor(Color.GRAY);

        // x-axis limit line
        LimitLine llXAxis = new LimitLine(10f, "Index 10");
        llXAxis.setLineWidth(4f);
        llXAxis.enableDashedLine(10f, 10f, 0f);
        llXAxis.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        llXAxis.setTextSize(10f);

        XAxis xAxis =  fragmentStockDetailsBinding.chart.getXAxis();
        xAxis.enableGridDashedLine(10f, 10f, 0f);
        //xAxis.setValueFormatter(new MyCustomXAxisValueFormatter());
        //xAxis.addLimitLine(llXAxis); // add x-axis limit line

        LimitLine ll1 = new LimitLine(maxClosePrice, "Max Close");
        ll1.setLineWidth(1f);
        ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        ll1.setTextSize(10f);

//        LimitLine ll2 = new LimitLine(minClosePrice, "Lower Limit");
//        ll2.setLineWidth(4f);
//        ll2.enableDashedLine(10f, 10f, 0f);
//        ll2.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
//        ll2.setTextSize(10f);

        YAxis leftAxis =  fragmentStockDetailsBinding.chart.getAxisLeft();
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
        leftAxis.addLimitLine(ll1);

        leftAxis.setSpaceTop(25l);
        float adjustedMaxiumnClosePrice = maxClosePrice * 1.05f;
        leftAxis.setAxisMaximum(adjustedMaxiumnClosePrice);
        leftAxis.setAxisMinimum(minClosePrice);
        //leftAxis.setYOffset(20f);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setDrawZeroLine(false);

        // limit lines are drawn behind data (and not on top)
        leftAxis.setDrawLimitLinesBehindData(true);

        fragmentStockDetailsBinding.chart.getAxisRight().setEnabled(false);

        //mChart.getViewPortHandler().setMaximumScaleY(2f);
        //mChart.getViewPortHandler().setMaximumScaleX(2f);

        // add data
        setData(historicalQuotes);

//        mChart.setVisibleXRange(20);
//        mChart.setVisibleYRange(20f, AxisDependency.LEFT);
//        mChart.centerViewTo(20, 50, AxisDependency.LEFT);

        fragmentStockDetailsBinding.chart.animateX(2500);
        //mChart.invalidate();

        // get the legend (only possible after setting data)
        Legend l =  fragmentStockDetailsBinding.chart.getLegend();

        // modify the legend ...
        l.setForm(Legend.LegendForm.LINE);

        // // dont forget to refresh the drawing
        // mChart.invalidate();
    }

    private void setData(List<HistoricalQuote> historicalQuotes) {

        ArrayList<Entry> values = new ArrayList<Entry>();

        for (int i = 0; i < historicalQuotes.size(); i++) {
            HistoricalQuote  historicalQuote = historicalQuotes.get(i);

            float val = historicalQuote.getClose().floatValue();
            values.add(new Entry(i, val));
        }

        LineDataSet set1;

        if (fragmentStockDetailsBinding.chart.getData() != null &&
                fragmentStockDetailsBinding.chart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet)fragmentStockDetailsBinding.chart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            fragmentStockDetailsBinding.chart.getData().notifyDataChanged();
            fragmentStockDetailsBinding.chart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(values, "DataSet 1");

            // set the line to be drawn like this "- - - - - -"
            set1.enableDashedLine(10f, 5f, 0f);
            set1.enableDashedHighlightLine(10f, 5f, 0f);
            set1.setColor(Color.BLACK);
            set1.setCircleColor(Color.BLACK);
            set1.setLineWidth(1f);
            set1.setCircleRadius(3f);
            set1.setDrawCircleHole(false);
            set1.setValueTextSize(9f);
            set1.setDrawFilled(true);
            set1.setFormLineWidth(1f);
            set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            set1.setFormSize(15.f);

            if (Utils.getSDKInt() >= 18) {
                // fill drawable only supported on api level 18 and above
                Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.fade_red);
                set1.setFillDrawable(drawable);
            }
            else {
                set1.setFillColor(Color.BLACK);
            }

            ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
            dataSets.add(set1); // add the datasets

            // create a data object with the datasets
            LineData data = new LineData(dataSets);

            // set data
            fragmentStockDetailsBinding.chart.setData(data);
        }

    }


    private float getMaxClosePrice(List<HistoricalQuote> historicalQuotes) {
        List<HistoricalQuote> maxPriceSearch = new ArrayList<>(historicalQuotes);
        Collections.sort(maxPriceSearch, new StockDetailsMaxPriceComparator());
        int quoteSize = historicalQuotes.size() - 1;
        return maxPriceSearch.get(quoteSize).getClose().floatValue();
    }

    private float getMinClosePrice(List<HistoricalQuote> historicalQuotes) {
        List<HistoricalQuote> minPriceSearch = new ArrayList<>(historicalQuotes);
        Collections.sort(minPriceSearch, new StockDetailsMaxPriceComparator());
        return minPriceSearch.get(0).getClose().floatValue();
    }

}
