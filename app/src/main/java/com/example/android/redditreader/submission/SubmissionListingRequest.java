package com.example.android.redditreader.submission;

import android.util.Log;

import com.example.android.redditreader.RequestTask;
import com.github.jreddit.parser.entity.Submission;
import com.github.jreddit.parser.exception.RedditParseException;
import com.github.jreddit.parser.listing.SubmissionsListingParser;
import com.github.jreddit.request.RedditGetRequest;
import com.github.jreddit.request.retrieval.param.SubmissionSort;
import com.github.jreddit.request.retrieval.submissions.SubmissionsOfSubredditRequest;

import java.util.ArrayList;
import java.util.List;

/**
 *   Submission listing request
 */

public class SubmissionListingRequest extends RequestTask
{
    /* Subreddit */

    private String name;

    /* Submissions */

    private List<Submission> submissions = new ArrayList<>();

    /**
     *    Creates a new SubmissionListingRequest
     *
     *    @param name name of subreddit
     */

    public SubmissionListingRequest(String name)
    {
        this.name = name;
    }

    /**
     *    Requests submission listing and parses them
     */

    @Override
    protected Void doInBackground(Void... v)
    {
        super.doInBackground(v);
        try
        {
            SubmissionsListingParser parser = new SubmissionsListingParser();
            RedditGetRequest request = new SubmissionsOfSubredditRequest(name,
                                                                         SubmissionSort.NEW)
                                                                        .setLimit(100);
            submissions = parser.parse(getClient().get(getToken(), request));
        }
        catch(RedditParseException e)
        {
            Log.w(getClass().getName(), "Problem parsing submission listing for " + name);
        }

        return null;
    }

    /**
     *   Returns the submissions
     *
     *   @return submissions
     */

    public List<Submission> getSubmissions()
    {
        return submissions;
    }
}
