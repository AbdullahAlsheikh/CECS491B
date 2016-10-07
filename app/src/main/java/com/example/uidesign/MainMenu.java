package com.example.uidesign;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.login.widget.LoginButton;
import com.firebase.client.Firebase;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;


public class MainMenu extends AppCompatActivity implements View.OnClickListener{
    //---------------------------------------Declarations---------------------------------------
    Firebase mRef;
    public static final String DB_URL = "https://incandescent-heat-931.firebaseio.com/";
    private CallbackManager callbackManager;
    private LoginButton FbLoginButton;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.main_menu);

        ImageView full = (ImageView) findViewById(R.id.day);
        ImageView events = (ImageView) findViewById(R.id.event);
        ImageView nights = (ImageView) findViewById(R.id.night);
        ImageView single = (ImageView) findViewById(R.id.single);
        ImageView options = (ImageView) findViewById(R.id.options);
        ImageView quit = (ImageView) findViewById(R.id.quit);
        full.setOnClickListener(this);
        events.setOnClickListener(this);
        nights.setOnClickListener(this);
        single.setOnClickListener(this);

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


        /**
         FbLoginButton = (LoginButton) findViewById(R.id.FBlogin_button);
         FbLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
        @Override public void onSuccess(LoginResult loginResult) {
        onFacebookAccessTokenChange(AccessToken.getCurrentAccessToken());
        }
        @Override public void onCancel() {}
        @Override public void onError(FacebookException error) {}
        });
         **/

        //mRef = new Firebase(DB_URL);


/**
 RotateAnimation rotate = new RotateAnimation(0, 360,
 Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
 0.5f);
 RotateAnimation rotate1 = new RotateAnimation(90, 450,
 Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
 0.5f);
 rotate.setDuration(4000);
 rotate1.setDuration(4000);
 rotate.setRepeatCount(Animation.INFINITE);
 rotate.setInterpolator(new LinearInterpolator());
 rotate1.setRepeatCount(Animation.INFINITE);
 rotate1.setInterpolator(new LinearInterpolator());
 findViewById(R.id.night_mode).setAnimation(rotate);
 findViewById(R.id.events).setAnimation(rotate1);
 findViewById(R.id.fulldaybtn).setAnimation(rotate);
 findViewById(R.id.singleEvent).setAnimation(rotate1);
 RadioButton center = (RadioButton) findViewById(R.id.center);
 RotateAnimation rotateInCirle = new RotateAnimation(0, 360,
 Animation.RELATIVE_TO_PARENT, center.getX(), Animation.RELATIVE_TO_PARENT,
 center.getY());
 rotateInCirle.setDuration(4000);
 rotateInCirle.setRepeatCount(Animation.INFINITE);
 findViewById(R.id.night_mode).setAnimation(rotateInCirle);
 findViewById(R.id.events).setAnimation(rotateInCirle);
 findViewById(R.id.fulldaybtn).setAnimation(rotateInCirle);
 findViewById(R.id.singleEvent).setAnimation(rotateInCirle);
 **/

        /**
         ViewAnimator viewAnimator = new ViewAnimator<>(ActionBarActivity.this,
         new ArrayList<Resourceble>(),
         (LinearLayout) findViewById(R.id.left_drawer),
         contentFragment, drawerLayout);
         //to open menu you have to override ActionBarDrawerToggle method
         @Override public void onDrawerSlide(View drawerView, float slideOffset) {
         super.onDrawerSlide(drawerView, slideOffset);
         if (slideOffset > 0.6 && viewAnimator.getLinearLayout().getChildCount() == 0)
         viewAnimator.showMenuContent();
         }
         public void onDrawerClosed(View view) {
         super.onDrawerClosed(view);
         viewAnimator.getLinearLayout().removeAllViews();
         viewAnimator.getLinearLayout().invalidate();
         }
         }
         @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
         callbackManager.onActivityResult(requestCode, resultCode, data);
         }
         private void onFacebookAccessTokenChange(AccessToken token) {
         if (token != null) {
         mRef.authWithOAuthToken("facebook", token.getToken(), new Firebase.AuthResultHandler() {
         @Override public void onAuthenticated(AuthData authData) {
         Intent main = new Intent(login.this, SingleEvent.class);
         startActivity(main);
         }
         @Override public void onAuthenticationError(FirebaseError firebaseError) {
         }
         });
         } else {
         Logged out of Facebook so do a logout from the Firebase app
         mRef.unauth();*/

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        startService(new Intent(this, GPSLocationService.class));
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


//    @Override
//    public void onStart() {
//        super.onStart();
//
//        // ATTENTION: This was auto-generated to implement the App Indexing API.
//        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        client.connect();
//        Action viewAction = Action.newAction(
//                Action.TYPE_VIEW, // TODO: choose an action type.
//                "login Page", // TODO: Define a title for the content shown.
//                // TODO: If you have web page content that matches this app activity's content,
//                // make sure this auto-generated web page URL is correct.
//                // Otherwise, set the URL to null.
//                Uri.parse("http://host/path"),
//                // TODO: Make sure this auto-generated app URL is correct.
//                Uri.parse("android-app://com.cecs492a_group4.sp/http/host/path")
//        );
//        AppIndex.AppIndexApi.start(client, viewAction);
//    }

//    @Override
//    public void onStop() {
//        super.onStop();
//
//        // ATTENTION: This was auto-generated to implement the App Indexing API.
//        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        Action viewAction = Action.newAction(
//                Action.TYPE_VIEW, // TODO: choose an action type.
//                "login Page", // TODO: Define a title for the content shown.
//                // TODO: If you have web page content that matches this app activity's content,
//                // make sure this auto-generated web page URL is correct.
//                // Otherwise, set the URL to null.
//                Uri.parse("http://host/path"),
//                // TODO: Make sure this auto-generated app URL is correct.
//                Uri.parse("android-app://com.cecs492a_group4.sp/http/host/path")
//        );
//        AppIndex.AppIndexApi.end(client, viewAction);
//        client.disconnect();
//    }

    @Override
    public void onClick(View v) {
        zoom(v);
        switch (v.getId() /*to get clicked view id**/) {
            case R.id.night:
                ImageView purple = (ImageView) findViewById(R.id.purple);
                purple.setVisibility(View.VISIBLE);
                zoom(purple);
                // do something when the corky is clicked
                break;

            case R.id.day:
                final Intent mainIntent = new Intent(MainMenu.this, MainActivity.class);
                MainMenu.this.startActivity(mainIntent);
                break;

            case R.id.single:
                System.out.println("Single Activity");
                final Intent singleResturant = new Intent(MainMenu.this, ResturantActivity.class);
                MainMenu.this.startActivity(singleResturant);
                break;

        }
    }
}