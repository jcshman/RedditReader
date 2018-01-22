package com.example.android.redditreader.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

import com.example.android.redditreader.submission.SubmissionActivity;

public class RedditWidgetProvider extends AppWidgetProvider
{
    public static final String VIEW_SUBMISSION = "com.example.android.redditreader.VIEW_SUBMISSION";

    @Override
    public void onUpdate(Context context, AppWidgetManager widgetManager, int[] widgetIds)
    {
        Intent intent = new Intent(context, RedditWidgetIntentService.class);
        context.startService(intent);

        super.onUpdate(context, widgetManager, widgetIds);
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        if (intent.getAction().equals(VIEW_SUBMISSION))
        {
            String subredditName = intent.getStringExtra(SubmissionActivity.SUBREDDIT_NAME);
            String id = intent.getStringExtra(SubmissionActivity.SUBMISSION_ID);

            Intent launch = new Intent(context, SubmissionActivity.class);
            launch.putExtra(SubmissionActivity.SUBREDDIT_NAME, subredditName);
            launch.putExtra(SubmissionActivity.SUBMISSION_ID, id);
            launch.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            context.startActivity(launch);
        }

        super.onReceive(context, intent);
    }
}
