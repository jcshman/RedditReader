package com.example.android.redditreader;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.android.redditreader.database.ContentHelper;
import com.example.android.redditreader.database.RedditDatabaseHelper;
import com.example.android.redditreader.subscription.SubscriptionFragment;
import com.example.android.redditreader.widget.RedditWidgetProvider;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class MainActivity extends AppCompatActivity
{
    public static final String REDDIT_CLIENT_ID = "";

    private Tracker tracker;

    /**
     *    Called when activity is created
     *
     *    @param savedState saved state
     */

    @Override
    protected void onCreate(Bundle savedState)
    {
        super.onCreate(savedState);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ContentHelper.setResolver(getContentResolver());

        if (RedditDatabaseHelper.isNewDatabase()) {
            ContentHelper.addSubscription(getString(R.string.subscribe_name_A),
                    getString(R.string.subscribe_title_A));
            ContentHelper.addSubscription(getString(R.string.subscribe_name_B),
                    getString(R.string.subscribe_title_B));
            ContentHelper.addSubscription(getString(R.string.subscribe_name_C),
                    getString(R.string.subscribe_title_C));
        }

        AnalyticsApplication application = (AnalyticsApplication)getApplication();
        tracker = application.getDefaultTracker();

        if (savedState == null) {
            Fragment fragment = new SubscriptionFragment();
            fragment.setArguments(getIntent().getExtras());

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.subscription_container, fragment)
                    .commit();
        }

        Context context = getApplicationContext();
        AppWidgetManager widgetManager = AppWidgetManager.getInstance(context);
        ComponentName name = new ComponentName(context, RedditWidgetProvider.class);
        int[] ids = widgetManager.getAppWidgetIds(name);
        widgetManager.notifyAppWidgetViewDataChanged(ids, R.id.widget_list);
    }

    @Override
    public void onResume()
    {
        super.onResume();

        tracker.send(new HitBuilders.ScreenViewBuilder().build());
    }
}

