package com.example.uidesign;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.common.api.GoogleApiClient;


/**
 * This class is responsible for handling the main menu page
 */
public class MainMenu extends AppCompatActivity implements View.OnClickListener{
    //---------------------------------------Declarations---------------------------------------

    public static final String DB_URL = "https://incandescent-heat-931.firebaseio.com/";

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    private GoogleApiClient client;
    //These are variables that are being used in the main menu layout
    public static View screen;
    public static ImageView full, events, nights, single;
    public Button options;
    private CallbackManager callbackManager;
    public static AccessToken accessToken;
    public static String userId;
    private Context mContext;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    /**
     * The function that will be run when the main menu layout is created
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        //ads initialization
        MobileAds.initialize(getApplicationContext(), "1:68912929915:android:0488b4351ca1a767");


        mContext = getApplicationContext();
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);

        mEditor = mSharedPreferences.edit();
        boolean fullToturial = mSharedPreferences.getBoolean("fullToturial",false);
        if(!fullToturial) {
            mEditor.putBoolean("fullTutorial", false).commit();
        }
        boolean singleToturial = mSharedPreferences.getBoolean("singleToturial",false);
        if(!singleToturial) {
            mEditor.putBoolean("singleTutorial", false).commit();

        }
        mEditor.apply();


        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        full = (ImageView) findViewById(R.id.day);
        events = (ImageView) findViewById(R.id.event);
        nights = (ImageView) findViewById(R.id.night);
        single = (ImageView) findViewById(R.id.single);
        options = (Button) findViewById(R.id.options);

        full.setOnClickListener(this);
        events.setOnClickListener(this);
        nights.setOnClickListener(this);
        single.setOnClickListener(this);
        options.setOnClickListener(this);


        //here
        screen = findViewById(R.id.categorias_table);
        //animation for the main menu buttons at the startup
        move(nights);
        moveleft(full);
        move(events);
        moveleft(single);
        moveleft(options);



    }


    /**
     * This function handles the animation of the buttons on the main menu
     * @param view - the view that is being moved
     */
    public void move(View view) {
        Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move);
        view.startAnimation(animation1);
    }

    /**
     * This function handles the animation of the buttons to move them to the left
     * @param view
     */
    public void moveleft(View view) {
        Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.moveleft);
        view.startAnimation(animation1);
    }

    /**
     * This function makes the button zooms in
     * @param view the view to be zoomed in
     */
    public void zoom(View view) {
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom);
        view.startAnimation(animation);
    }

    /**
     *A function that runs when a button is pressed
     * @param v
     */
    @Override
    public void onClick(View v) {
        zoom(v);

        switch (v.getId() /*to get clicked view id**/) {
            case R.id.night:
                screen.setBackgroundColor(getResources().getColor(R.color.Purple));
                break;
            case R.id.day:
                screen.setBackgroundColor(getResources().getColor(R.color.DarkGreen));
                break;
            case R.id.event:
                screen.setBackgroundColor(getResources().getColor(R.color.DBlue));
                break;
            case R.id.single:
                screen.setBackgroundColor(getResources().getColor(R.color.Yellow));
                break;
            case R.id.options:
                screen.setBackgroundColor(getResources().getColor(R.color.Grey));



        }


        moveButtons();

        switch (v.getId() /*to get clicked view id**/) {
            case R.id.night:
                Intent activity = new Intent(this,BarHoppingMode.class);
                startActivity(activity);
                break;
            case R.id.day:
                Intent activity1 = new Intent(this,MainActivity.class);
                startActivity(activity1);
                break;
            case R.id.event:
                Intent activity2 = new Intent(this,EventsActivity.class);
                startActivity(activity2);
                break;
            case R.id.single:
                Intent activity3 = new Intent(this,ResturantActivity.class);
                startActivity(activity3);
                break;
            case R.id.options:
                Intent activity4 = new Intent(this,Setting_Page.class);
                startActivity(activity4);
                break;

        }


    }

    /**
     * This function moves all the buttons at the same time.
     */
    private void moveButtons() {
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.moveaway);
        full.startAnimation(animation);
        nights.startAnimation(animation);
        events.startAnimation(animation);
        single.startAnimation(animation);
        options.startAnimation(animation);

    }



}