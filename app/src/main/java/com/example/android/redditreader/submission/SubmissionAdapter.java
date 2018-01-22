package com.example.android.redditreader.submission;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.redditreader.R;
import com.github.jreddit.parser.entity.Submission;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SubmissionAdapter extends ArrayAdapter<Submission>
{
    public SubmissionAdapter(Context context, List<Submission> submissions)
    {
        super(context, 0, submissions);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent)
    {
        if (view == null)
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_item_submissions,
                    parent, false);

        TextView title = (TextView) view.findViewById(R.id.submissions_title);
        ImageView image = (ImageView) view.findViewById(R.id.submissions_image);

        Submission submission = getItem(position);

        title.setText(submission.getTitle());
        String thumbnail = submission.getThumbnail();
        if (!thumbnail.isEmpty())
            Picasso.with(getContext()).load(submission.getThumbnail()).into(image);

        return view;
    }
}
