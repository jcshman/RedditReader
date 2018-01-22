package com.example.android.redditreader.widget;

import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.android.redditreader.R;

/**
 *   Reddit widget intent service
 */

public class RedditWidgetIntentService extends IntentService
{
    public RedditWidgetIntentService()
    {
        super("RedditWidgetIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        Context context = getBaseContext();
        AppWidgetManager widgetManager = AppWidgetManager.getInstance(this);
        int[] widgetIds = widgetManager.getAppWidgetIds(
                          new ComponentName(this, RedditWidgetProvider.class));

        for (int id : widgetIds)
        {
            RemoteViews remote = new RemoteViews(context.getPackageName(), R.layout.widget);
            Intent serviceIntent = new Intent(context, RedditWidgetService.class);
            remote.setRemoteAdapter(id, R.id.widget_list, serviceIntent);

            Intent viewIntent = new Intent(context, RedditWidgetProvider.class);
            viewIntent.setAction(RedditWidgetProvider.VIEW_SUBMISSION);

            PendingIntent viewPendingIntent = PendingIntent.getBroadcast(context, 0, viewIntent,
                                              PendingIntent.FLAG_UPDATE_CURRENT);
            remote.setPendingIntentTemplate(R.id.widget_list, viewPendingIntent);

            widgetManager.updateAppWidget(id, remote);
            widgetManager.notifyAppWidgetViewDataChanged(id, R.id.widget_list);
        }
    }
}