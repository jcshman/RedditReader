package com.example.android.redditreader.subscription;

import android.util.Log;

import com.example.android.redditreader.RequestTask;
import com.github.jreddit.parser.entity.Subreddit;
import com.github.jreddit.parser.exception.RedditParseException;
import com.github.jreddit.parser.listing.SubredditsListingParser;
import com.github.jreddit.request.RedditGetRequest;
import com.github.jreddit.request.retrieval.subreddits.SubredditsSearchRequest;

import java.util.ArrayList;
import java.util.List;

/**
 *   Subreddits request
 */

public class SubredditsRequest extends RequestTask
{
    /* Subreddit name */

    private String name;

    /* Subreddits */

    private List<Subreddit> subreddits = new ArrayList<>();

    /**
     *    Creates a new SubredditsRequest for the specified subreddit name
     *
     *    @param name subreddit name
     */

    public SubredditsRequest(String name)
    {
        this.name = name;
    }

    /**
     *    Requests subreddits for a title and parses
     */

    @Override
    protected Void doInBackground(Void... v)
    {
       super.doInBackground(v);

       try
       {
           SubredditsListingParser parser = new SubredditsListingParser();
           RedditGetRequest request = new SubredditsSearchRequest(name).setLimit(50);
           subreddits = parser.parse(getClient().get(getToken(), request));
       }
       catch(RedditParseException e)
       {
           Log.w(getClass().getName(), "Problem parsing subreddit listing for " + name);
       }

        return null;
    }

    /**
     *   Returns the subreddits
     *
     *   @return subreddits
     */

    public List<Subreddit> getSubreddits()
    {
        return subreddits;
    }
}
