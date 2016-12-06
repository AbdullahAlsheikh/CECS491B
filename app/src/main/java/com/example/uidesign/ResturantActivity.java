package com.example.uidesign;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

import static com.example.uidesign.MainMenu.userId;


/**
 * Created by Abdullah on 10/3/16.
 */
public class ResturantActivity  extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private ProgressDialog loadingSpinner;

    private Context mContext;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    private static LinearLayout togetherlayout;

    private String consumerKey = "dudmo3ssHxvpUP_i_Lw60A";
    private String consumerSecret = "fOhwH5mUo_CyzX2D2vcDUc8FNw8";
    private String token = "yPhkb0u9cRxGE8ikWRkH3ceMCCpKYpQA";
    private String tokenSecret = "-WoZd39mwu4X9iVDXo5bxDNOBBU";
    private Yelp yelp = new Yelp(consumerKey, consumerSecret, token, tokenSecret);
    String breakfast;
    String lunch;
    String dinner, activity;

    private BussnessInfo single_activty_info = null;
    private int limit = 19;
    private String address = "California State University, Long Beach, Long Beach, CA";

    private  boolean startSingleActivite = true;

    private InterstitialAd mInterstitial;
    LoginButton facebookButton;
    CallbackManager callbackManager;
    private AccessToken accessToken;
    AccessTokenTracker accessTokenTracker;
    String[] fbInfo;
    NavigationView navView;
    View header;
    ProfilePictureView profilePictureView;
    TextView facebookName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resturant_activity_main);
        setTitle("Single Activity");
        togetherlayout = (LinearLayout) findViewById(R.id.resutrant_together);

        mContext = getApplicationContext();
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);

        accessToken = AccessToken.getCurrentAccessToken();
//        Interstitial Ad
        mInterstitial = new InterstitialAd(this);
        mInterstitial.setAdUnitId(getString(R.string.single_day_ad_unit_id));
        AdRequest request = new AdRequest.Builder().build();
        mInterstitial.loadAd(request);

//        yelp.setLimit(limit);

        breakfast = mSharedPreferences.getString("breakfastCat", "");
        lunch = mSharedPreferences.getString("lunchCat", "");
        dinner = mSharedPreferences.getString("dinnerCat", "");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        SwipeLayout swipeLayout = (SwipeLayout)findViewById(R.id.resutrant_activity);
        swipeLayout.setDragEdge(SwipeLayout.DragEdge.Bottom);
