package com.example.uidesign;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * Created by Abdullah on 10/6/16.
 */
public class Setting_Page  extends AppCompatActivity {

    private Context mContext;

    Button breakPref, lunchPref, dinnerPref;
    Button next;
    private SharedPreferences mSharedPreferences;
    SeekBar seekBar;
    TextView limitText;
    private SharedPreferences.Editor mEditor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_layout);

        mContext = getApplicationContext();

        breakPref = (Button)findViewById(R.id.breakPref);
        lunchPref = (Button)findViewById(R.id.lunchPref);
        dinnerPref = (Button)findViewById(R.id.dinnerPref);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        limitText = (TextView) findViewById(R.id.limitValue);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        mEditor = mSharedPreferences.edit();

        limitText.setText(String.valueOf(mSharedPreferences.getInt("limit", 0)));

        seekBar.setProgress(mSharedPreferences.getInt("limit", 0) - 5);



        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                limitText.setText(String.valueOf(seekBar.getProgress() + 5));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                limitText.setText(String.valueOf(seekBar.getProgress()+ 5) );
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                limitText.setText(String.valueOf(seekBar.getProgress() + 5) );

                int mProgress = seekBar.getProgress() + 5;
                mEditor.putInt("limit", mProgress).commit();

                /**
                 * to get value of seek bar use this
                 *  int mProgress = mSharedPrefs.getInt("limit", 0);
                 *  mSeekBar.setProgress(mProgress);
                 */
            }

        });



        breakPref.setOnClickListener(new Button.OnClickListener(){
            @Override

            public void onClick(View arg0) {

                // TODO Auto-generated method stub

                Intent intent = new Intent(Setting_Page.this,setBreakPref.class);

                startActivityForResult(intent, 0);

            }});

        dinnerPref.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View arg0) {

                // TODO Auto-generated method stub

                Intent intent = new Intent(Setting_Page.this,setDinnerPref.class);

                startActivityForResult(intent, 0);

            }});

        lunchPref.setOnClickListener(new Button.OnClickListener(){



            @Override

            public void onClick(View arg0) {

                // TODO Auto-generated method stub

                Intent intent = new Intent(Setting_Page.this,setLunchPref.class);

                startActivityForResult(intent, 0);

            }});

        checkPref();
    }

    private void checkPref(){

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(Setting_Page.this);
        mEditor = mSharedPreferences.edit();
        String[] breakfast = {"DelisB","AmericanB","SandwichesB", "Coffee and TeaB", "BakeryB", "SaladB"};
        String[] lunch = {"ChineseL","IndianL","MexicanL","AmericanL", "ItalianL", "JapaneseL", "KoreanL",
                "CombodianL", "VietnameseL", "GreekL", "MediterraneanL", "Sea FoodL"};
        String[] dinner = {"ChineseD","IndianD","MexicanD","AmericanD", "ItalianD", "JapaneseD", "KoreanD",
                "CombodianD", "VietnameseD", "GreekD", "MediterraneanD", "Sea FoodD"};
////        String[] dinner = {"a","b","c"};
//        ArrayList<String> cat = new ArrayList<>(10);
//
//        cat.add("Chinese");
//        cat.add("Fast Food");
//        cat.add("Indian");
//        cat.add("American");
//        cat.add("Japanese");
//        cat.add("Korean");
//        cat.add("Mediterranean");
//        cat.add("Greek");
//        cat.add("Combodian");
//        cat.add("Italian");
//        cat.add("Vietnamese");
//        cat.add("Mexican");
        String breakFastStr = " ";

        for (String catog : breakfast){
            if(mSharedPreferences.getBoolean(catog, false)){
                breakFastStr += " " + catog.substring(0,catog.length()-1);
            }
        }

        String lunchStr = " ";
        for (String catog : lunch){
            if(mSharedPreferences.getBoolean(catog, false)){
                lunchStr += " " + catog.substring(0,catog.length()-1);
            }
        }

        String dinnerStr = " ";
        for (String catog : dinner){
            if(mSharedPreferences.getBoolean(catog, false)){
                dinnerStr += " " + catog.substring(0,catog.length()-1);
            }
        }

        //store the strings in preferences
        mEditor.putString("breakfastCat",breakFastStr).commit();
        mEditor.putString("lunchCat",lunchStr).commit();
        mEditor.putString("dinnerCat",dinnerStr).commit();

        mEditor.apply();


    }
}