package com.example.android.redditreader.submission;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.android.redditreader.R;
import com.github.jreddit.parser.entity.Comment;
import com.github.jreddit.parser.entity.Submission;
import com.github.jreddit.parser.entity.imaginary.CommentTreeElement;
import com.github.jreddit.parser.entity.imaginary.FullSubmission;

import java.util.ArrayList;
import java.util.List;

public class SubmissionActivity extends AppCompatActivity
{
    public static final String SUBREDDIT_NAME = "com.example.android.redditreader.SUBREDDIT_NAME";
    public static final String SUBMISSION_ID = "com.example.android.redditreader.SUBMISSION_ID";

    /* Subreddit */

    private String subreddit;

    /* Submissions */

    private List<Submission> submissions = new ArrayList<>();

    /* Current submission index */

    private int index;

    /* Finishing flag */

    private boolean finishing = false;

    /**
     *   Called on activity create
     *
     *   @param savedState previous state
     */

    @Override
    protected void onCreate(Bundle savedState)
    {
        super.onCreate(savedState);
        setContentView(R.layout.activity_submission);

        subreddit = getIntent().getStringExtra(SUBREDDIT_NAME);
        ListView listView = (ListView)findViewById(R.id.submission_list);

        Drawable icon = null;
        if (listView != null)
            icon = getDrawable(R.drawable.ic_refresh_black_24dp);
        else
            icon = getDrawable(R.drawable.ic_clear_black_24dp);

        icon.setTint(Color.WHITE);

        Toolbar toolbar = (Toolbar)findViewById(R.id.submission_toolbar);
        toolbar.setOverflowIcon(icon);
        toolbar.setTitle(getString(R.string.subreddit_prefix) + subreddit);

        setSupportActionBar(toolbar);

        if (listView != null)
            listView.setOnItemClickListener(createListener());

        setupListingView(getIntent().getStringExtra(SUBMISSION_ID));
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        finishing = true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();

        ListView listView = (ListView)findViewById(R.id.submission_list);
        if (listView != null)
            inflater.inflate(R.menu.menu_refresh_icon, menu);
        else
            inflater.inflate(R.menu.menu_dismiss_icon, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == R.id.refresh)
           setupListingView(null);
        if (item.getItemId() == R.id.dismiss)
        {
            if (!submissions.isEmpty())
            {
                index++;
                if (index >= submissions.size())
                    index = 0;
                setupFragment(submissions.get(index));
            }
        }

        return true;
    }

    private AdapterView.OnItemClickListener createListener()
    {
        return new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                setupFragment(submissions.get(position));
            }
        };
    }

    /**
     *    Requests a new submission listing for this subreddit, then sets detail view to
     *    a specified submission
     *
     *    @param submissionId submission id to show in detail, null if submission is to be
     *                        selected from subreddit listing instead
     */

    private void setupListingView(final String submissionId)
    {
        new SubmissionListingRequest(subreddit)
        {
            @Override
            protected void onPostExecute(Void v)
            {
                List<Submission> sub = getSubmissions();
                if (sub != null)
                {
                    submissions.clear();
                    submissions.addAll(sub);

                    ArrayAdapter<Submission> adapter = new SubmissionAdapter(getApplicationContext(),
                                                                             submissions);

                    ListView submissionList = (ListView)findViewById(R.id.submission_list);
                    if (submissionList != null)
                        submissionList.setAdapter(adapter);

                    if (submissionId == null)
                    {
                        if (!submissions.isEmpty())
                            setupFragment(submissions.get(0));
                    }
                    else
                    {
                        Submission found = null;
                        for (Submission s : submissions)
                            if (s.getTitle().equals(submissionId))
                                found = s;
                        setupFragment(found);
                    }
                }
                else
                    Log.w(getClass().getName(), "Server did not return submissions for " + subreddit);
            }
        }.execute();
    }

    /**
     *   Sets up fragment from a listing submission
     *
     *   @param submission listing submission
     */

    private void setupFragment(final Submission submission)
    {
        new SubmissionRequest(submission)
        {
            @Override
            protected void onPostExecute(Void v)
            {
                FullSubmission full = getSubmission();
                if (full != null)
                    setupFragment(full);
                else
                    Log.w(getClass().getName(), "Server did not return full submission for "
                                              + submission);
            }
        }.execute();
    }

    /**
     *   Sets up fragment from a full submission
     *
     *   @param fullSubmission full submission
     */

    private void setupFragment(FullSubmission fullSubmission)
    {
        Submission submission = fullSubmission.getSubmission();

        Bundle bundle = new Bundle();

        bundle.putString("title", submission.getTitle());
        bundle.putString("author", submission.getAuthor());
        bundle.putString("content", submission.getURL());

        ArrayList<CharSequence> commentStrings = new ArrayList<>();
        for (CommentTreeElement c : fullSubmission.getCommentTree())
            if (c instanceof Comment)
                commentStrings.add(((Comment)c).getBody());

        bundle.putCharSequenceArrayList("comments", commentStrings);

        SubmissionFragment fragment = new SubmissionFragment();
        fragment.setArguments(bundle);

        if (!finishing)
            getFragmentManager().beginTransaction()
                                .replace(R.id.submission_container, fragment)
                                .commitAllowingStateLoss();
    }
}
