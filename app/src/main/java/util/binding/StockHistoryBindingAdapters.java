package util.binding;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.widget.TextView;

import com.udacity.stockhawk.R;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by David on 2/5/17.
 */
public class StockHistoryBindingAdapters {

    private static final String TAG = "BindingAdapters";

    @BindingAdapter("history_close_date")
    public static void setHistoryCloseDate(TextView textView, Calendar historicalDate) {

        Context context = textView.getContext();
        if (historicalDate != null) {
            Calendar calendar = Calendar.getInstance();
            Date date = new Date(historicalDate.getTimeInMillis());

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E, d MMM yyyy");
            String weekEnding = context.getString(R.string.week_eneding);
            String displayResult = weekEnding  + " " + simpleDateFormat.format(date);
            textView.setText(displayResult);
        } else {
            textView.setText(context.getString(R.string.not_available));
        }
    }

    @BindingAdapter("history_opening_price")
    public static void setHistoryOpeningPrice(TextView textView, BigDecimal openingPrice) {

        Context context = textView.getContext();

        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        if (openingPrice != null) {
            String displayedResults = "$" + decimalFormat.format(openingPrice);
            textView.setText(displayedResults);
        } else {
            textView.setText(context.getString(R.string.not_available));
        }
    }

    @BindingAdapter("history_closing_price")
    public static void setHistoryClosingPrice(TextView textView, BigDecimal closingPrice) {

        Context context = textView.getContext();
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        if (closingPrice != null) {
            String displayedResults = "$" + decimalFormat.format(closingPrice);
            textView.setText(displayedResults);
        } else {
            textView.setText(context.getString(R.string.not_available));
        }
    }

    @BindingAdapter("history_adjusted_price")
    public static void setHistoryAdjustedPrice(TextView textView, BigDecimal adjustedPrice) {
        Context context = textView.getContext();
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        if (adjustedPrice != null) {
            String displayedResults = "$" + decimalFormat.format(adjustedPrice);
            textView.setText(displayedResults);
        } else {
            textView.setText(context.getString(R.string.not_available));
        }
    }

    @BindingAdapter("history_high_price")
    public static void setHistoryHighPrice(TextView textView, BigDecimal highPrice) {
        Context context = textView.getContext();
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        if (highPrice != null) {
            String displayedResults = "$" + decimalFormat.format(highPrice);
            textView.setText(displayedResults);
        } else {
            textView.setText(context.getString(R.string.not_available));
        }
    }


    @BindingAdapter("history_low_price")
    public static void setHistoryLowPrice(TextView textView, BigDecimal lowPrice) {
        Context context = textView.getContext();
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        if (lowPrice != null) {
            String displayedResults = "$" + decimalFormat.format(lowPrice);
            textView.setText(displayedResults);
        } else {
            textView.setText(context.getString(R.string.not_available));
        }
    }

    @BindingAdapter("history_volume")
    public static void setHistoryVolume(TextView textView, Long volume) {
        Context context = textView.getContext();

        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        if (volume != null) {
            String displayedResults = decimalFormat.format(volume);
            textView.setText(displayedResults);
        } else {
            textView.setText(context.getString(R.string.not_available));
        }
    }
}
