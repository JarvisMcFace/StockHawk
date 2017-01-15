package util;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.DecimalFormat;

/**
 * @author David
 */
public class CurrencyAmountAxisValueFormatter implements IAxisValueFormatter {

    public CurrencyAmountAxisValueFormatter() {}

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        DecimalFormat decimalFormat = new DecimalFormat("0");
        return "$" + decimalFormat.format(value);
    }
}