package util;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import com.udacity.stockhawk.data.QuoteContract;
import com.udacity.stockhawk.to.StockTO;

import static com.udacity.stockhawk.fragment.StockChartDetailsFragment.STOCK_SYMBOL;

/**
 * Created by David on 2/4/17.
 */
public class RetrieveStockTOFromIntent {

    public static StockTO execute(Activity activity) {

        StockTO stockTO = null;
        String symbol = null;
        Intent intent = activity.getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            symbol = bundle.getString(STOCK_SYMBOL);
        }

        if (StringUtils.isNotEmpty(symbol)) {

            ContentResolver contentResolver = activity.getApplication().getContentResolver();
            Cursor cursor = contentResolver.query(QuoteContract.Quote.makeUriForStock(symbol), null, null, null, null);

            if (cursor != null || cursor.getCount() == 1) {
                stockTO = StockSymbolCursorHelper.retrieveStockHistory(cursor);
                cursor.close();
            }
        }

        return stockTO;
    }
}
