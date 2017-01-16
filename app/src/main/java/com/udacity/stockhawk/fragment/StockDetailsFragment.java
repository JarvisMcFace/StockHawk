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

import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Utils;
import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.QuoteContract;
import com.udacity.stockhawk.databinding.FragmentStockDetailsBinding;
import com.udacity.stockhawk.to.StockTO;
import com.udacity.stockhawk.ui.XYMarkerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import util.CurrencyAmountAxisValueFormatter;
import util.StockSymbolCursorHelper;
import util.WeeksDateAxisValueFormatter;
import yahoofinance.histquotes.HistoricalQuote;

import static android.content.ContentValues.TAG;

/**
 * Created by David on 12/18/16.
 */
public class StockDetailsFragment extends Fragment implements OnChartValueSelectedListener {

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
        fragmentStockDetailsBinding.chart.setOnChartValueSelectedListener(this);

        List<HistoricalQuote> historicalQuotes = stockTO.getHistory();

        float maxClosePrice = getMaxClosePrice(historicalQuotes);
        float minClosePrice = getMinClosePrice(historicalQuotes);


        fragmentStockDetailsBinding.chart.setDescription(null);
        fragmentStockDetailsBinding.chart.setDrawGridBackground(false);
        fragmentStockDetailsBinding.chart.setTouchEnabled(true);
        fragmentStockDetailsBinding.chart.setDragEnabled(true);
        fragmentStockDetailsBinding.chart.setScaleEnabled(false);
        fragmentStockDetailsBinding.chart.setScaleXEnabled(true);
        fragmentStockDetailsBinding.chart.setScaleYEnabled(false);
        fragmentStockDetailsBinding.chart.getAxisRight().setEnabled(false);
        fragmentStockDetailsBinding.chart.setPinchZoom(false);

        IAxisValueFormatter xAxisFormatter = new CurrencyAmountAxisValueFormatter();
        IAxisValueFormatter weekDateAxisValueFormatter = new WeeksDateAxisValueFormatter(fragmentStockDetailsBinding.chart, historicalQuotes);
        XYMarkerView mv = new XYMarkerView(getContext(), historicalQuotes);
        mv.setChartView(fragmentStockDetailsBinding.chart); // For bounds control
        fragmentStockDetailsBinding.chart.setMarker(mv); // Set the marker to the chart


        // x-axis limit line
        LimitLine llXAxis = new LimitLine(10f, "Index 10");
        llXAxis.setLineWidth(4f);
        llXAxis.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        llXAxis.setTextSize(10f);

        XAxis xAxis = fragmentStockDetailsBinding.chart.getXAxis();
        xAxis.enableGridDashedLine(10f, 10f, 0f);
        xAxis.setValueFormatter(weekDateAxisValueFormatter);
        xAxis.setLabelRotationAngle(15f);

        String maxLineLimeClosePrice = getString(R.string.max_close_price);
        LimitLine limitLineMax = new LimitLine(maxClosePrice, maxLineLimeClosePrice);
        limitLineMax.setLineWidth(1f);
        limitLineMax.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        limitLineMax.setTextSize(8f);


        YAxis axisLeft = fragmentStockDetailsBinding.chart.getAxisLeft();
        axisLeft.removeAllLimitLines();
        axisLeft.addLimitLine(limitLineMax);

        axisLeft.setSpaceTop(25l);
        float adjustedMaxiumnClosePrice = maxClosePrice * 1.05f;
        axisLeft.setAxisMaximum(adjustedMaxiumnClosePrice);
        axisLeft.setAxisMinimum(minClosePrice - (minClosePrice * .05f));
        axisLeft.enableGridDashedLine(10f, 10f, 0f);
        axisLeft.setDrawZeroLine(true);
        axisLeft.setValueFormatter(xAxisFormatter);

        // add data
        setData(historicalQuotes);

        fragmentStockDetailsBinding.chart.setVisibleXRange(10, 20);

        fragmentStockDetailsBinding.chart.animateX(2500);
        //mChart.invalidate();

        // get the legend (only possible after setting data)
//        Legend l = fragmentStockDetailsBinding.chart.getLegend();

