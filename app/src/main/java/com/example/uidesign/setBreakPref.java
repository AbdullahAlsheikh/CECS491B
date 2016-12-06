package com.example.uidesign;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.util.Log;

/**
 * Created by muhannad on 10/10/16.
 */

public class setBreakPref extends PreferenceActivity implements Preference.OnPreferenceClickListener, Preference.OnPreferenceChangeListener {



    SharedPreferences mPreferences;
    PreferenceScreen preferenceScreen;
    CheckBoxPreference a,b,c,d,e,f,g,h,i,j,k,l,m;
    int count = 0;
    String[] lunch = {"DelisB","AmericanB","SandwichesB", "Coffee and TeaB", "BakeryB", "SaladB", "DealsB"};



    @Override

    protected void onCreate(Bundle savedInstanceState) {

        // TODO Auto-generated method stub

        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.breakpref);

        a = (CheckBoxPreference) findPreference(lunch[0]);
        b = (CheckBoxPreference) findPreference(lunch[1]);
        c = (CheckBoxPreference) findPreference(lunch[2]);
        d = (CheckBoxPreference) findPreference(lunch[3]);
        e = (CheckBoxPreference) findPreference(lunch[4]);
        f = (CheckBoxPreference) findPreference(lunch[5]);
        g = (CheckBoxPreference) findPreference(lunch[6]);


        a.setOnPreferenceChangeListener(this);
        b.setOnPreferenceChangeListener(this);
        c.setOnPreferenceChangeListener(this);
        d.setOnPreferenceChangeListener(this);
        e.setOnPreferenceChangeListener(this);
        f.setOnPreferenceChangeListener(this);
        g.setOnPreferenceChangeListener(this);


        countChecked();

        if (count >= 3) {
            disableAll();
        } else {
            enableAll();
        }

    }

    public void countChecked(){
        for(String a : lunch ){
            CheckBoxPreference temp = (CheckBoxPreference) findPreference(a);
            if(temp.isChecked()){
                count++;
            }
        }
    }

    public void disableAll(){
        for(String a : lunch ){
            CheckBoxPreference temp = (CheckBoxPreference) findPreference(a);
            if(!temp.isChecked()){
                temp.setEnabled(false);
            }
        }
    }

    public void enableAll(){
        for(String a : lunch ){
            CheckBoxPreference temp = (CheckBoxPreference) findPreference(a);
            if(!temp.isChecked()){
                temp.setEnabled(true);
            }
        }
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        Log.d("Size",""+preferenceScreen.getPreferenceCount());
        if(count >= 3)
            disableAll();

        if(count < 3)
            enableAll();

        return super.onPreferenceTreeClick(preferenceScreen, preference);


    }

    public boolean onPreferenceChange(Preference preference, Object newValue)
    {
        boolean checked = Boolean.valueOf(newValue.toString());


        if(checked){
            count++;
        }else {
            count--;
        }


        Log.d("Count",""+count);


        //set your shared preference value equal to checked

        return true;
    }



    @Override
    public boolean onPreferenceClick(Preference preference) {

        int size = mPreferences.getAll().size();

        if (mPreferences.getAll().size() > 3) {

        }
        return true;
    }







}