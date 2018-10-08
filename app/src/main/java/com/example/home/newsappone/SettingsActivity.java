package com.example.home.newsappone;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    public static class NewsPreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {
        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
            //We’ll need to update the preference summary (the UI) when the settings activity is launched in onCreate().
            //To do so, we’ll need to first find the preference we’re interested in and
            //then bind the current preference value to be displayed.
            Preference search = findPreference(getString(R.string.settings_search_key));
            bindPreferenceSummaryToValue(search);
            Preference sections = findPreference(getString(R.string.settings_sections_key));
            bindPreferenceSummaryToValue(sections);

        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object o) {
            //Convert the changed value of preference into a string
            String stringValue = o.toString();
            //If listpreference was altered, then find the index of the string
            if (preference instanceof ListPreference) {
                ListPreference lp = (ListPreference) preference;
                int prefIndex = lp.findIndexOfValue(stringValue);
                if (prefIndex >= 0) {
                    //get the values of the preference
                    CharSequence[] labels = lp.getEntries();
                    preference.setSummary(labels[prefIndex]);
                }
            } else {
                preference.setSummary(stringValue);
            }
            return true;
        }

        private void bindPreferenceSummaryToValue(Preference preference) {
            //We use setOnPreferenceChangeListener to set the current EarthquakePreferenceFragment
            //instance to listen for changes to the preference we pass in using.
            preference.setOnPreferenceChangeListener(this);
            //We also read the current value of the preference stored in the SharedPreferences on the device
            //and display that in the preference summary (so that the user can see the current value of the preference).
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(preference.getContext());
            String preferenceString = preferences.getString(preference.getKey(), "");
            onPreferenceChange(preference, preferenceString);
        }
    }

}
