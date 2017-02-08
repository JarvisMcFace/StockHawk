package util.widget;

import android.app.Application;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;

import com.udacity.stockhawk.StockInfoWidgetProvider;

/**
 * Created by David on 2/5/17.
 */
public class RefreshStockInformationWidget {

    public static void execute(Application application) {

        Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE, null, application, StockInfoWidgetProvider.class);
        ComponentName componentName = new ComponentName(application, StockInfoWidgetProvider.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.getInstance(application).getAppWidgetIds(componentName));
        application.sendBroadcast(intent);
    }
}
