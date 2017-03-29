package com.udacity.stockhawk.widgets;

import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.ui.MainActivity;
import com.udacity.stockhawk.ui.StockHistoryActivity;

import static com.udacity.stockhawk.sync.QuoteSyncJob.ACTION_DATA_UPDATED;
import static com.udacity.stockhawk.ui.MainActivity.EXTRA_SYMBOL_KEY;

/**
 * Created by mohammed on 3/27/17.
 */

public class StockWidgetProvider extends AppWidgetProvider {
    public final static String ACTION_Activity_Launch = "launch-history-activity-action";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.app_widget_layout);
            views.setTextViewText(R.id.tv_title_widget, context.getString(R.string.stock_widget_title));

            // Create an Intent to launch MainActivity
            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            views.setOnClickPendingIntent(R.id.tv_title_widget, pendingIntent);

            // Set up the collection
            Intent serviceIntent = new Intent(context, StockWidgetService.class);
            serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            serviceIntent.setData(Uri.parse(serviceIntent
                    .toUri(Intent.URI_INTENT_SCHEME)));
            views.setRemoteAdapter(R.id.sv_widget, serviceIntent);

            Intent clickIntentTemplate = new Intent(context, StockHistoryActivity.class);
            PendingIntent clickPendingIntentTemplate = TaskStackBuilder.create(context)
                    .addNextIntentWithParentStack(clickIntentTemplate)
                    .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setPendingIntentTemplate(R.id.sv_widget, clickPendingIntentTemplate);
            views.setEmptyView(R.id.sv_widget, R.id.tv_empty_widget);

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // update widget when data changes
        if (ACTION_DATA_UPDATED.equals(intent.getAction())) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(
                    new ComponentName(context, getClass()));
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.sv_widget);
        } else if (intent.getAction().equals(ACTION_Activity_Launch)) {
            Intent stockHistoryActivity = new Intent(context, StockHistoryActivity.class);
            String symbol = intent.getStringExtra(EXTRA_SYMBOL_KEY);
            stockHistoryActivity.putExtra(EXTRA_SYMBOL_KEY, symbol);
            context.startActivity(stockHistoryActivity);
        }
        super.onReceive(context, intent);
    }


}