//
//        TextView bottominformation = (TextView) findViewById(R.id.single_filer_information);

        final PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.single_place_autocomplete_fragment);
        autocompleteFragment.setText(GPSLocationService.currentLocation);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.

                stopService(new Intent(ResturantActivity.this, GPSLocationService.class));

                GPSLocationService.currentLocation = (String) place.getName();

                autocompleteFragment.setText("GPSLocationService.currentLocation");

                Log.i(MainActivity.class.getName(), "Place: " + GPSLocationService.currentLocation);
            }

            @Override
            public void onError(Status status) {
            }
        });


        ImageButton autocompleteClear = (ImageButton) findViewById(R.id.place_autocomplete_clear_button);
        //mypart
        ImageButton autoCompleteSearch = (ImageButton) findViewById(R.id.place_autocomplete_search_button);

        autocompleteClear.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

                startService(new Intent(ResturantActivity.this, GPSLocationService.class));

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        autocompleteFragment.setText(GPSLocationService.currentLocation);

                    }
                }, 2000);

                autocompleteFragment.setText(GPSLocationService.currentLocation);

                Log.i(MainActivity.class.getName(), "Place: " + GPSLocationService.currentLocation);

            }
        });


        ImageButton call = (ImageButton) findViewById(R.id.callButton);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + single_activty_info.phone));
                try{
                    startActivity(callIntent);
                } catch (android.content.ActivityNotFoundException ex){
                    Toast.makeText(getApplicationContext(),"Call permission denied ",Toast.LENGTH_SHORT).show();
                }
            }

        });

        ImageButton nav = (ImageButton) findViewById(R.id.navButton);
        nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchAddre = single_activty_info.location.display_address.get(0) + " "+ single_activty_info.location.display_address.get(1);
                Log.i("test", "" + searchAddre);
                String map = "http://maps.google.co.in/maps?q=" + searchAddre;
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(map));
                startActivity(i);
            }
        });

        ImageButton web = (ImageButton) findViewById(R.id.webButton);
        web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(single_activty_info.mobile_url));
                startActivity(myIntent);
            }
        });





        ImageButton fullday = (ImageButton) findViewById(R.id.fullday);
        //assert fullday != null;




        fullday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Pressed Full Day", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                if(mInterstitial.isLoaded()){
                    mInterstitial.show();
                }
                if (isNetworkAvailable() && GPSLocationService.currentLocation != null) {

                    try {
                        new GeneratePlanTask().execute();


                    } catch (Exception e) {
                        System.out.println("full day set on click :Error -> " + e);
                        e.printStackTrace();
                    }
                }

            }
        });

        if(startSingleActivite && isNetworkAvailable() && GPSLocationService.currentLocation != null){

            new GeneratePlanTask().execute();
            startSingleActivite = false;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navView = (NavigationView) findViewById(R.id.nav_view);
        header = navView.getHeaderView(0);
        profilePictureView = (ProfilePictureView) header.findViewById(R.id.facebook_picture);
        facebookName = (TextView) header.findViewById(R.id.facebook_name);

        if(isLoggedIn()){

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



        }


        updateWithToken(accessToken);

        mContext = getApplicationContext();
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        mEditor = mSharedPreferences.edit();

        boolean singleTutorial = mSharedPreferences.getBoolean("singleToturial",false);
        if(!singleTutorial) {

            ShowcaseConfig config = new ShowcaseConfig();
            config.setDelay(500); // half second between each showcase view

            MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this);

            sequence.setConfig(config);

            sequence.addSequenceItem(autoCompleteSearch,
                    "Click and type in the desired address.", "GOT IT");

            sequence.addSequenceItem(autocompleteClear,
                    "The X button clears the entered address and activates the GPS.", "GOT IT");

            sequence.addSequenceItem(findViewById(R.id.fullday),
                    "The refresh button refreshes the search.", "GOT IT");

            sequence.addSequenceItem(findViewById(R.id.callButton),
                    "The call button pastes the phone number of the business into your phone app.", "GOT IT");

            sequence.addSequenceItem(findViewById(R.id.navButton),
                    "The navigation button inputs the business's address into google maps for directions.", "GOT IT");

            sequence.addSequenceItem(findViewById(R.id.webButton),
                    "The Yelp button opens the business's Yelp page from within our application.", "GOT IT");


            sequence.start();
            mEditor.putBoolean("singleToturial",true).commit();
            mEditor.apply();
        }

    }



    public synchronized void getYelpSearchResult(final int ran, final String term, final LinearLayout display) throws InterruptedException{
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                System.out.println("random:  " + ran +  " Term:" + term);
                try{

                    String response = yelp.searchByLocation(term, GPSLocationService.currentLocation);
//                    String response = yelp.searchByLocation(term, address);
                    System.out.println(response);
                    Gson gson = new GsonBuilder().create();

                    ResultInfo result = gson.fromJson(response, ResultInfo.class);
                    final BussnessInfo bussnessInfo = result.getBussnessInfo(ran);
                    single_activty_info = bussnessInfo;

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println(bussnessInfo.name + " " + bussnessInfo.snippet_text);
                            TextView title = (TextView) display.findViewById(R.id.activity_title);
                            title.setText(bussnessInfo.name);

                            TextView snippet_text = (TextView) display.findViewById(R.id.description);
                            snippet_text.setText(bussnessInfo.snippet_text);

                            TextView criteria = (TextView) display.findViewById(R.id.criteria);
                            criteria.setText(term.split(" ")[0]);

                            try {

                                ImageView icon = (ImageView) display.findViewById(R.id.activity_icon);
                                // Bitmap blurredIcon = BlurBuilder.blur(ResturantActivity.this, bussnessInfo.icon_img);

//                                icon.setImageBitmap(blurredIcon);
                                icon.setImageBitmap(bussnessInfo.icon_img);

                                icon.setScaleType(ScaleType.FIT_XY);


                                ImageView ratingImg = (ImageView) display.findViewById(R.id.rating);
                                ratingImg.setImageBitmap(bussnessInfo.rating_img);


                                TextView dealTitle = (TextView) display.findViewById(R.id.isDeal);
                                dealTitle.setText("No Deal");

                                //my part 11/11
                                if (bussnessInfo.deals.get(0) != null){
                                    dealTitle.setText(bussnessInfo.deals.get(0).title  + " *see Yelp for more details.");
                                }



                            } catch (Exception e) {
                                System.out.println("Image Error -> " + e);
                                e.printStackTrace();
                            }



                        }
                    });

                }catch (Exception e)
                {
                    System.out.println("GetYelpsearchRsult  Error -> "  + e);
                    e.printStackTrace();
                }
            }
        });

        thread.start();

        thread.join();

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.First) {
            System.out.println("First Button");
            final Intent mainIntent = new Intent(ResturantActivity.this, MainActivity.class);
            ResturantActivity.this.startActivity(mainIntent);
            finish();
        } else if (id == R.id.Second) {
            System.out.println("Already in Single Activiy");

        } else if (id == R.id.Third) {
            final Intent barHopping = new Intent(ResturantActivity.this, BarHoppingMode.class);
            ResturantActivity.this.startActivity(barHopping);
            finish();
            System.out.println("Third Button");


        } else if (id == R.id.Fourth) {
            final Intent events = new Intent(ResturantActivity.this, EventsActivity.class);
            ResturantActivity.this.startActivity(events);
            finish();
            System.out.println("Fourth Button");


        } else if (id == R.id.sectionOne) {
            System.out.println("First Section Button");
            final Intent mainIntent = new Intent(ResturantActivity.this, Setting_Page.class);
            ResturantActivity.this.startActivity(mainIntent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public class GeneratePlanTask extends AsyncTask<Void, Void, Void> {
        protected void onPreExecute() {
            //progressBarStatus = 0;

            //loadingSpinner = new ProgressDialog(senderView.getContext());
            loadingSpinner = new ProgressDialog(ResturantActivity.this);
            loadingSpinner.setCancelable(false);
            loadingSpinner.setMessage("Generating a plan ...");
            loadingSpinner.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            loadingSpinner.setProgress(0);
            loadingSpinner.setMax(100);
            loadingSpinner.show();

        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Calendar c = Calendar.getInstance();
                Log.e("Time HOD", "" + c.get(Calendar.HOUR_OF_DAY));

                String cri = "Restaurant";
                if(6 < c.get(Calendar.HOUR_OF_DAY) &&  c.get(Calendar.HOUR_OF_DAY)  <= 11){

                    cri = "Breakfast "+ breakfast ;
                    Log.e("Time", cri );
                }else if(11 < c.get(Calendar.HOUR_OF_DAY) && c.get(Calendar.HOUR_OF_DAY) <= 17){

                    cri = "Lunch " + lunch;
                    Log.e("Time", cri );
                }else{
                    cri = "Dinner " + dinner ;
                    Log.e("Time", cri );
                }

                Random r = new Random();
                int randNum = r.nextInt(limit);

                System.out.println("Random Number:  " + randNum + " limit: " + limit);
                getYelpSearchResult(randNum, cri, togetherlayout);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            //arrayAdapter.notifyDataSetChanged();
            loadingSpinner.dismiss();
        }
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


            profilePictureView.setProfileId("");
            profilePictureView.setPresetSize(ProfilePictureView.SMALL);
            facebookName.setText("User Name");

        }

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
}


//class BlurBuilder {
//    private static final float BITMAP_SCALE = 0.4f;
//    private static final float BLUR_RADIUS = 0.1f;
//
//    public static Bitmap blur(Context context, Bitmap image) {
//        int width = Math.round(image.getWidth() * BITMAP_SCALE);
//        int height = Math.round(image.getHeight() * BITMAP_SCALE);
//
//        Bitmap inputBitmap = Bitmap.createScaledBitmap(image, width, height, false);
//        Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);
//
//        RenderScript rs = RenderScript.create(context);
//        ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
//        Allocation tmpIn = Allocation.createFromBitmap(rs, inputBitmap);
//        Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);
//        theIntrinsic.setRadius(BLUR_RADIUS);
//        theIntrinsic.setInput(tmpIn);
//        theIntrinsic.forEach(tmpOut);
//        tmpOut.copyTo(outputBitmap);
//
//        return outputBitmap;
//    }
//}
