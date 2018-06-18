package com.testspace.amer.psycholover;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
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
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String toDate = sharedPrefs.getString(
                getString(R.string.settings_toDate_key),
                getString(R.string.settings_toDate_default));
        String orderBy = sharedPrefs.getString(
                getString(R.string.settings_orderBy_key),
                getString(R.string.settings_orderBy_default));
        Uri baseUri = Uri.parse(REQUEST_URL_STR);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter(getString(R.string.query_orderBy_key), orderBy);
        if (!toDate.isEmpty() && !toDate.equals(getString(R.string.settings_toDate_default))) {
            uriBuilder.appendQueryParameter(getString(R.string.query_toDate_key), toDate);
        }
        uriBuilder.appendQueryParameter(getString(R.string.query_showFields_key), getString(R.string.query_showFields_value));
        uriBuilder.appendQueryParameter(getString(R.string.query_pageSize_key), getString(R.string.query_pageSize_value));
        uriBuilder.appendQueryParameter(getString(R.string.query_query_key), getString(R.string.query_query_value));
        uriBuilder.appendQueryParameter(getString(R.string.query_apiKey_key), getString(R.string.query_apiKey_value));
        return new NewsLoader(this, uriBuilder.toString());
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}