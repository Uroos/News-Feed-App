package com.example.home.newsappone;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Home on 12/19/2017.
 */

public class CustomAdapter extends ArrayAdapter<News> {

    //private static final String LOCATION_SEPARATOR = "|";

    public CustomAdapter(Activity context, ArrayList<News> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        // Get the {@link Word} object located at this position in the list
        News currentObject = getItem(position);

        String sectionName = currentObject.getSectionName();
        String webPublicationDate = currentObject.getWebPublicationDate();
        String webTitle = currentObject.getWebTitle();
        String authorName=currentObject.getAuthorName();
        //This method will separate the author name from the title of the news.
        int sectionColor = getSectionColor(sectionName);

        //Find the TextView in the list_item.xml with the ID offset_text_view.
        TextView sectionTextView = listItemView.findViewById(R.id.section_name);
        sectionTextView.setText(sectionName);
        sectionTextView.setTextColor(sectionColor);
        // Find the TextView in the list_item.xml layout with the ID place_text_view.
        TextView titleTextView = listItemView.findViewById(R.id.web_title);
        titleTextView.setText(webTitle);
        //Find the TextView in the list_item.xml layout with the ID author;
        TextView authorTextView = listItemView.findViewById(R.id.author);
        if(authorName!=null) {
            authorTextView.setText("Author: "+authorName);
        }else{
            authorTextView.setText("Author not available.");
        }
        // Find the TextView in the list_item.xml layout with the ID date_text_view.
        TextView dateTextView = listItemView.findViewById(R.id.web_publication_date);
        dateTextView.setText(webPublicationDate);

        // Return the whole list item layout (containing 2 TextViews and an ImageView)
        // so that it can be shown in the ListView
        return listItemView;
    }

    private int getSectionColor(String sectionName) {
        int sectionColor = 0;
        switch (sectionName) {
            case "Politics":
                sectionColor = ContextCompat.getColor(getContext(), R.color.section1);
                break;
            case "US news":
                sectionColor = ContextCompat.getColor(getContext(), R.color.section2);
                break;
            case "Society":
                sectionColor = ContextCompat.getColor(getContext(), R.color.section3);
                break;
            case "Opinion":
                sectionColor = ContextCompat.getColor(getContext(), R.color.section4);
                break;
            case "Business":
                sectionColor = ContextCompat.getColor(getContext(), R.color.section5);
                break;
            default:
                sectionColor = ContextCompat.getColor(getContext(), R.color.section6);
                break;
        }
        return sectionColor;
    }

//    private void formatName(String sectionName) {
//        //The '|' character is special to the regular expression compiler so you should escape
//        //it with a '\' (backslash), but then again the backslash is also special to the Java
//        //compiler (javac) so you should escape it twice; use "\\|"
//        String[] temp = null;
//        if (sectionName.contains(LOCATION_SEPARATOR)) {
//            temp = sectionName.split("\\" + LOCATION_SEPARATOR);
//            webTitle = temp[0].trim();
//            authorName = "Author: " + temp[1].trim();
//        } else {
//            webTitle = sectionName;
//            authorName = "Author: Not Available";
//        }
//    }
}
