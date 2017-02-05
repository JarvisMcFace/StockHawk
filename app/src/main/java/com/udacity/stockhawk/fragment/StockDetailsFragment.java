package com.udacity.stockhawk.fragment;

import android.app.Fragment;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.databinding.FragmentStockDetailsBinding;

/**
 * Created by David on 1/29/17.
 */
public class StockDetailsFragment extends Fragment {

    private FragmentStockDetailsBinding fragmentStockDetailsBinding;

    public static Fragment newInstance() {
        return new StockDetailsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentStockDetailsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_stock_details, null, false);
        return fragmentStockDetailsBinding.getRoot();
    }


}
