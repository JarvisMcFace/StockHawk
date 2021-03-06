
package com.udacity.stockhawk.fragment;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;
import com.udacity.stockhawk.R;

import static com.github.mikephil.charting.utils.Utils.formatNumber;


/**
 * Custom implementation of the MarkerView.
 *
 * @author Philipp Jahoda
 */
public class StockClosePriceMarker extends MarkerView {

    private TextView tvContent;

    public StockClosePriceMarker(Context context, int layoutResource) {
        super(context, layoutResource);

        tvContent = (TextView) findViewById(R.id.close_price);
    }

    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {

        if (e instanceof CandleEntry) {

            CandleEntry ce = (CandleEntry) e;
            String ceHight = formatNumber(ce.getHigh(), 0, true);
            tvContent.setText(ceHight);
        } else {
            String entryY = Utils.formatNumber(e.getY(), 0, true);
            tvContent.setText(entryY);
        }

        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }
}
