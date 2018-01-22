package com.example.android.redditreader.submission;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.android.redditreader.R;

import java.util.List;

public class CommentAdapter extends ArrayAdapter<String>
{
    public CommentAdapter(Context context, List<String> comments)
    {
        super(context, 0, comments);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent)
    {
        if (view == null)
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_item_comment,
                    parent, false);

        TextView comment = (TextView)view.findViewById(R.id.comment);

        comment.setText(getItem(position));

        return view;
    }
}
