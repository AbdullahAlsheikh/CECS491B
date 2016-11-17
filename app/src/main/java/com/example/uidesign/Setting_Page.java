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

    Button breakPref, lunchPref, dinnerPref, activityPref;
    Button next;
    private SharedPreferences mSharedPreferences;
    SeekBar seekBar, radiusBar;
    TextView limitText, radiusText;
    private SharedPreferences.Editor mEditor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_layout);

        mContext = getApplicationContext();

        breakPref = (Button)findViewById(R.id.breakPref);
        activityPref = (Button)findViewById(R.id.activityPref);
        lunchPref = (Button)findViewById(R.id.lunchPref);
        dinnerPref = (Button)findViewById(R.id.dinnerPref);

        //seekBar = (SeekBar)findViewById(R.id.seekBar);
        radiusBar = (SeekBar)findViewById(R.id.radiusBar);
       // limitText = (TextView)findViewById(R.id.limitValue);
        radiusText = (TextView)findViewById(R.id.radiusValue);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        mEditor = mSharedPreferences.edit();

//        int defLimit = mSharedPreferences.getInt("limit", 0);
//        if ( defLimit == 0 ){
//            defLimit = 5;
//            limitText.setText(String.valueOf(defLimit));
//            seekBar.setProgress(0);
//            mEditor.putInt("limit", defLimit).commit();
//            System.out.println("Value of limit on first run: "+ defLimit);
//
//
//        }else {
//            limitText.setText(String.valueOf(defLimit));
//            seekBar.setProgress(0);
//            seekBar.setProgress(defLimit - 5);
//            System.out.println("Value of limit on second run: "+ defLimit);
//        }

        //System.out.println("Value of limit: "+ defLimit);


        int defRadius = (mSharedPreferences.getInt("radius", 0));
        if ( defRadius == 0 ){
            defRadius = 5;
            radiusText.setText(String.valueOf(defRadius));
            radiusBar.setProgress(0);
            mEditor.putInt("radius", defRadius).commit();
            System.out.println("Value of radius on first run: "+ defRadius);
        }else {

            radiusText.setText(String.valueOf(defRadius));
            radiusBar.setProgress(0);
            radiusBar.setProgress(defRadius - 5);
            System.out.println("Value of radius on second run: "+ defRadius);
        }






//
//        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                limitText.setText(String.valueOf(seekBar.getProgress() + 5));
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//                limitText.setText(String.valueOf(seekBar.getProgress()+ 5) );
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//                limitText.setText(String.valueOf(seekBar.getProgress() + 5) );
//
//                int mProgress = seekBar.getProgress() + 5;
//                mEditor.putInt("limit", mProgress).commit();
//
//                /**
//                 * to get value of seek bar use this
//                 *  int mProgress = mSharedPrefs.getInt("limit", 0);
//                 *  mSeekBar.setProgress(mProgress);
//                 */
//            }
//
//        });

        radiusBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                radiusText.setText(String.valueOf(seekBar.getProgress() + 5));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                radiusText.setText(String.valueOf(seekBar.getProgress()+ 5) );
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                radiusText.setText(String.valueOf(seekBar.getProgress() + 5) );

                int mProgress = seekBar.getProgress() + 5;
                mEditor.putInt("radius", mProgress).commit();

                /**
                 * to get value of seek bar use this
                 *  int mProgress = mSharedPrefs.getInt("radius", 0);
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
                //                checkPref();


            }});

        dinnerPref.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View arg0) {

                // TODO Auto-generated method stub

                Intent intent = new Intent(Setting_Page.this,setDinnerPref.class);

                startActivityForResult(intent, 0);
                //checkPref();

            }});

        lunchPref.setOnClickListener(new Button.OnClickListener(){



            @Override

            public void onClick(View arg0) {

                // TODO Auto-generated method stub

                Intent intent = new Intent(Setting_Page.this,setLunchPref.class);

                startActivityForResult(intent, 0);

                //checkPref();

            }});

        activityPref.setOnClickListener(new Button.OnClickListener(){



            @Override

            public void onClick(View arg0) {

                // TODO Auto-generated method stub

                Intent intent = new Intent(Setting_Page.this,setActivityPref.class);

                startActivityForResult(intent, 0);

                //checkPref();

            }});

        //checkPref();
    }

    @Override
    public void onBackPressed() {
        checkPref();
        
        String a = mSharedPreferences.getString("lunchCat", "");
        System.out.println("String from preferences for lunch: "+ a);
        String a2 = mSharedPreferences.getString("dinnerCat", "");
        System.out.println("String from preferences for dinner: "+ a2);
        String a3 = mSharedPreferences.getString("breakfastCat", "");
        System.out.println("String from preferences for break fast: "+ a3);
        String a4 = mSharedPreferences.getString("activityCat", "");
        System.out.println("String from preferences for activity: "+ a4);
        super.onBackPressed();
    }

    private void checkPref(){

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(Setting_Page.this);
        mEditor = mSharedPreferences.edit();
        String[] breakfast = {"DelisB","AmericanB","SandwichesB", "Coffee and TeaB", "BakeryB", "SaladB"};

        String[] lunch = {"ChineseL","IndianL","MexicanL","AmericanL", "ItalianL", "JapaneseL", "KoreanL",
                "CombodianL", "VietnameseL", "GreekL", "MediterraneanL", "Sea FoodL"};

        String[] dinner = {"ChineseD","IndianD","MexicanD","AmericanD", "ItalianD", "JapaneseD", "KoreanD",
                "CombodianD", "VietnameseD", "GreekD", "MediterraneanD", "Sea FoodD"};

        String[] activity = {"MuseumA","AquariumA","ParkA","Amusement ParkA", "MallA", "Movie TheatreA", "Skate ParkA",
                "Skating RinkA", "BeachA", "SkiingA", "SnowboardingA"};



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

        String activityStr = " ";
        for (String catog : activity){
            if(mSharedPreferences.getBoolean(catog, false)){
                activityStr += " " + catog.substring(0,catog.length()-1);
            }
        }
        //store the strings in preferences
        mEditor.putString("breakfastCat",breakFastStr).commit();
        mEditor.putString("lunchCat",lunchStr).commit();
        mEditor.putString("dinnerCat",dinnerStr).commit();
        mEditor.putString("activityCat",activityStr).commit();
        mEditor.apply();


    }
}
