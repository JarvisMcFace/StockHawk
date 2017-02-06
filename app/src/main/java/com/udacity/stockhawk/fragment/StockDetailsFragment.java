package com.udacity.stockhawk.fragment;

import android.app.Fragment;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.adapter.StockDetailsHistoryAdapter;
import com.udacity.stockhawk.databinding.FragmentStockDetailsBinding;
import com.udacity.stockhawk.to.StockTO;

import util.RetrieveStockTOFromIntent;

/**
 * Created by David on 1/29/17.
 */
public class StockDetailsFragment extends Fragment {

    private FragmentStockDetailsBinding fragmentStockDetailsBinding;
    private StockDetailsHistoryAdapter historyAdapter;
    private StockTO stockTO;

    public static Fragment newInstance() {
        return new StockDetailsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentStockDetailsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_stock_details, null, false);
        return fragmentStockDetailsBinding.getRoot();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        fragmentStockDetailsBinding.recyclerViewWeeklyClose.setLayoutManager(linearLayoutManager);

        stockTO = RetrieveStockTOFromIntent.execute(getActivity());

    }

    @Override
    public void onResume() {
        super.onResume();

        if (historyAdapter == null) {
            historyAdapter = new StockDetailsHistoryAdapter(stockTO.getHistory());
        }

        fragmentStockDetailsBinding.recyclerViewWeeklyClose.setAdapter(historyAdapter);

    }
}
