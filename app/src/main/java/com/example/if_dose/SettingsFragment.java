package com.example.if_dose;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceFragmentCompat;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends PreferenceFragmentCompat {


    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(com.example.if_dose.R.xml.preferences);
        if (getString(com.example.if_dose.R.string.app_mode).equals("dev"))
            getPreferenceScreen().findPreference("pref_debug").setEnabled(true);

    }

}
