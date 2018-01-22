package com.example.android.redditreader.submission;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.redditreader.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.List;

public class SubmissionFragment extends Fragment
{
    /* Title */

    private String title;

    /* Author */

    private String author;

    /* Content */

    private String content;

    /* Comments */

    private List<String> comments = new ArrayList<>();

    /**
     *   Called to create fragment view
     *
     *   @param inflater layout inflater
     *   @param container container
     *   @param savedState saved state
     *   @return view
     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedState)
    {
        View root = inflater.inflate(R.layout.fragment_submission, container, false);

        WebView webView = (WebView)root.findViewById(R.id.webView);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.setWebViewClient(new WebViewClient());
        webView.setFocusable(false);

        View.OnClickListener listener = new View.OnClickListener()
        {
            public void onClick(View view)
            {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(content));
                startActivity(i);
            }
        };

        FloatingActionButton fab = (FloatingActionButton)root.findViewById(R.id.fab_submission);
        fab.setOnClickListener(listener);

        ArrayAdapter<String> adapter = new CommentAdapter(getContext(), comments);
        ListView commentList = (ListView)root.findViewById(R.id.commentList);
        commentList.setAdapter(adapter);

        // Create an ad request. Check logcat output for the hashed device ID to
        // get test ads on a physical device. e.g.
        // "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."

        AdView adView = (AdView) root.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                                           .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                                           .build();
        adView.loadAd(adRequest);

        return root;
    }

    /**
     *   Called after activity creation
     *
     *   @param savedState saved state
     */

    @Override
    public void onActivityCreated(Bundle savedState)
    {
        super.onActivityCreated(savedState);

        TextView nameView = (TextView)getActivity().findViewById(R.id.textView);
        TextView authorView = (TextView)getActivity().findViewById(R.id.authorView);

        nameView.setText(title);
        authorView.setText(author);

        WebView webView = (WebView)getActivity().findViewById(R.id.webView);
        webView.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                return true;
            }
        });

        webView.loadUrl(content);
    }

    /**
     *   Called to set arguments
     *
     *   @param bundle argument bundles
     */

    @Override
    public void setArguments(Bundle bundle)
    {
        super.setArguments(bundle);

        title = bundle.getString("title");
        author = bundle.getString("author");
        content = bundle.getString("content");
        comments = bundle.getStringArrayList("comments");
    }
}
