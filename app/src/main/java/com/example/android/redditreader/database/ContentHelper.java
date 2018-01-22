package com.example.android.redditreader.database;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

/**
 *   Content helper
 */

public class ContentHelper
{
    /* Content resolver */

    private static ContentResolver resolver;

    public static void setResolver(ContentResolver r)
    {
        resolver = r;
    }

    public static boolean isNewDatabase()
    {
        getSubscriptionNames();
        return RedditDatabaseHelper.isNewDatabase();
    }

    public static List<String> getSubscriptionNames()
    {
        if (resolver == null)
            return new ArrayList<>();

        List<String> names = new ArrayList<>();

        String[] columns = {RedditContract.SubredditEntry.COLUMN_NAME};
        Cursor cursor = resolver.query(RedditContract.SUBREDDIT_TABLE, columns, null, null, null);

        for (int i = 0; i < cursor.getCount(); i++)
        {
            cursor.moveToPosition(i);
            names.add(cursor.getString(0));
        }
        cursor.close();

        return names;
    }

    public static void addSubscription(String name, String title)
    {
        ContentValues values = new ContentValues();

        values.put(RedditContract.SubredditEntry.COLUMN_NAME, name);
        values.put(RedditContract.SubredditEntry.COLUMN_TITLE, title);

        if (resolver != null)
        {
            resolver.insert(RedditContract.SUBREDDIT_TABLE, values);
            RedditDatabaseHelper.setCreated();
        }
    }

    public static void deleteSubscription(String name)
    {
        if (resolver != null)
        {
            String[] args = {name};
            resolver.delete(RedditContract.SUBREDDIT_TABLE,
                        RedditContract.SubredditEntry.COLUMN_NAME + "=?", args);
            RedditDatabaseHelper.setCreated();
        }
    }
}
