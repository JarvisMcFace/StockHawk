package com.udacity.stockhawk.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
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
import com.udacity.stockhawk.adapter.StockAdapter;
import com.udacity.stockhawk.data.PreferencesUtils;
import com.udacity.stockhawk.data.QuoteContract;
import com.udacity.stockhawk.fragment.StockDetailsLandingFragment;
import com.udacity.stockhawk.sync.QuoteSyncJob;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;
import util.CallbackWeakReference;
import util.StringUtils;
import util.SymbolLookup;
import util.widget.RefreshStockInformationWidget;

import static android.view.View.VISIBLE;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>,
        SwipeRefreshLayout.OnRefreshListener, StockAdapter.StockAdapterOnClickHandler,
        CallbackWeakReference, StockRemoedCalledBack {

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
    @BindView(R.id.stocks_empty_state)
    View emptyStateView;
    private StockAdapter adapter;
    private String stockSymbol;
    private Paint paint = new Paint();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rootView = findViewById(R.id.coordinator);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);

        toolbar.setTitle("Stock Hawk");

        adapter = new StockAdapter(this, this);
        stockRecyclerView.setAdapter(adapter);
        stockRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setRefreshing(true);
        onRefresh();

        QuoteSyncJob.initialize(this);
        getSupportLoaderManager().initLoader(STOCK_LOADER, null, this);

        initStockSwipeDelete();
    }


    private boolean networkUp() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    @Override
    public void onRefresh() {

        if (!networkUp()) {
            showNoNetworkSnackBar();
            return;
        }

        if (adapter.getItemCount() == 0 || PreferencesUtils.getStocks(this).size() == 0) {
            deriveEmptyStateView();
            return;
        }

        QuoteSyncJob.syncImmediately(this);

//        if (!networkUp() && adapter.getItemCount() == 0) {
//            swipeRefreshLayout.setRefreshing(false);
////            error.setText(getString(R.string.error_no_network));
////            error.setVisibility(View.VISIBLE);
//            //Snackbar message of last udpate date //TODO
//
//            deriveEmptyStateView();
//        } else if (!networkUp()) {
//            swipeRefreshLayout.setRefreshing(false);
//            showNoNetworkSnackBar();
//        } else if (PreferencesUtils.getStocks(this).size() == 0) {
//            Timber.d("All Stocks have been removed, add default list");
//            //TODO Only allow to run once
//
//        } else {
//            emptyStateView.setVisibility(View.GONE);
//            error.setVisibility(View.GONE);
//        }
    }

    public void addStockButton(View view) {
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

    @Override
    public void deriveEmptyStateView() {
        if (adapter.getItemCount() == 0) {
            swipeRefreshLayout.setRefreshing(false);
            emptyStateView.setVisibility(VISIBLE);
        } else {
            swipeRefreshLayout.setRefreshing(true);
            emptyStateView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(String symbol) {
        Timber.d("Symbol clicked: %s", symbol);
        Intent intent = new Intent(this, StockDetailsLandingActivity.class);
        intent.putExtra(StockDetailsLandingFragment.STOCK_SYMBOL, symbol);
        startActivity(intent);
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

        if (data.getCount() > 0) {
            error.setVisibility(View.GONE);
            emptyStateView.setVisibility(View.GONE);
        }

        adapter.setCursor(data);
        RefreshStockInformationWidget.execute(getApplication());
    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        swipeRefreshLayout.setRefreshing(false);
        adapter.setCursor(null);
    }


    public void addStock(String symbol) {

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

    private void initStockSwipeDelete() {

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

                if (direction == ItemTouchHelper.LEFT) {
                    String symbol = adapter.getSymbolAtPosition(viewHolder.getAdapterPosition());
                    PreferencesUtils.removeStock(MainActivity.this, symbol);
                    getContentResolver().delete(QuoteContract.Quote.makeUriForStock(symbol), null, null);
                    RefreshStockInformationWidget.execute(getApplication());
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                Bitmap icon;
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    if (dX < 0) {
                        paint.setColor(Color.parseColor("#D32F2F"));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background, paint);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_delete_white);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2 * width, (float) itemView.getTop() + width, (float) itemView.getRight() - width, (float) itemView.getBottom() - width);
                        c.drawBitmap(icon, null, icon_dest, paint);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }

            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);
                deriveEmptyStateView();
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(stockRecyclerView);
    }


    private void showNoNetworkSnackBar() {

        int accentColor = ContextCompat.getColor(this, R.color.colorAccent);
        Snackbar.make(rootView, getString(R.string.no_connectivity), Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(R.string.retry), getNetorkRetryOnClickListener(this))
                .setActionTextColor(accentColor)
                .show();
    }

    private View.OnClickListener getNetorkRetryOnClickListener(final Activity activity) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (networkUp()) {
                    QuoteSyncJob.syncImmediately(activity);
                    Snackbar.make(rootView, getString(R.string.network_restored), Snackbar.LENGTH_SHORT).show();
                } else {
                    showNoNetworkSnackBar();
                }
            }
        };
    }
}
