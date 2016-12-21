package com.udacity.stockhawk.fragment;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.QuoteContract;
import com.udacity.stockhawk.to.StockTO;

import butterknife.ButterKnife;
import util.StockSymbolCursorHelper;

import static android.content.ContentValues.TAG;

/**
 * Created by David on 12/18/16.
 */
public class StockDetailsFragment extends Fragment {

    public static final String STOCK_SYMBOL =  "com.udacity.stockhawk.fragment.stock.symbol";
    private View rootView;
    private String symbol;
    private StockTO stockTO;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_stock_details, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);


        Intent intent = getActivity().getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null ) {
            symbol = bundle.getString(STOCK_SYMBOL);
        }

        ContentResolver contentResolver = getActivity().getApplication().getContentResolver();
        Cursor cursor = contentResolver.query(QuoteContract.Quote.makeUriForStock(symbol), null, null, null, null);

        if (cursor != null || cursor.getCount() == 1) {

            stockTO = StockSymbolCursorHelper.retrieveStockHistory(cursor);
            cursor.close();
        }

        Log.d(TAG, "David: " + "onActivityCreated() called with: savedInstanceState = [" + savedInstanceState + "]");
    }

}
