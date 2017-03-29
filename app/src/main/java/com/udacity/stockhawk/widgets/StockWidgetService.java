package com.udacity.stockhawk.widgets;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import static com.udacity.stockhawk.ui.MainActivity.EXTRA_SYMBOL_KEY;
import static com.udacity.stockhawk.widgets.StockWidgetProvider.ACTION_Activity_Launch;

/**
 * Created by mohammed on 3/27/17.
 */

/**
 * This is the service that provides the factory to be bound to the collection service.
 */
public class StockWidgetService extends RemoteViewsService {
    public final String LOG_TAG = StockWidgetService.class.getSimpleName();

    @Override
    public RemoteViewsFactory onGetViewFactory(final Intent intent) {
        return new StockViewsFactory(this.getApplicationContext(), intent);
    }
}

class StockViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private Cursor data = null;
    private Context context;

    public StockViewsFactory(Context context, Intent intent) {
        this.context = context;
    }

    @Override
    public void onCreate() {
        // Nothing to do
    }

    @Override
    public void onDataSetChanged() {
        if (data != null) {
            data.close();
        }
        final long identityToken = Binder.clearCallingIdentity();
        data = this.context.getContentResolver().query(Contract.Quote.URI,
                Contract.Quote.QUOTE_COLUMNS.toArray(new String[]{}),
                null,
                null,
                null);
        Binder.restoreCallingIdentity(identityToken);
    }

    @Override
    public void onDestroy() {
        if (data != null) {
            data.close();
            data = null;
        }
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.getCount();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (position == AdapterView.INVALID_POSITION ||
                data == null || !data.moveToPosition(position)) {
            return null;
        }

        data.moveToPosition(position);
        RemoteViews views = new RemoteViews(this.context.getPackageName(),
                R.layout.app_widget_list_item);
        String symbol = data.getString(data.getColumnIndex(Contract.Quote.COLUMN_SYMBOL));
        DecimalFormat dollarFormat = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);
        dollarFormat.setPositivePrefix("$");
        String price = dollarFormat.format(data.getFloat(Contract.Quote.POSITION_PRICE));
        views.setTextViewText(R.id.tv_widget_symbol, symbol);
        views.setTextViewText(R.id.tv_widget_price, price);

        Intent fillInIntent = new Intent();
        fillInIntent.putExtra(EXTRA_SYMBOL_KEY, symbol);
        fillInIntent.setAction(ACTION_Activity_Launch);
        // put the extras here
        views.setOnClickFillInIntent(R.id.ll_widget_quote_item, fillInIntent);
        return views;
    }


    @Override
    public RemoteViews getLoadingView() {
        return new RemoteViews(this.context.getPackageName(), R.layout.app_widget_list_item);
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}