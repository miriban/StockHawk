package com.udacity.stockhawk.ui;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;
import com.udacity.stockhawk.utils.StockHistoryUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StockHistoryActivity extends AppCompatActivity {

    private static final String TAG = StockHistoryActivity.class.getSimpleName();
    private String symbol;
    @BindView(R.id.chart)
    LineChart chart;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_history);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.history_toolbar);
        setSupportActionBar(toolbar);


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
            final float values[] = StockHistoryUtils.getStockValues(historyData);
            final String dates[] = StockHistoryUtils.getStockDates(historyData);
            // creating chart
            StockHistoryUtils.createChart(this, this.symbol, chart, values, dates);
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



}
