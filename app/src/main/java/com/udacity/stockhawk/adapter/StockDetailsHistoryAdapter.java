package com.udacity.stockhawk.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.databinding.ListDetailItemBinding;

import java.util.List;

import yahoofinance.histquotes.HistoricalQuote;

/**
 * Created by David on 2/5/17.
 */
public class StockDetailsHistoryAdapter extends RecyclerView.Adapter<StockDetailsHistoryAdapter.HistoryDetailsBindingHolder> {

    private List<HistoricalQuote> historicalQuotes;

    public StockDetailsHistoryAdapter(List<HistoricalQuote> historicalQuotes) {
        this.historicalQuotes = historicalQuotes;
    }

    @Override
    public HistoryDetailsBindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_detail_item, parent, false);
        HistoryDetailsBindingHolder holder = new HistoryDetailsBindingHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(HistoryDetailsBindingHolder holder, int position) {
        final HistoricalQuote historicalQuote = historicalQuotes.get(position);

        ListDetailItemBinding dataBinding = holder.getListDetailItemBinding();

        dataBinding.setStockHistory(historicalQuote);
        dataBinding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return historicalQuotes.size();
    }

    public static class HistoryDetailsBindingHolder extends RecyclerView.ViewHolder {

        private ListDetailItemBinding listDetailItemBinding;

        public HistoryDetailsBindingHolder(View rowView) {
            super(rowView);
            listDetailItemBinding = DataBindingUtil.bind(rowView);
        }

        public ListDetailItemBinding getListDetailItemBinding() {
            return listDetailItemBinding;
        }
    }
}
