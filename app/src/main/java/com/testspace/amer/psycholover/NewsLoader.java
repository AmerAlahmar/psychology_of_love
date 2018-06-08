package com.testspace.amer.psycholover;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class NewsLoader extends AsyncTaskLoader<List<News>> {
    String url;

    public NewsLoader(Context context, String url) {
        super(context);
        this.url = url;
    }

    @Override
    public List<News> loadInBackground() {
        if (url != null && !url.isEmpty())
            return NewsQueryUtils.getNews(url);
        return new ArrayList<>();
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}