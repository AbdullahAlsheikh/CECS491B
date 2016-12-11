package com.example.uidesign;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;

import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import static com.example.uidesign.MainMenu.userId;

/**
 * Created by Abdullah on 10/6/16.
 */
public class Setting_Page  extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Context mContext;

    Button breakPref, lunchPref, dinnerPref, activityPref;
    Button next;
    private SharedPreferences mSharedPreferences;
    SeekBar seekBar, radiusBar;
    TextView limitText, radiusText;
    private SharedPreferences.Editor mEditor;

    LoginButton facebookButton;
    CallbackManager callbackManager;
    private AccessToken accessToken;
    AccessTokenTracker accessTokenTracker;
    String[] fbInfo;
    private NavigationView navView;
    private View header;
    ProfilePictureView profilePictureView;
    TextView facebookName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_activity_main);


        fbInfo = new String[2];


        callbackManager = CallbackManager.Factory.create();

        facebookButton = (LoginButton) findViewById(R.id.button_facebook_login);
        accessToken = AccessToken.getCurrentAccessToken();
        navView = (NavigationView) findViewById(R.id.nav_view);
        header = navView.getHeaderView(0);
        profilePictureView = (ProfilePictureView) header.findViewById(R.id.facebook_picture);
        facebookName = (TextView) header.findViewById(R.id.facebook_name);

        if(isNetworkAvailable()) {
            LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {


                @Override
                public void onSuccess(LoginResult loginResult) {

                    //accessToken之後或許還會用到 先存起來
                    accessToken = loginResult.getAccessToken();
                    if (isNetworkAvailable()) {
                        try {

                            fbInfo = getFacebookInfo();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                    }
                    Log.d("FB", "loading image with test " + fbInfo[1]);
                    loadImage(fbInfo[1]);
                    facebookName.setText(fbInfo[0]);

                }

                //登入取消
                @Override
                public void onCancel() {
                    // App code
                    Log.d("FB", "CANCEL");
                }

                //登入失敗
                @Override
                public void onError(FacebookException exception) {
                    // App code
                    Log.d("FB", exception.toString());
                }


            });
        }
        //always update
        if(isNetworkAvailable()) {
            updateWithToken(accessToken);
        }
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken newAccessToken) {
                updateWithToken(newAccessToken);
            }
        };

        if(isLoggedIn() && isNetworkAvailable()){

            try {
                fbInfo = getFacebookInfo();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            Log.d("FB", "loading image with test " + fbInfo[1]);
            loadImage(fbInfo[1]);
            facebookName.setText(fbInfo[0]);


            //loadImage(fbInfo[1]);
        }


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);




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
    }

    public void loadImage(String id) {
        profilePictureView.setProfileId(id);
        profilePictureView.setPresetSize(ProfilePictureView.SMALL);
    }

    private void updateWithToken(AccessToken currentAccessToken) {

        if (currentAccessToken != null) {

            try {
                fbInfo = getFacebookInfo();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            Log.d("FB", "loading image with test " + fbInfo[1]);
            loadImage(fbInfo[1]);
            facebookName.setText(fbInfo[0]);
            

        }else{

            Log.d("FB", "no logged in user");

            navView = (NavigationView) findViewById(R.id.nav_view);
            header = navView.getHeaderView(0);
            profilePictureView = (ProfilePictureView) header.findViewById(R.id.facebook_picture);
            profilePictureView.setProfileId("");
            profilePictureView.setPresetSize(ProfilePictureView.SMALL);
            facebookName.setText("User Name");

        }

    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }


    @Override
    public void onBackPressed() {
        checkPref();
        menuSave();
        super.onBackPressed();
    }

    public void menuSave() {
        checkPref();
        String a = mSharedPreferences.getString("lunchCat", "");
        System.out.println("String from preferences for lunch: "+ a);
        String a2 = mSharedPreferences.getString("dinnerCat", "");
        System.out.println("String from preferences for dinner: "+ a2);
        String a3 = mSharedPreferences.getString("breakfastCat", "");
        System.out.println("String from preferences for break fast: "+ a3);
        String a4 = mSharedPreferences.getString("activityCat", "");
        System.out.println("String from preferences for activity: "+ a4);
    }

    private void checkPref(){

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(Setting_Page.this);
        mEditor = mSharedPreferences.edit();
        String[] breakfast = {"DelisB","AmericanB","SandwichesB", "Coffee and TeaB", "BakeryB", "SaladB", "DealsB"};

        String[] lunch = {"ChineseL","IndianL","MexicanL","AmericanL", "ItalianL", "JapaneseL", "KoreanL",
                "CombodianL", "VietnameseL", "GreekL", "MediterraneanL", "Sea FoodL", "DealsL"};

        String[] dinner = {"ChineseD","IndianD","MexicanD","AmericanD", "ItalianD", "JapaneseD", "KoreanD",
                "CombodianD", "VietnameseD", "GreekD", "MediterraneanD", "Sea FoodD", "DealsD"};

        String[] activity = {"MuseumA","AquariumA","ParkA","Amusement ParkA", "MallA", "Movie TheatreA", "Skate ParkA",
                "Skating RinkA", "BeachA", "SkiingA", "SnowboardingA","DealsA"};



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

    @SuppressWarnings("StatementWithEmptyBody")
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        menuSave();
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
            final Intent bar = new Intent(Setting_Page.this, BarHoppingMode.class);
            Setting_Page.this.startActivity(bar);
            finish();
            System.out.println("Third Button");


        } else if (id == R.id.Fourth) {

            final Intent event = new Intent(Setting_Page.this, EventsActivity.class);
            Setting_Page.this.startActivity(event);
            finish();
            System.out.println("Fourth Button");


        } else if (id == R.id.sectionOne) {

            System.out.println("Already in setting");

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public boolean isNetworkAvailable() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }


    public boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }

    public static String[] getFacebookInfo() throws InterruptedException, ExecutionException {
        final String[] info = new String[2];



        // Run facebook graphRequest.
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                GraphRequest request = GraphRequest.newMeRequest(
                        AccessToken.getCurrentAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {

                            //當RESPONSE回來的時候
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                userId = object.optString("id");
                                //讀出姓名 ID FB個人頁面連結
                                Log.d("FB", "complete");
                                Log.d("FB", object.optString("name"));
                                Log.d("FB", object.optString("link"));
                                Log.d("FB", userId);
                                info[0] = object.optString("name");
                                info[1] = object.optString("id");


                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,link");
                request.setParameters(parameters);
                request.executeAndWait();

            }
        });
        t.start();
        t.join();
        return info;
    }
}

