package com.example.android.redditreader.database;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 *   Reddit contract
 */

public class RedditContract
{
    /* Content authority */

    public static final String CONTENT_AUTHORITY = "com.example.android.redditreader";

    /* Base content */

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /* Base path */

    public static final String PATH_SUBREDDIT = "subreddit";

    /* Full path */

    public static final Uri SUBREDDIT_TABLE = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_SUBREDDIT);

    /* Subreddits table */

    public static final class SubredditEntry implements BaseColumns
    {
        /* Table */

        public static final String TABLE_NAME = "subreddit";

        /* Name */

        public static final String COLUMN_NAME = "name";

        /* Title */

        public static final String COLUMN_TITLE = "title";
    }
}
