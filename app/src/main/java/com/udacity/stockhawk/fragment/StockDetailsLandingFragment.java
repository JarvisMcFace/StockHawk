package com.udacity.stockhawk.fragment;

import android.app.Fragment;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.adapter.StockDetailsTabFragmentPagerAdapter;
import com.udacity.stockhawk.databinding.FragmentStockDetailsLandingBinding;
import com.udacity.stockhawk.to.StockTO;

import java.lang.ref.WeakReference;

import util.RetrieveStockTOFromIntent;

/**
 * Created by David on 1/27/17.
 */
public class StockDetailsLandingFragment extends Fragment {

    public static final String STOCK_SYMBOL = "com.udacity.stockhawk.fragment.stock.symbol";

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private PagerAdapter tabStockDetailsViewPagerAdapter;
    private FragmentStockDetailsLandingBinding fragmentStockDetailsLandingBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentStockDetailsLandingBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_stock_details_landing, null, false);
        return fragmentStockDetailsLandingBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);

        viewPager = fragmentStockDetailsLandingBinding.stockDetailsTabViewPager;
        tabLayout = fragmentStockDetailsLandingBinding.appBarLayout.tabLayout;

        initToolbar();
        initTabLayout();
    }



    private void initToolbar() {

        Toolbar toolbar = fragmentStockDetailsLandingBinding.appBarLayout.toolbar;
        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        appCompatActivity.setSupportActionBar(toolbar);
        ActionBar actionBar = appCompatActivity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
        StockTO stockTO = RetrieveStockTOFromIntent.execute(getActivity());
        toolbar.setTitle(stockTO.getName());
    }

    private void initTabLayout() {

        tabStockDetailsViewPagerAdapter = new StockDetailsTabFragmentPagerAdapter(getFragmentManager(), new WeakReference<Context>(getActivity()));
        viewPager.setAdapter(tabStockDetailsViewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }


}
