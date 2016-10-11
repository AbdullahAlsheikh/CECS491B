package com.example.uidesign;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by muhannad on 10/10/16.
 */

public class setLunchPref extends PreferenceActivity {

    @Override

    protected void onCreate(Bundle savedInstanceState) {

        // TODO Auto-generated method stub

        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.lunchpref);

    }



}