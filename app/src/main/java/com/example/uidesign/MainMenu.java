package com.example.uidesign;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.common.api.GoogleApiClient;

//import com.facebook.CallbackManager;
//import com.facebook.FacebookSdk;
//import com.facebook.login.widget.LoginButton;
//import com.firebase.client.Firebase;


public class MainMenu extends AppCompatActivity implements View.OnClickListener{
    //---------------------------------------Declarations---------------------------------------
    //Firebase mRef;
    public static final String DB_URL = "https://incandescent-heat-931.firebaseio.com/";
    //private CallbackManager callbackManager;
    //private LoginButton FbLoginButton;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    public static View screen;
    public static ImageView full, events, nights, single, options, quit;
    private CallbackManager callbackManager;
    public static AccessToken accessToken;
    public static String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);
        MobileAds.initialize(getApplicationContext(), "1:68912929915:android:0488b4351ca1a767");

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        full = (ImageView) findViewById(R.id.day);
        events = (ImageView) findViewById(R.id.event);
        nights = (ImageView) findViewById(R.id.night);
        single = (ImageView) findViewById(R.id.single);
        options = (ImageView) findViewById(R.id.options);
        quit = (ImageView) findViewById(R.id.quit);
        full.setOnClickListener(this);
        events.setOnClickListener(this);
        nights.setOnClickListener(this);
        single.setOnClickListener(this);
        options.setOnClickListener(this);
        quit.setOnClickListener(this);

        //here
        screen = findViewById(R.id.categorias_table);

        /**
         move(full);
         moveleft(nights);
         move(single);
         moveleft(events);
         move(options);
         moveleft(quit);
         */

        move(nights);
        moveleft(full);
        move(events);
        moveleft(single);
        move(quit);
        moveleft(options);



    }





    public void move(View view) {
        Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move);
        view.startAnimation(animation1);
    }

    public void moveleft(View view) {
        Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.moveleft);
        view.startAnimation(animation1);
    }

    public void zoom(View view) {
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom);
        view.startAnimation(animation);
    }

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

    private void moveButtons() {
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.moveaway);
        full.startAnimation(animation);
        nights.startAnimation(animation);
        events.startAnimation(animation);
        single.startAnimation(animation);
        options.startAnimation(animation);
        quit.startAnimation(animation);
    }

//    @Override
//    public void onClick(View v) {
//        zoom(v);
//        switch (v.getId() /*to get clicked view id**/) {
//            case R.id.night:
//                ImageView purple = (ImageView) findViewById(R.id.purple);
//                purple.setVisibility(View.VISIBLE);
//                zoom(purple);
//                // do something when the corky is clicked
//                break;
//
//            case R.id.day:
//                final Intent mainIntent = new Intent(MainMenu.this, MainActivity.class);
//                MainMenu.this.startActivity(mainIntent);
//                break;
//
//            case R.id.single:
//                System.out.println("Single Activity");
//                final Intent singleResturant = new Intent(MainMenu.this, ResturantActivity.class);
//                MainMenu.this.startActivity(singleResturant);
//                break;
//
//        }
//    }
public boolean isLoggedIn() {
    AccessToken accessToken = AccessToken.getCurrentAccessToken();
    return accessToken != null;
}

}