package com.example.uidesign;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * Created by Abdullah on 10/6/16.
 */
public class Setting_Page  extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener{

    private Context mContext;

    Button breakPref, lunchPref, dinnerPref;
    Button next;
    private SharedPreferences mSharedPreferences;
    SeekBar seekBar, radiusBar;
    TextView limitText, radiusText;
    private SharedPreferences.Editor mEditor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_activity_main);

        mContext = getApplicationContext();

        breakPref = (Button)findViewById(R.id.breakPref);
        lunchPref = (Button)findViewById(R.id.lunchPref);
        dinnerPref = (Button)findViewById(R.id.dinnerPref);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        radiusBar = (SeekBar) findViewById(R.id.radiusBar);
        limitText = (TextView) findViewById(R.id.limitValue);
        radiusText = (TextView) findViewById(R.id.radiusValue);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        mEditor = mSharedPreferences.edit();
        int defLimit = (mSharedPreferences.getInt("limit", 0));
        if ( defLimit == 0 ){
            defLimit = 5;
            limitText.setText(String.valueOf(defLimit));
            seekBar.setProgress(0);
            mEditor.putInt("limit", defLimit).commit();

        }else {
            limitText.setText(String.valueOf(mSharedPreferences.getInt("limit", 0)));
            seekBar.setProgress(mSharedPreferences.getInt("limit", 0) - 5);
        }

        int defRadius = (mSharedPreferences.getInt("radius", 0));
        if ( defRadius == 0 ){
            defRadius = 5;
            radiusText.setText(String.valueOf(defRadius));
            radiusBar.setProgress(0);
            mEditor.putInt("radius", defRadius).commit();


        }else {
            limitText.setText(String.valueOf(mSharedPreferences.getInt("radius", 0)));
            seekBar.setProgress(mSharedPreferences.getInt("radius", 0) - 5);
        }


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



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
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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

    @SuppressWarnings("StatementWithEmptyBody")
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.First) {
            final Intent singleResturant = new Intent(Setting_Page.this, MainActivity.class);
            Setting_Page.this.startActivity(singleResturant);
            finish();
            System.out.println("within Full Day Activity");

            // Handle the camera action
        } else if (id == R.id.Second) {
            final Intent singleResturant = new Intent(Setting_Page.this, ResturantActivity.class);
            Setting_Page.this.startActivity(singleResturant);
            finish();
            System.out.println("Second Button");

        } else if (id == R.id.Third) {
            System.out.println("Third Button");


        } else if (id == R.id.Fourth) {
            System.out.println("Fourth Button");


        } else if (id == R.id.sectionOne) {
            System.out.println("Already in setting");



        } else if (id == R.id.sectionTwo) {
            System.out.println("Second Section Button");

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
