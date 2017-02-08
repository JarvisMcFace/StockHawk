package com.udacity.stockhawk.service;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by David on 2/7/17.
 */

public class StockWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new StockRemoteViewFactory(this.getApplication(), intent);
    }
}
