package com.example.android.redditreader.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.webkit.URLUtil;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.redditreader.database.ContentHelper;
import com.example.android.redditreader.R;
import com.example.android.redditreader.submission.SubmissionActivity;
import com.example.android.redditreader.submission.SubmissionListingRequest;
import com.github.jreddit.parser.entity.Submission;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RedditRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory
{
    /* Context */

    private final Context context;

    /* Submissions */

    private List<Submission> submissions;

    public RedditRemoteViewsFactory(Context context)
    {
        this.context = context;
        submissions = new ArrayList<>();
    }

    @Override
    public RemoteViews getViewAt(int position)
    {
        RemoteViews remoteItemView = new RemoteViews(context.getPackageName(),
                                                     R.layout.widget_item);
        Submission s = submissions.get(position);
        remoteItemView.setTextViewText(R.id.widget_text, s.getTitle());

        Bundle parameters = new Bundle();

        parameters.putString(SubmissionActivity.SUBREDDIT_NAME, s.getSubreddit());
        parameters.putString(SubmissionActivity.SUBMISSION_ID, s.getTitle());

        Intent fillIntent = new Intent();
        fillIntent.putExtras(parameters);
        remoteItemView.setOnClickFillInIntent(R.id.widget_item, fillIntent);

        String thumbnail = s.getThumbnail();
        try
        {
            Bitmap bitmap = null;
            if ((!thumbnail.isEmpty()) && (URLUtil.isValidUrl(thumbnail)))
            {
                bitmap = Picasso.with(context).load(thumbnail).get();
                remoteItemView.setImageViewBitmap(R.id.widget_image, bitmap);
            }
        }
        catch(IOException e)
        {
            Log.w(getClass().getName(), "Problem with loading thumbnail at " + thumbnail);
        }

        return remoteItemView;
    }

    @Override
    public void onDataSetChanged()
    {
        submissions.clear();
        for (String name : ContentHelper.getSubscriptionNames())
        {
            try
            {
                SubmissionListingRequest r = new SubmissionListingRequest(name);
                r.execute().get();

                Submission s = r.getSubmissions().get(0);
                submissions.add(s);
            }
            catch(Exception e)
            {
                Log.w(getClass().getName(), "Problem executing listing request synchronously");
            }
        }
    }

    @Override
    public int getCount()
    {
        return submissions.size();
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }

    @Override
    public RemoteViews getLoadingView()
    {
        return null;
    }

    @Override
    public int getViewTypeCount()
    {
        return 1;
    }

    @Override
    public boolean hasStableIds()
    {
        return true;
    }

    @Override
    public void onCreate(){}

    @Override
    public void onDestroy(){}
}
