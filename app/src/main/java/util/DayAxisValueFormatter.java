package util;

import android.util.Log;

import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import yahoofinance.histquotes.HistoricalQuote;

import static android.content.ContentValues.TAG;

/**
 * Created by philipp on 02/06/16.
 */
public class DayAxisValueFormatter implements IAxisValueFormatter {


    private BarLineChartBase<?> chart;
    private List<HistoricalQuote> historicalQuotes;

    public DayAxisValueFormatter(BarLineChartBase<?> chart, List<HistoricalQuote> historicalQuotes) {
        this.chart = chart;
        this.historicalQuotes = historicalQuotes;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {


        Date data = new Date();

        int index = (int) value;

        Calendar transactionCalendar = historicalQuotes.get(index).getDate();

        SimpleDateFormat formatDate = new SimpleDateFormat("MMMM d yyyy");
        Log.d(TAG, "David: "  + " : " + formatDate.format(transactionCalendar.getTime()));

        return formatDate.format(transactionCalendar.getTime());
    }

    private int getDaysForMonth(int month, int year) {

        // month is 0-based

        if (month == 1) {
            int x400 = month % 400;
            if (x400 < 0)
            {
                x400 = -x400;
            }
            boolean is29 = (month % 4) == 0 && x400 != 100 && x400 != 200 && x400 != 300;

            return is29 ? 29 : 28;
        }

        if (month == 3 || month == 5 || month == 8 || month == 10)
            return 30;
        else
            return 31;
    }

    private int determineMonth(int dayOfYear) {

        int month = -1;
        int days = 0;

        while (days < dayOfYear) {
            month = month + 1;

            if (month >= 12)
                month = 0;

            int year = determineYear(days);
            days += getDaysForMonth(month, year);
        }

        return Math.max(month, 0);
    }

    private int determineDayOfMonth(int days, int month) {

        int count = 0;
        int daysForMonths = 0;

        while (count < month) {

            int year = determineYear(daysForMonths);
            daysForMonths += getDaysForMonth(count % 12, year);
            count++;
        }

        return days - daysForMonths;
    }

    private int determineYear(int days) {

        if (days <= 366)
            return 2016;
        else if (days <= 730)
            return 2017;
        else if (days <= 1094)
            return 2018;
        else if (days <= 1458)
            return 2019;
        else
            return 2020;

    }
}