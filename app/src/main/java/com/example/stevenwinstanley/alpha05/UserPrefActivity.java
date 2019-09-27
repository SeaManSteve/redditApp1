package com.example.stevenwinstanley.alpha05;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceActivity;

public class UserPrefActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.user_pref);
    }
}
