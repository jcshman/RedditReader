package com.example.android.redditreader.submission;

import android.util.Log;

import com.example.android.redditreader.RequestTask;
import com.github.jreddit.parser.entity.Submission;
import com.github.jreddit.parser.entity.imaginary.FullSubmission;
import com.github.jreddit.parser.exception.RedditParseException;
import com.github.jreddit.parser.single.FullSubmissionParser;
import com.github.jreddit.request.retrieval.mixed.FullSubmissionRequest;

/**
 *   Submission request
 */

public class SubmissionRequest extends RequestTask
{
    /* Submission */

    private Submission submission;

    /* Full submission */

    private FullSubmission fullSubmission;

    /**
     *    Creates a new SubmissionRequest
     *
     *    @param submission submission
     */

    public SubmissionRequest(Submission submission)
    {
        this.submission = submission;
    }

    /**
     *    Requests full submission and parses
     */

    @Override
    protected Void doInBackground(Void... v)
    {
        super.doInBackground(v);

        try
        {
            FullSubmissionParser parser = new FullSubmissionParser();
            FullSubmissionRequest request = new FullSubmissionRequest(submission).setDepth(1);
            fullSubmission = parser.parse(getClient().get(getToken(), request));
        }
        catch(RedditParseException e)
        {
            Log.w(getClass().getName(), "Problem parsing full submission for "
                                      + submission.getTitle());
        }

        return null;
    }

    /**
     *   Returns the full submission
     *
     *   @return submission
     */

    public FullSubmission getSubmission()
    {
        return fullSubmission;
    }
}
