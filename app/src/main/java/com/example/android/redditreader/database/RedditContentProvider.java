package com.example.android.redditreader.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 *   Content provider
 */

public class RedditContentProvider extends ContentProvider
{
    /* Matcher */

    private final UriMatcher matcher;

    /* Matching codes */

    private final static int SUBREDDIT = 1;

    /* Database helper */

    private RedditDatabaseHelper helper;

    /**
     *    Returns a new RedditContentProvider
     */

    public RedditContentProvider()
    {
        matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(RedditContract.CONTENT_AUTHORITY, RedditContract.PATH_SUBREDDIT, SUBREDDIT);
    }

    /**
     *    Returns a cursor for the specified query
     *
     *    @param uri
     *    @param projection
     *    @param selection
     *    @param selectionArgs
     *    @param sortOrder
     *    @return
     */

    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder)
    {
        SQLiteDatabase database = helper.getWritableDatabase();

        Cursor cursor = null;
        switch (matcher.match(uri))
        {
           case SUBREDDIT:
              cursor = database.query(RedditContract.SubredditEntry.TABLE_NAME, projection,
                                      selection, selectionArgs, sortOrder, null, null);
           break;

           default:
        }

        return cursor;
    }

    /**
     *    Inserts values into content provider
     *
     *    @param uri
     *    @param values
     *    @return
     */

    public Uri insert(Uri uri, ContentValues values)
    {
        SQLiteDatabase database = helper.getWritableDatabase();

        switch (matcher.match(uri))
        {
            case SUBREDDIT:
                database.insert(RedditContract.SubredditEntry.TABLE_NAME, null, values);
                break;
        }

        database.close();

        return uri;
    }

    /**
     *    Updates the content provider
     *
     *    @param uri
     *    @param values
     *    @param selection
     *    @param selectionArgs
     *    @return
     */

    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs)
    {
        return 0;
    }

    /**
     *    Deletes entries from the content provider
     *
     *    @param uri
     *    @param selection
     *    @param selectionArgs
     *    @return
     */

    public int delete(Uri uri, String selection, String[] selectionArgs)
    {
        SQLiteDatabase database = helper.getWritableDatabase();

        int d = 0;
        switch (matcher.match(uri))
        {
            case SUBREDDIT:
                d = database.delete(RedditContract.SubredditEntry.TABLE_NAME, selection,
                                    selectionArgs);
            break;
        }

        database.close();
        getContext().getContentResolver().notifyChange(uri, null);

        return d;
    }

    /**
     *    Returns the MIME type of the specified data
     *
     *    @param uri
     *    @return
     */

    public String getType(Uri uri)
    {
        return null;
    }

    /**
     *    Called to initialize content provider
     */

    public boolean onCreate()
    {
        helper = new RedditDatabaseHelper(getContext());
        SQLiteDatabase database = helper.getWritableDatabase();
        database.close();

        return true;
    }
}
