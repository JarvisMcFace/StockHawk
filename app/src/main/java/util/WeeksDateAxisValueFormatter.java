package util;

import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import yahoofinance.histquotes.HistoricalQuote;

/**
 * @author David
 */
public class WeeksDateAxisValueFormatter implements IAxisValueFormatter {


    private BarLineChartBase<?> chart;
    private List<HistoricalQuote> historicalQuotes;

    public WeeksDateAxisValueFormatter(BarLineChartBase<?> chart, List<HistoricalQuote> historicalQuotes) {
        this.chart = chart;
        this.historicalQuotes = historicalQuotes;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {

        int index = (int) value;
        Calendar transactionCalendar = historicalQuotes.get(index).getDate();
        SimpleDateFormat formatDate = new SimpleDateFormat("MMM d, ''yy");
        return formatDate.format(transactionCalendar.getTime());
    }
}