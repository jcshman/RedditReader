package com.example.android.redditreader.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.redditreader.database.RedditContract;

/**
 *    Reddit database open helper
 */

public class RedditDatabaseHelper extends SQLiteOpenHelper
{
   /* Database name */

   static final String DATABASE_NAME = "reddit.db";

   /* Newly created flag */

   private static boolean created = false;

   /**
    *    Returns a database helper
    *
    *    @param context context
    */

   public RedditDatabaseHelper(Context context)
   {
      super(context, DATABASE_NAME, null, 1);
   }

   /**
    *    Called to initialize database
    *
    *    @param database database
    */

   public void onCreate(SQLiteDatabase database)
   {
      final String CREATE_SUBREDDIT_TABLE = "CREATE TABLE " +
                                            RedditContract.SubredditEntry.TABLE_NAME +
                                            "(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                            RedditContract.SubredditEntry.COLUMN_NAME +
                                            " STRING NOT NULL UNIQUE, " +
                                            RedditContract.SubredditEntry.COLUMN_TITLE + ");";

      database.execSQL(CREATE_SUBREDDIT_TABLE);
      created = true;
   }

   /**
    *   Indicates that the database is newly created
    *
    *   @return true if newly created, false otherwise
    */

   public static boolean isNewDatabase()
   {
       return created;
   }

   /**
    *   Sets the new database status to false
    */

   public static void setCreated()
   {
      created = false;
   }

   /**
    *    Called to upgrade database
    *
    *    @param database database
    *    @param oldVersion old version
    *    @param newVersion new version
    */

   public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion)
   {}
}
