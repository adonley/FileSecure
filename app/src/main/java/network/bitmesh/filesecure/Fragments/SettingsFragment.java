package network.bitmesh.filesecure.Fragments;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import network.bitmesh.filesecure.R;

public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
    }
}
