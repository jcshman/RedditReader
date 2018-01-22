package com.example.android.redditreader.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class RedditWidgetService extends RemoteViewsService
{
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent)
    {
        return new RedditRemoteViewsFactory(this.getApplicationContext());
    }
}
