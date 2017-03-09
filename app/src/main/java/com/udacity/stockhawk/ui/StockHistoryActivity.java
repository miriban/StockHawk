package com.udacity.stockhawk.ui;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;
import com.udacity.stockhawk.data.StockProvider;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StockHistoryActivity extends AppCompatActivity {

    private static final String TAG = StockHistoryActivity.class.getSimpleName();
    private String symbol;
    @BindView(R.id.rv_stock_history)
    RecyclerView recyclerView_stock_history;
    private StockHistoryAdapter historyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_history);
        ButterKnife.bind(this);

        if(getIntent().hasExtra(MainActivity.EXTRA_SYMBOL_KEY))
        {
            this.symbol = getIntent().getStringExtra(MainActivity.EXTRA_SYMBOL_KEY);
        }

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
        {
            actionBar.setTitle(this.symbol+" "+getString(R.string.stock_history));
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        Uri uri = Contract.Quote.makeUriForStock(this.symbol);
        ContentResolver resolver = getContentResolver();
        Cursor cursor = null;

        if(resolver != null)
        {
            cursor = resolver.query(uri,null,null,null,null);
        }

        if(cursor != null)
        {
            cursor.moveToFirst();
            String historyData = cursor.getString(Contract.Quote.POSITION_HISTORY);
            historyAdapter = new StockHistoryAdapter(historyData);
            populateData(historyAdapter);
            cursor.close();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId() == android.R.id.home)
        {
            finish();
            return true;
        }
        return false;
    }

    private void populateData(StockHistoryAdapter adapter)
    {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView_stock_history.setLayoutManager(linearLayoutManager);
        recyclerView_stock_history.setHasFixedSize(true);
        recyclerView_stock_history.setAdapter(adapter);
    }



}
