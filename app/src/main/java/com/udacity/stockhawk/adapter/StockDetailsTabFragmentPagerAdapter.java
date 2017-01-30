package com.udacity.stockhawk.adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.v13.app.FragmentPagerAdapter;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.fragment.StockChartDetailsFragment;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by David on 1/27/17.
 */
public class StockDetailsTabFragmentPagerAdapter extends FragmentPagerAdapter {

    private static final int TAB_COUNT = 2;
    private List<StockDetailsTabItem> tabItems;

    public StockDetailsTabFragmentPagerAdapter(FragmentManager fragmentManager, WeakReference<Context> weakReference) {
        super(fragmentManager);
        Context context = weakReference.get();
        tabItems = new ArrayList<>();
        tabItems.add(new StockDetailsTabItem(context.getString(R.string.details_chart), StockChartDetailsFragment.newInstance()));
        tabItems.add(new StockDetailsTabItem(context.getString(R.string.details_chart), StockChartDetailsFragment.newInstance()));
    }

    @Override
    public android.app.Fragment getItem(int position) {
        return tabItems.get(position).getFragment();
    }

    @Override
    public int getCount() {
        return TAB_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabItems.get(position).getTitle();
    }

    private class StockDetailsTabItem {
        private String title;
        private Fragment fragment;

        public StockDetailsTabItem(String title, Fragment fragment) {
            this.title = title;
            this.fragment = fragment;
        }

        public String getTitle() {
            return title;
        }

        public Fragment getFragment() {
            return fragment;
        }
    }
}
