package com.example.home.newsappone;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<News>> {
    //https://content.guardianapis.com/search?q=debate&tag=politics/politics&from-date=2018&api-key=test
    private static final String USGS_REQUEST_URL = "https://content.guardianapis.com/search";
    private CustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /********************************************************************************/
        /***************Setting adapter on the list view*********************************/
        /********************************************************************************/
        // Find a reference to the {@link ListView} in the layout
        ListView newsListView = (ListView) findViewById(R.id.list);
        newsListView.setEmptyView(findViewById(R.id.emptyView));
        // Create a new adapter that takes an empty list of news as input
        adapter = new CustomAdapter(this, new ArrayList<News>());
        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        newsListView.setAdapter(adapter);
        // Set a listener on the listView so when an item is clicked, it opens the corresponding
        // web page.
        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Get the news object at the selected position
                News selectedNewsObject = adapter.getItem(position);
                String url = selectedNewsObject.getWebUrl();

                Uri webpage = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });

        /********************************************************************************/
        /******************Checking Network Connectivity*********************************/
        /********************************************************************************/
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if (isConnected) {
            getLoaderManager().initLoader(1, null, this).forceLoad();
        } else {
            ProgressBar pb = findViewById(R.id.progressBar);
            TextView empty = findViewById(R.id.emptyView);
            pb.setVisibility(View.GONE);
            empty.setText(getString(R.string.network_error));
        }
    }

    @Override
    public Loader<ArrayList<News>> onCreateLoader(int i, Bundle bundle) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Here we are getting the value of preference that is stored in memory so that the screen can be initialized
        //with last chosen values of preferences.
        //getString retrieves a String value from the preferences. The second parameter is the default value for this preference.
        String search = sharedPrefs.getString(
                getString(R.string.settings_search_key),
                getString(R.string.settings_search_default));
        String section = sharedPrefs.getString(
                getString(R.string.settings_sections_key),
                getString(R.string.settings_sections_default));

        /*********************************************************************/
        /*******************Using the Uri Builer******************************/
        /*********************************************************************/

        // parse breaks apart the URI string that's passed into its parameter
        Uri baseUri = Uri.parse(USGS_REQUEST_URL);
        // buildUpon prepares the baseUri that we just parsed so we can add query parameters to it
        Uri.Builder uriBuilder = baseUri.buildUpon();
        // Append query parameter and its value. For example, the `format=json`
        //https://content.guardianapis.com/search?format=json&q=search&section=section&from-date=2018&api-key=test&show-references=all
        uriBuilder.appendQueryParameter("format", "json");
        uriBuilder.appendQueryParameter("section", section);
        uriBuilder.appendQueryParameter("from-date", "2018");
        uriBuilder.appendQueryParameter("api-key", "test");
        uriBuilder.appendQueryParameter("show-references", "all");
        uriBuilder.appendQueryParameter("q", search);
        uriBuilder.appendQueryParameter("show-fields","byline");
        return new NewsLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<News>> loader, ArrayList<News> news) {
        TextView empty = findViewById(R.id.emptyView);
        ProgressBar pb = findViewById(R.id.progressBar);
        pb.setVisibility(View.GONE);
        // Clear the adapter of previous news data
        adapter.clear();
        // If there is a valid list of {@link New}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (news != null && !news.isEmpty()) {
            adapter.addAll(news);
        } else {
            empty.setText(getString(R.string.no_news_error));
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<News>> loader) {
        adapter.addAll(new ArrayList<News>());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the Options Menu we specified in XML
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
