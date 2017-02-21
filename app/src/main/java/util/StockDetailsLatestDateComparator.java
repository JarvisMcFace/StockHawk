package util;

import java.util.Calendar;
import java.util.Comparator;

import yahoofinance.histquotes.HistoricalQuote;

/**
 * Created by David on 12/23/16.
 */
public class StockDetailsLatestDateComparator implements Comparator<HistoricalQuote> {

    @Override
    public int compare(HistoricalQuote first, HistoricalQuote second) {
        Calendar firstCalendarDate = first.getDate();
        Calendar secondCalendarDate = second.getDate();

        if ( firstCalendarDate.before(secondCalendarDate) ) return -1;
        if ( firstCalendarDate.after(secondCalendarDate) ) return 1;
        return 0;
    }
}
