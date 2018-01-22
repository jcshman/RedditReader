package com.example.android.redditreader.subscription;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.redditreader.R;
import com.example.android.redditreader.submission.SubmissionActivity;

/**
 *   Subreddit cursor adapter
 */

public class SubredditCursorAdapter extends RecyclerView.Adapter<SubredditCursorAdapter.ViewHolder>
{
    /* View holder */

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        /* Tile */

        View tileView;

        /* Title view */

        TextView titleView;

        /* Description view */

        TextView descriptionView;

        /* Name */

        String name;

        /**
         *   Returns a new ViewHolder
         *
         *   @param view text view
         */

        public ViewHolder(View view)
        {
            super(view);
            this.tileView = view.findViewById(R.id.list_item_subreddit_tile);
            this.titleView = (TextView)view.findViewById(R.id.list_item_subreddit_textview);
            this.descriptionView = (TextView)view.findViewById(R.id.list_item_description_textview);
        }
    }

    /* Cursor */

    private Cursor cursor;

    /**
     *   Returns a new ViewHolder for a specified parent view group
     *
     *   @param parent parent view group
     *   @param viewType view type
     *   @return cursor adapter
     */

    @Override
    public SubredditCursorAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext())
                                           .inflate(R.layout.grid_item_subscription, parent, false);

        return new ViewHolder(v);
    }

    /**
     *   Binds view holder to value provided by cursor
     *
     *   @param holder view holder
     *   @param position position within view
     */

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        cursor.moveToPosition(position);

        final Context context = holder.titleView.getContext();
        final String subredditName = cursor.getString(1);

        holder.name = subredditName;
        holder.titleView.setText(context.getString(R.string.subreddit_prefix) + subredditName);
        holder.descriptionView.setText(cursor.getString(2));

        holder.tileView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(context, SubmissionActivity.class);
                intent.putExtra(SubmissionActivity.SUBREDDIT_NAME, subredditName);
                context.startActivity(intent);
            }
        });
    }

    /**
     *   Returns rows in cursor
     *
     *   @return item count
     */

    @Override
    public int getItemCount()
    {
        if (cursor != null)
            return cursor.getCount();
        else
            return 0;
    }

    /**
     *   Swaps old cursor for new cursor
     *
     *   @param cursor new cursor
     */

    public void swapCursor(Cursor cursor)
    {
        this.cursor = cursor;
    }
}
