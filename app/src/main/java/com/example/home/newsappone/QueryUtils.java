package com.example.home.newsappone;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Created by Home on 12/19/2017.
 */

public class QueryUtils {
    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Query the USGS dataset and return a list of {@link News} objects.
     */
    public static ArrayList<News> fetchNewsData(String urls) {

        // Don't perform the request if there are no URLs, or the first URL is null.
        // We must return null because an object is expected to be returned.
        if (urls.length() < 1 || urls == null) {
            return null;
        }
        // Create URL object
        URL url = createUrl(urls);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e("QueryUtils", "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link News}s
        ArrayList<News> news = extractNews(jsonResponse);

        // Return the list of {@link News}s
        return news;
    }

    /**
     * Return a list of {@link News} objects that has been built up from
     * parsing a JSON response.
     */
    private static ArrayList<News> extractNews(String jsonResponse) {

        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }
        // Create an empty ArrayList that we can start adding news to
        ArrayList<News> news = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            //SAMPLE_JSON_RESPONSE string is converted in to a JSON object and is referred as
            //the root.
            JSONObject root = new JSONObject(jsonResponse);

            //Extract the "response" object. "response" is an object within the root.
            JSONObject response = root.getJSONObject("response");

            //Extract the "result" array.
            JSONArray results = response.getJSONArray("results");

            //Loop through all the objects of the "results" array.
            for (int i = 0; i < results.length(); i++) {
                //Get objects at position i
                JSONObject newObject = results.optJSONObject(i);

                //Now that we are in the results array which has many objects, we can start
                // retrieving the desired values.
                String section = newObject.getString("sectionName");
                String date = newObject.getString("webPublicationDate");
                String title = newObject.getString("webTitle");
                String url = newObject.getString("webUrl");
                String author=null;

                JSONObject authorObject=newObject.optJSONObject("fields");
                if(authorObject!=null) {
                    author = authorObject.getString("byline");
                }

                //Now we add an Earthquake object to the ArrayList
                news.add(new News(section, date, title, url,author));
            }
        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of news
        return news;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException exception) {
            Log.e("QueryUtils", "Error with creating URL", exception);
            return null;
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        //if url is null, then return early.
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.connect();
            int responseCode = urlConnection.getResponseCode();
            if (responseCode == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e("makeHttpRequest", "response code is: " + responseCode);
            }
        } catch (IOException e) {
            // TODO: Handle the exception
            Log.e("makeHttpRequest", "IOException is thrown.");
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // function must handle java.io.IOException here
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }
}
