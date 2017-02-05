package com.udacity.stockhawk.fragment;

import android.app.Fragment;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
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
import com.udacity.stockhawk.databinding.FragmentStockChartDetailsBinding;
import com.udacity.stockhawk.to.StockTO;

import util.StockDetailsLatestDateComparator;
import util.StockDetailsMaxPriceComparator;

import com.udacity.stockhawk.ui.XYMarkerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import util.CurrencyAmountAxisValueFormatter;
import util.RetrieveStockTOFromIntent;
import util.WeeksDateAxisValueFormatter;
import yahoofinance.histquotes.HistoricalQuote;

/**
 * Created by David on 12/18/16.
 */
public class StockChartDetailsFragment extends Fragment implements OnChartValueSelectedListener {

    public static final String STOCK_SYMBOL = "com.udacity.stockhawk.fragment.stock.symbol";

    private StockTO stockTO;
    private FragmentStockChartDetailsBinding fragmentStockChartDetailsBinding;
    private Toolbar toolbar;

    public static Fragment newInstance() {
        return new StockChartDetailsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentStockChartDetailsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_stock_chart_details, null, false);
        return fragmentStockChartDetailsBinding.getRoot();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        stockTO = RetrieveStockTOFromIntent.execute(getActivity());

        if (stockTO != null) {
            fragmentStockChartDetailsBinding.setStockInfo(stockTO);
            initToolBar();
        }
    }

    private void initToolBar() {
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(stockTO.getName());
    }


    @Override
    public void onResume() {
        super.onResume();

        if (toolbar != null) {
            toolbar.setTitle(stockTO.getName());
        }
        populateChart();
    }

    private void populateChart() {

        fragmentStockChartDetailsBinding.chart.setOnChartValueSelectedListener(this);

        List<HistoricalQuote> historicalQuotes = stockTO.getHistory();

        float maxClosePrice = getMaxClosePrice(historicalQuotes);
        float minClosePrice = getMinClosePrice(historicalQuotes);

        fragmentStockChartDetailsBinding.chart.setDescription(null);
        fragmentStockChartDetailsBinding.chart.setDrawGridBackground(false);
        fragmentStockChartDetailsBinding.chart.setTouchEnabled(true);
        fragmentStockChartDetailsBinding.chart.setDragEnabled(true);
        fragmentStockChartDetailsBinding.chart.setScaleEnabled(false);
        fragmentStockChartDetailsBinding.chart.setScaleXEnabled(true);
        fragmentStockChartDetailsBinding.chart.setScaleYEnabled(false);
        fragmentStockChartDetailsBinding.chart.getAxisRight().setEnabled(false);
        fragmentStockChartDetailsBinding.chart.setPinchZoom(false);
        fragmentStockChartDetailsBinding.chart.setVisibleXRange(10, 20);
        fragmentStockChartDetailsBinding.chart.animateX(2500);

        IAxisValueFormatter xAxisFormatter = new CurrencyAmountAxisValueFormatter();
        IAxisValueFormatter weekDateAxisValueFormatter = new WeeksDateAxisValueFormatter(fragmentStockChartDetailsBinding.chart, historicalQuotes);
        XYMarkerView mv = new XYMarkerView(getContext(), historicalQuotes);
        mv.setChartView(fragmentStockChartDetailsBinding.chart); // For bounds control
        fragmentStockChartDetailsBinding.chart.setMarker(mv); // Set the marker to the chart

        // x-axis limit line
        LimitLine llXAxis = new LimitLine(10f, "Index 10");
        llXAxis.setLineWidth(4f);
        llXAxis.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        llXAxis.setTextSize(10f);

        XAxis xAxis = fragmentStockChartDetailsBinding.chart.getXAxis();
        xAxis.enableGridDashedLine(10f, 10f, 0f);
        xAxis.setValueFormatter(weekDateAxisValueFormatter);
        xAxis.setLabelRotationAngle(15f);

        String maxLineLimeClosePrice = getString(R.string.max_close_price);
        LimitLine limitLineMax = new LimitLine(maxClosePrice, maxLineLimeClosePrice);
        limitLineMax.setLineWidth(1f);
        limitLineMax.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        limitLineMax.setTextSize(8f);

        YAxis axisLeft = fragmentStockChartDetailsBinding.chart.getAxisLeft();
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
    }

    private void setData(List<HistoricalQuote> historicalQuotes) {

        ArrayList<Entry> values = new ArrayList<Entry>();

        for (int i = 0; i < historicalQuotes.size(); i++) {
            HistoricalQuote historicalQuote = historicalQuotes.get(i);
            float val = historicalQuote.getClose().floatValue();
            values.add(new Entry(i, val));
        }

        LineDataSet lineDataSet;

        if (fragmentStockChartDetailsBinding.chart.getData() != null &&
                fragmentStockChartDetailsBinding.chart.getData().getDataSetCount() > 0) {
            lineDataSet = (LineDataSet) fragmentStockChartDetailsBinding.chart.getData().getDataSetByIndex(0);
            lineDataSet.setValues(values);
            fragmentStockChartDetailsBinding.chart.getData().notifyDataChanged();
            fragmentStockChartDetailsBinding.chart.notifyDataSetChanged();
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
            fragmentStockChartDetailsBinding.chart.setData(data);
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
        //Intentionally left blank
    }

    @Override
    public void onNothingSelected() {
        //Intentionally left blank
    }
}
