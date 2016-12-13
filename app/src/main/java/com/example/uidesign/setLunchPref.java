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
/**
 * This class represents the activity preferences menu in the settings page
 */
public class setLunchPref extends PreferenceActivity implements Preference.OnPreferenceClickListener, Preference.OnPreferenceChangeListener {

    //the variables this class uses
    //preferences
    SharedPreferences mPreferences;

    //the checkboxes in the preference menu
    CheckBoxPreference a,b,c,d,e,f,g,h,i,j,k,l,m;
    int count = 0;
    //the values in the checkboxes
    String[] lunch = {"ChineseL","IndianL","MexicanL","AmericanL", "ItalianL", "JapaneseL", "KoreanL",
            "CombodianL", "VietnameseL", "GreekL", "MediterraneanL", "Sea FoodL", "DealsL"};
    /**
     * This is the on create function that will run when the acitivity preferences are run
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // TODO Auto-generated method stub

        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.lunchpref);

        //findinig each checkbox in the screen
        a = (CheckBoxPreference) findPreference(lunch[0]);
        b = (CheckBoxPreference) findPreference(lunch[1]);
        c = (CheckBoxPreference) findPreference(lunch[2]);
        d = (CheckBoxPreference) findPreference(lunch[3]);
        e = (CheckBoxPreference) findPreference(lunch[4]);
        f = (CheckBoxPreference) findPreference(lunch[5]);
        g = (CheckBoxPreference) findPreference(lunch[6]);
        h = (CheckBoxPreference) findPreference(lunch[7]);
        i = (CheckBoxPreference) findPreference(lunch[8]);
        j = (CheckBoxPreference) findPreference(lunch[9]);
        k = (CheckBoxPreference) findPreference(lunch[10]);
        l = (CheckBoxPreference) findPreference(lunch[11]);
        m = (CheckBoxPreference) findPreference(lunch[12]);

        //giving each checkbox a listener to do something when its run
        a.setOnPreferenceChangeListener(this);
        b.setOnPreferenceChangeListener(this);
        c.setOnPreferenceChangeListener(this);
        d.setOnPreferenceChangeListener(this);
        e.setOnPreferenceChangeListener(this);
        f.setOnPreferenceChangeListener(this);
        g.setOnPreferenceChangeListener(this);
        h.setOnPreferenceChangeListener(this);
        i.setOnPreferenceChangeListener(this);
        j.setOnPreferenceChangeListener(this);
        k.setOnPreferenceChangeListener(this);
        l.setOnPreferenceChangeListener(this);
        m.setOnPreferenceChangeListener(this);
        //this checks how many check boxes are checked
        countChecked();

        if (count >= 3){
            disableAll();
        }else {
            enableAll();
        }


    }
    /**
     * This function checks the number of checked boxes
     */
    public void countChecked(){
        for(String a : lunch ){
            CheckBoxPreference temp = (CheckBoxPreference) findPreference(a);
            if(temp.isChecked()){
                count++;
            }
        }
    }
    /**
     * This function disables all the unchecked boxes
     */
    public void disableAll(){
        for(String a : lunch ){
            CheckBoxPreference temp = (CheckBoxPreference) findPreference(a);
            if(!temp.isChecked()){
                temp.setEnabled(false);
            }
        }
    }

    /**
     * this function enables all the unchecked checkboxes
     */
    public void enableAll(){
        for(String a : lunch ){
            CheckBoxPreference temp = (CheckBoxPreference) findPreference(a);
            if(!temp.isChecked()){
                temp.setEnabled(true);
            }
        }
    }
    /**
     * This function is run when a checkbox is clicked on the menu, and checks if the
     * number of the checked items is greater than or equal to 3 it will disable the rest,
     * otherwise enable the rest
     * @param preferenceScreen - the preference menu
     * @param preference
     * @return
     */
    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        Log.d("Size",""+preferenceScreen.getPreferenceCount());
        if(count >= 3)
            disableAll();

        if(count < 3)
            enableAll();

        return super.onPreferenceTreeClick(preferenceScreen, preference);


    }
    /**
     * This function will will if an item on the menu has changed, incrementing the
     * checked items counter or decrementing it.
     * @param preference
     * @param newValue
     * @return
     */
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


    /**
     * This function is run when a checkbox is pressed
     * @param preference - the checkbox/preference being pressed
     * @return
     */
    @Override
    public boolean onPreferenceClick(Preference preference) {

        int size = mPreferences.getAll().size();

        if (mPreferences.getAll().size() > 3) {

        }
        return true;
    }


}