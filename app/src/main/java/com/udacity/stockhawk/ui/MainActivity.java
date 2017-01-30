package com.udacity.stockhawk.ui;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.udacity.stockhawk.R;
import com.udacity.stockhawk.activity.StockDetailsLandingActivity;
import com.udacity.stockhawk.data.PreferencesUtils;
import com.udacity.stockhawk.data.QuoteContract;
import com.udacity.stockhawk.fragment.StockChartDetailsFragment;
import com.udacity.stockhawk.sync.QuoteSyncJob;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;
import util.StringUtils;
import util.SymbolLookup;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>,
        SwipeRefreshLayout.OnRefreshListener,
        StockAdapter.StockAdapterOnClickHandler,
        CallbackWeakReference {

    private static final int STOCK_LOADER = 0;
    private View rootView;

    @BindView(R.id.app_bar_layout)
    AppBarLayout appBarLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recycler_view)
    RecyclerView stockRecyclerView;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;
        @BindView(R.id.error)
        TextView error;
    private StockAdapter adapter;
    private String stockSymbol;

    @Override
    public void onClick(String symbol) {
        Timber.d("Symbol clicked: %s", symbol);
        Intent intent = new Intent(this, StockDetailsLandingActivity.class);
        intent.putExtra(StockChartDetailsFragment.STOCK_SYMBOL, symbol);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rootView = findViewById(R.id.coordinator);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        adapter = new StockAdapter(this, this);
        stockRecyclerView.setAdapter(adapter);
        stockRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setRefreshing(true);
        onRefresh();

        QuoteSyncJob.initialize(this);
        getSupportLoaderManager().initLoader(STOCK_LOADER, null, this);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                String symbol = adapter.getSymbolAtPosition(viewHolder.getAdapterPosition());
                PreferencesUtils.removeStock(MainActivity.this, symbol);
                getContentResolver().delete(QuoteContract.Quote.makeUriForStock(symbol), null, null);
            }
        }).attachToRecyclerView(stockRecyclerView);


    }

    private boolean networkUp() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    @Override
    public void onRefresh() {

        QuoteSyncJob.syncImmediately(this);

        if (!networkUp() && adapter.getItemCount() == 0) {
            swipeRefreshLayout.setRefreshing(false);
//            error.setText(getString(R.string.error_no_network));
//            error.setVisibility(View.VISIBLE);

        } else if (!networkUp()) {
            swipeRefreshLayout.setRefreshing(false);
            Toast.makeText(this, R.string.toast_no_connectivity, Toast.LENGTH_LONG).show();
        } else if (PreferencesUtils.getStocks(this).size() == 0) {
            Timber.d("WHYAREWEHERE");
            swipeRefreshLayout.setRefreshing(false);
//            error.setText(getString(R.string.error_no_stocks));
//            error.setVisibility(View.VISIBLE);
        } else {
//            error.setVisibility(View.GONE);
        }
    }

    public void addStockButton(View view) {
//        new AddStockDialog().show(getFragmentManager(), "StockDialogFragment");
        new MaterialDialog.Builder(this)
                .title(R.string.dialog_title)
                .customView(R.layout.add_stock_dialog, true)
                .positiveText(R.string.dialog_add)
                .negativeText(R.string.dialog_cancel)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        EditText symbol = (EditText) dialog.getView().findViewById(R.id.dialog_stock);
                        stockSymbol = symbol.getText().toString().trim();
                        swipeRefreshLayout.setRefreshing(true);
                        lookupStock();
                    }
                })
                .show();
    }


    void addStock(String symbol) {
        if (StringUtils.isNotEmpty(symbol)) {

            if (networkUp()) {
                swipeRefreshLayout.setRefreshing(true);
            } else {
                String message = getString(R.string.toast_stock_added_no_connectivity, symbol);
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
            PreferencesUtils.addStock(this, symbol);
            QuoteSyncJob.syncImmediately(this);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this,
                QuoteContract.Quote.URI,
                QuoteContract.Quote.QUOTE_COLUMNS,
                null, null, QuoteContract.Quote.COLUMN_SYMBOL);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        swipeRefreshLayout.setRefreshing(false);

        if (data.getCount() != 0) {
            //TODO David show Snackbaar for error message
//            error.setVisibility(View.GONE);
        }

        adapter.setCursor(data);
    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        swipeRefreshLayout.setRefreshing(false);
        adapter.setCursor(null);
    }


    private void setDisplayModeMenuItemIcon(MenuItem item) {
        if (PreferencesUtils.getDisplayMode(this)
                .equals(getString(R.string.pref_display_mode_absolute_key))) {
            item.setIcon(R.drawable.ic_percentage);
        } else {
            item.setIcon(R.drawable.ic_dollar);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_settings, menu);
        MenuItem item = menu.findItem(R.id.action_change_units);
        setDisplayModeMenuItemIcon(item);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_change_units) {
            PreferencesUtils.toggleDisplayMode(this);
            setDisplayModeMenuItemIcon(item);
            adapter.notifyDataSetChanged();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void lookupStock() {
        WeakReference<CallbackWeakReference> callbackWeakReference = new WeakReference<CallbackWeakReference>(this);
        SymbolLookup symbolLookup = new SymbolLookup(callbackWeakReference);
        symbolLookup.execute(stockSymbol);
    }

    @Override
    public void symbolAvailable(Boolean isAvailable) {

        if (isAvailable) {
            addStock(stockSymbol);
        } else {
            swipeRefreshLayout.setRefreshing(false);
            String errorMessage = getString(R.string.symbol_not_available, stockSymbol);
            Snackbar.make(rootView, errorMessage, Snackbar.LENGTH_SHORT).show();
        }

    }
}
