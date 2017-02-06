package util.binding;

import android.databinding.BindingAdapter;
import android.util.Log;
import android.widget.TextView;

import com.udacity.stockhawk.to.StockHistoryDateTO;

/**
 * Created by David on 2/5/17.
 */
public class StockHistoryBindingAdapters {

    private static final String TAG = "BindingAdapters";

    @BindingAdapter("history_close_date")
    public static void setHistoryCloseDate(TextView textView, StockHistoryDateTO stockHistoryDateTO) {

        Log.d(TAG, "David: " + "setHistoryCloseDate() called with: textView = [" + textView + "], stockHistoryDateTO = [" + stockHistoryDateTO + "]");
    }

    @BindingAdapter("history_opening_price")
    public static void setHistoryOpeningPrice(TextView textView, Double openingPrice) {
        Log.d(TAG, "David: " + "setHistoryOpeningPrice() called with: textView = [" + textView + "], openingPrice = [" + openingPrice + "]");
    }

    @BindingAdapter("history_closing_price")
    public static void setHistoryClosingPrice(TextView textView, Double closingPrice) {
        Log.d(TAG, "David: " + "setHistoryClosingPrice() called with: textView = [" + textView + "], closingPrice = [" + closingPrice + "]");
    }

    @BindingAdapter("history_adjusted_price")
    public static void setHistoryAdjustedPrice(TextView textView, Double adjustedPrice) {
        Log.d(TAG, "David: " + "setHistoryAdjustedPrice() called with: textView = [" + textView + "], adjustedPrice = [" + adjustedPrice + "]");
    }

    @BindingAdapter("history_high_price")
    public static void setHistoryHighPrice(TextView textView, Double highPrice) {
        Log.d(TAG, "David: " + "setHistoryHighPrice() called with: textView = [" + textView + "], highPrice = [" + highPrice + "]");
    }

    @BindingAdapter("history_low_price")
    public static void setHistoryLowPrice(TextView textView, Double lowPrice) {
        Log.d(TAG, "David: " + "setHistoryLowPrice() called with: textView = [" + textView + "], lowPrice = [" + lowPrice + "]");
    }

    @BindingAdapter("history_volume")
    public static void setHistoryVolume(TextView textView, Integer volume) {
        Log.d(TAG, "David: " + "setHistoryVolume() called with: textView = [" + textView + "], volume = [" + volume + "]");
    }
}
