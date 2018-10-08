package com.example.home.newsappone;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.ArrayList;

/**
 * Created by Home on 12/20/2017.
 */

public class NewsLoader extends AsyncTaskLoader<ArrayList<News>> {
    private String urlString;

    public NewsLoader(Context context, String url) {
        super(context);
        urlString = url;
    }

    @Override
    public ArrayList<News> loadInBackground() {
        ArrayList<News> news = QueryUtils.fetchNewsData(urlString);
        return news;
    }
}
