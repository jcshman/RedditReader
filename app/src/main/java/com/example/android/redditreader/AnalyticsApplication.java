package com.example.android.redditreader;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

/**
 *    Analytics
 */

public class AnalyticsApplication extends Application
{
    private static GoogleAnalytics analytics;
    private static Tracker tracker;

    @Override
    public void onCreate()
    {
        super.onCreate();

        analytics = GoogleAnalytics.getInstance(this);
    }

    synchronized public Tracker getDefaultTracker()
    {
        if (tracker == null)
           tracker = analytics.newTracker(R.xml.global_tracker);

        return tracker;
    }
}
