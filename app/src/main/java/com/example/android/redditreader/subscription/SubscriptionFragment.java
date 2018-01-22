package com.example.android.redditreader.subscription;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.android.redditreader.database.ContentHelper;
import com.example.android.redditreader.R;
import com.example.android.redditreader.database.RedditContract;
import com.example.android.redditreader.widget.RedditWidgetProvider;
import com.github.jreddit.parser.entity.Subreddit;

import java.util.List;

/**
 *    Subscription fragment
 */

public class SubscriptionFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>
{
    /* Subreddit adapter */

    private SubredditCursorAdapter adapter;

    /**
     *   Called when fragment is created
     *
     *   @param inflater layout inflater
     *   @param container container
     *   @param savedState saved state bundle
     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedState)
    {
        View rootView = inflater.inflate(R.layout.fragment_subscription, container, false);

        RecyclerView view = (RecyclerView)rootView.findViewById(R.id.recyclerview);
        adapter = new SubredditCursorAdapter();
        view.setAdapter(adapter);

        GridLayoutManager layoutManager = null;
        if (view.getTag().equals("id_phone"))
            layoutManager = new GridLayoutManager(getContext(), 2);
        else
            layoutManager = new GridLayoutManager(getContext(), 3);

        view.setLayoutManager(layoutManager);

        ItemTouchHelper helper = new ItemTouchHelper(createCallback());
        helper.attachToRecyclerView(view);

        FloatingActionButton fab = (FloatingActionButton)rootView.findViewById(R.id.fab);
        View.OnClickListener listener = new View.OnClickListener()
        {
            public void onClick(View view)
            {
                showDialog();
            }
        };
        fab.setOnClickListener(listener);

        getActivity().getSupportLoaderManager().initLoader(0, null, this);

        return rootView;
    }

    /**
     *   Refresh
     */

    public void refresh()
    {
        getActivity().getSupportLoaderManager().restartLoader(0, null, this);

        AppWidgetManager widgetManager = AppWidgetManager.getInstance(getContext());
        ComponentName name = new ComponentName(getContext(), RedditWidgetProvider.class);
        int[] ids = widgetManager.getAppWidgetIds(name);
        widgetManager.notifyAppWidgetViewDataChanged(ids, R.id.widget_list);
    }

    /**
     *   Called to create cursor loader
     *
     *   @param id loader id
     *   @param args arguments
     */

    public Loader<Cursor> onCreateLoader(int id, Bundle args)
    {
        String[] projection = {"id", RedditContract.SubredditEntry.COLUMN_NAME,
                                     RedditContract.SubredditEntry.COLUMN_TITLE};

        return new CursorLoader(getContext(),
                                RedditContract.SUBREDDIT_TABLE, projection, null, null, null);
    }

    /**
     *   Notifies that cursor is done loading
     *
     *   @param loader loader
     *   @param data cursor
     */

    public void onLoadFinished(Loader<Cursor> loader, Cursor data)
    {
        adapter.swapCursor(data);
        adapter.notifyDataSetChanged();
    }

    /**
     *   Called when loader is reset
     *
     *   @param loader loader
     */

    public void onLoaderReset(Loader<Cursor> loader)
    {
        adapter.swapCursor(null);
    }

    /**
     *   Creates a touch helper callback
     *
     *   @return callback
     */

    private ItemTouchHelper.Callback createCallback()
    {
        return new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                                                  ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT)
        {
            public boolean onMove(RecyclerView recyclerView,
                                  RecyclerView.ViewHolder viewHolder,
                                  RecyclerView.ViewHolder target)
            {
                return false;
            }

            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction)
            {
                String name = ((SubredditCursorAdapter.ViewHolder)viewHolder).name;
                ContentHelper.deleteSubscription(name);

                refresh();
            }
        };
    }

    /**
     *   Shows a subscribe dialog
     */

    private void showDialog()
    {
        final EditText input = new EditText(getContext());
        input.setSingleLine();

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                                                     .setView(input, 40, 0, 55, 0)
                                                     .setTitle(getString(R.string.subscribe_title))
                                                     .setMessage(
                                                      getString(R.string.subscribe_message));

        builder.setPositiveButton(getString(R.string.positive_label),
                                  new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                String name = input.getText().toString().trim();
                if (name.startsWith(getString(R.string.subreddit_prefix)))
                    name = name.substring(2);
                if (name.startsWith("/" + getString(R.string.subreddit_prefix)))
                    name = name.substring(3);

                if (!name.isEmpty())
                    subscribe(name);
                else
                    Log.i(getClass().getName(), "Entered blank subreddit name");
            }
        });
        builder.show();
    }

    /**
     *   Retrieves information on specified subreddit to validate and subscribe
     *
     *   @param name subreddit title
     */

    private void subscribe(final String name)
    {
        new SubredditsRequest(name)
        {
            @Override
            protected void onPostExecute(Void v)
            {
                List<Subreddit> subreddits = getSubreddits();
                if (!subreddits.isEmpty())
                {
                    Subreddit s = subreddits.get(0);
                    if ((s.getDisplayName().equals(name)) && (!s.isNSFW()))
                    {
                        List<String> subscribed = ContentHelper.getSubscriptionNames();
                        boolean found = false;
                        for (String sub : subscribed)
                            if (sub.equals(s.getDisplayName()))
                                found = true;

                        if (!found)
                        {
                            ContentHelper.addSubscription(s.getDisplayName(), s.getTitle());
                            refresh();

                            Context context = getContext();
                            AppWidgetManager widgetManager = AppWidgetManager.getInstance(context);
                            ComponentName name = new ComponentName(context,
                                                                   RedditWidgetProvider.class);

                            int[] ids = widgetManager.getAppWidgetIds(name);
                            widgetManager.notifyAppWidgetViewDataChanged(ids, R.id.widget_list);
                        }
                        else
                            Log.i(getClass().getName(), "Subreddit already subscribed to");
                    }
                    else
                        Log.i(getClass().getName(),
                              "Exact match for subreddit name not found on server");
                }
                else
                    Log.i(getClass().getName(), "No matches for subreddit name found on server");
            }
        }.execute();
    }
}