        // modify the legend ...
//        l.setForm(Legend.LegendForm.LINE);

        // // dont forget to refresh the drawing
        // mChart.invalidate();
    }


    private void setData(List<HistoricalQuote> historicalQuotes) {

        ArrayList<Entry> values = new ArrayList<Entry>();

        for (int i = 0; i < historicalQuotes.size(); i++) {
            HistoricalQuote historicalQuote = historicalQuotes.get(i);
            float val = historicalQuote.getClose().floatValue();
            values.add(new Entry(i, val));
        }

        LineDataSet lineDataSet;

        if (fragmentStockDetailsBinding.chart.getData() != null &&
                fragmentStockDetailsBinding.chart.getData().getDataSetCount() > 0) {
            lineDataSet = (LineDataSet) fragmentStockDetailsBinding.chart.getData().getDataSetByIndex(0);
            lineDataSet.setValues(values);
            fragmentStockDetailsBinding.chart.getData().notifyDataChanged();
            fragmentStockDetailsBinding.chart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type

            String earliestDisplayDate = getEarliestDisplayStartDate(historicalQuotes);
            String latestDisplayDate = getLatestDisplayStartDate(historicalQuotes);

            String dataSetRange = getString(R.string.data_set_range_description, earliestDisplayDate, latestDisplayDate);
            lineDataSet = new LineDataSet(values, dataSetRange);

            // set the line to be drawn like this "- - - - - -"
            lineDataSet.enableDashedLine(10f, 5f, 0f);
            lineDataSet.enableDashedHighlightLine(10f, 5f, 0f);
            lineDataSet.setColor(Color.BLACK);
            lineDataSet.setCircleColor(Color.BLACK);
            lineDataSet.setLineWidth(1f);
            lineDataSet.setCircleRadius(3f);
            lineDataSet.setDrawCircleHole(false);
            lineDataSet.setDrawValues(false);
            lineDataSet.setValueTextSize(9f);
            lineDataSet.setDrawFilled(true);
            lineDataSet.setFormLineWidth(1f);
            lineDataSet.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            lineDataSet.setFormSize(15.f);

            if (Utils.getSDKInt() >= 18) {
                // fill drawable only supported on api level 18 and above
                Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.fade_red);
                lineDataSet.setFillDrawable(drawable);
            } else {
                lineDataSet.setFillColor(Color.BLACK);
            }

            ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
            dataSets.add(lineDataSet); // add the datasets

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

    private Calendar getLatestCloseCalendarDate(List<HistoricalQuote> historicalQuotes) {
        List<HistoricalQuote> latestCloseDate = new ArrayList<>(historicalQuotes);
        Collections.sort(latestCloseDate, new StockDetailsLatestDateComparator());
        return latestCloseDate.get(0).getDate();
    }

    private Calendar getEarlistCloseCalendarDate(List<HistoricalQuote> historicalQuotes) {
        List<HistoricalQuote> earlistCloseDate = new ArrayList<>(historicalQuotes);
        Collections.sort(earlistCloseDate, new StockDetailsLatestDateComparator());
        int quoteSize = historicalQuotes.size() - 1;
        return earlistCloseDate.get(quoteSize).getDate();
    }


    private String getEarliestDisplayStartDate(List<HistoricalQuote> historicalQuotes) {
        Calendar earliestCalendarDate = getEarlistCloseCalendarDate(historicalQuotes);

        SimpleDateFormat formatDate = new SimpleDateFormat("MMM d, yyyy");
        return formatDate.format(earliestCalendarDate.getTime());
    }

    private String getLatestDisplayStartDate(List<HistoricalQuote> historicalQuotes) {
        Calendar latestCalendarDate = getLatestCloseCalendarDate(historicalQuotes);

        SimpleDateFormat formatDate = new SimpleDateFormat("MMM d, yyyy");
        return formatDate.format(latestCalendarDate.getTime());
    }

    @Override
    public void onValueSelected(Entry entry, Highlight highlight) {


    }

    @Override
    public void onNothingSelected() {

    }
}
