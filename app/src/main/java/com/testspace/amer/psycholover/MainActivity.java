package com.testspace.amer.psycholover;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements NewsAdapter.OnItemSelectedListener, LoaderManager.LoaderCallbacks<List<News>> {
    public static String REQUEST_URL_STR;
    private ArrayList<News> news;
    NewsAdapter newsAdapter;
    @BindView(R.id.listItemsRecyclerView)
    RecyclerView listItemsRecyclerView;
    @BindView(R.id.listItemsProgressBar)
    ProgressBar listItemsProgressBar;
    @BindView(R.id.listItemsErrorSpaceTextView)
    TextView listItemsErrorSpaceTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        REQUEST_URL_STR = getString(R.string.requestUrlStr);
        ButterKnife.bind(this);
        NetworkInfo networkInfo = getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            newsAdapter = new NewsAdapter();
            news = new ArrayList<>();
            listItemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            getLoaderManager().initLoader(0, null, this);
        } else {
            listItemsProgressBar.setVisibility(View.GONE);
            listItemsRecyclerView.setVisibility(View.GONE);
            listItemsErrorSpaceTextView.setText(R.string.noNetworkError);
            listItemsErrorSpaceTextView.setVisibility(View.VISIBLE);
        }
    }

    public NetworkInfo getActiveNetworkInfo() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            return connectivityManager.getActiveNetworkInfo();
        }
        return null;
    }

    @Override
    public void onItemClick(News news) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(news.getUrl()));
        startActivity(intent);
    }

    @Override
    public Loader<List<News>> onCreateLoader(int id, Bundle args) {
        return new NewsLoader(this, REQUEST_URL_STR);
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> data) {
        listItemsProgressBar.setVisibility(View.GONE);
        listItemsErrorSpaceTextView.setText(R.string.errorLadingData);
        if (data == null || data.size() < 1) {
            listItemsErrorSpaceTextView.setVisibility(View.VISIBLE);
            listItemsRecyclerView.setVisibility(View.GONE);
        } else {
            listItemsErrorSpaceTextView.setVisibility(View.GONE);
            listItemsRecyclerView.setVisibility(View.VISIBLE);
            news = (ArrayList<News>) data;
            newsAdapter.setNews(news);
            listItemsRecyclerView.setAdapter(newsAdapter);
            newsAdapter.setListener(this);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        newsAdapter.setNews(new ArrayList<News>());
    }
}