package com.example.android.redditreader;

import android.os.AsyncTask;
import android.util.Log;

import com.github.jreddit.oauth.RedditOAuthAgent;
import com.github.jreddit.oauth.RedditToken;
import com.github.jreddit.oauth.app.RedditApp;
import com.github.jreddit.oauth.app.RedditInstalledApp;
import com.github.jreddit.oauth.client.RedditClient;
import com.github.jreddit.oauth.client.RedditHttpClient;
import com.github.jreddit.oauth.exception.RedditOAuthException;

import org.apache.http.impl.client.HttpClientBuilder;

import static com.example.android.redditreader.MainActivity.REDDIT_CLIENT_ID;

/**
 *   Reddit request task
 */

public class RequestTask extends AsyncTask<Void, Integer, Void>
{
    /* Agent */

    private RedditClient client;

    /* Token */

    private RedditToken token;

    @Override
    protected Void doInBackground(Void... v)
    {
        String userAgent = "jReddit: Reddit API Wrapper for Java";
        String redirectURI = "https://github.com/snkas/jReddit";

        RedditApp redditApp = new RedditInstalledApp(REDDIT_CLIENT_ID, redirectURI);
        RedditOAuthAgent agent = new RedditOAuthAgent(userAgent, redditApp);
        client = new RedditHttpClient(userAgent, HttpClientBuilder.create().build());

        try
        {
            token = agent.tokenAppOnly(false);
        }
        catch(RedditOAuthException e)
        {
            Log.e(getClass().getName(), "Unable to authorize client");
        }

        return null;
    }

    /**
     *   Returns the client
     *
     *   @return reddit client
     */

    public RedditClient getClient()
    {
        return client;
    }

    /**
     *   Returns the token
     *
     *   @return token
     */

    public RedditToken getToken()
    {
        return token;
    }
}
