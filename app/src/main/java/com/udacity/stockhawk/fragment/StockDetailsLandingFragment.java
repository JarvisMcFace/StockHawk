package com.udacity.stockhawk.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.adapter.StockDetailsTabFragmentPagerAdapter;

import java.lang.ref.WeakReference;

/**
 * Created by David on 1/27/17.
 */
public class StockDetailsLandingFragment extends Fragment {

    private View rootView;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private PagerAdapter tabStockDetailsViewPagerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_stock_details_landing, container);
        return rootView;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);

        viewPager = (ViewPager) rootView.findViewById(R.id.stock_details_tab_view_pager);
        tabLayout = (TabLayout) rootView.findViewById(R.id.tabLayout);

        initTabLayout();
    }

    private void initTabLayout() {

        tabStockDetailsViewPagerAdapter = new StockDetailsTabFragmentPagerAdapter(getFragmentManager(),new WeakReference<Context>(getActivity()));
        viewPager.setAdapter(tabStockDetailsViewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }


}
