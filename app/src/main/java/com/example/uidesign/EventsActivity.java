package com.example.uidesign;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.LinearLayout;
import android.widget.TextView;

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

import java.io.InputStream;
import java.io.InterruptedIOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

import static com.example.uidesign.MainMenu.userId;


/**
 * Created by muhannad on 11/10/16.
 */
public class EventsActivity  extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private ProgressDialog loadingSpinner;
    private Context mContext;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    LinearLayout togetherlayout;
    TmResult result;
    boolean runAtStart = true;
    int previousIndex = 0;



    BussnessInfo single_activty_info = null;
    int limit;
    String address = "California State University, Long Beach, Long Beach, CA";
    String apiKey = "blIcAiLIvAxwsft0drWEHkHYCNmL6Gqu";
    int mIndex = 0;
    String city;
    String eventAddress, webURL;
    private InterstitialAd mInterstitial;

    LoginButton facebookButton;
    CallbackManager callbackManager;
    private AccessToken accessToken;
    AccessTokenTracker accessTokenTracker;
    String[] fbInfo;
    ProfilePictureView profilePictureView;
    NavigationView navView;
    View header;
    TextView facebookName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.events_activity_main);
        setTitle("Single Activity");
        togetherlayout = (LinearLayout) findViewById(R.id.resutrant_together);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        accessToken = AccessToken.getCurrentAccessToken();

//        Interstitial Ad
        mInterstitial = new InterstitialAd(this);
        mInterstitial.setAdUnitId(getString(R.string.events_page_ad_unit_id));
        AdRequest request = new AdRequest.Builder().build();
        mInterstitial.loadAd(request);

        //SwipeLayout swipeLayout = (SwipeLayout)findViewById(R.id.resutrant_activity);
        //swipeLayout.setDragEdge(SwipeLayout.DragEdge.Bottom);
//
//        TextView bottominformation = (TextView) findViewById(R.id.single_filer_information);

        final PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.single_place_autocomplete_fragment);
        autocompleteFragment.setText(GPSLocationService.currentLocation);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.

                stopService(new Intent(EventsActivity.this, GPSLocationService.class));

                GPSLocationService.currentLocation = (String) place.getAddress();


                String addressArray[] = GPSLocationService.currentLocation.split(", ");
                if (addressArray.length > 3) {
                    GPSLocationService.currentCity = addressArray[1];
                } else {
                    GPSLocationService.currentCity = addressArray[0];
                }

                System.out.println(GPSLocationService.currentCity);

                autocompleteFragment.setText("GPSLocationService.currentLocation");

                Log.i(MainActivity.class.getName(), "Place: " + GPSLocationService.currentLocation);
            }

            @Override
            public void onError(Status status) {

            }
        });


        ImageButton reloadButton = (ImageButton) findViewById(R.id.eventsReload);

        //ImageView next = (ImageView) findViewById(R.id.nextEvent);

        reloadButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mInterstitial.isLoaded()) {
                    mInterstitial.show();
                }
                if (isNetworkAvailable() && GPSLocationService.currentLocation != null) {
                    try {
                        new GeneratePlanTask().execute();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        if (runAtStart && isNetworkAvailable() && GPSLocationService.currentLocation != null) {
            try {
                new GeneratePlanTask().execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
            runAtStart = false;
        }


        ImageButton autocompleteClear = (ImageButton) findViewById(R.id.place_autocomplete_clear_button);
        //mypart
        ImageButton autoCompleteSearch = (ImageButton) findViewById(R.id.place_autocomplete_search_button);

        autocompleteClear.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

                startService(new Intent(EventsActivity.this, GPSLocationService.class));

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


//      bottominformation.setText("Address:\n " + address + "\nlimit:" + limit + "\n");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        ImageButton nav = (ImageButton) findViewById(R.id.navButton);
        nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchAddre = eventAddress;
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
                Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(webURL));
                startActivity(myIntent);
            }
        });


        navView = (NavigationView) findViewById(R.id.nav_view);
        header = navView.getHeaderView(0);
        profilePictureView = (ProfilePictureView) header.findViewById(R.id.facebook_picture);
        facebookName = (TextView) header.findViewById(R.id.facebook_name);
        if (isLoggedIn()) {

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

        updateWithToken(accessToken); mContext = getApplicationContext();
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


            sequence.addSequenceItem(findViewById(R.id.eventsReload),
                    "The refresh button refreshes the search.", "GOT IT");

            sequence.addSequenceItem(findViewById(R.id.navButton),
                    "The navigation button inputs the business's address into google maps for directions.", "GOT IT");

            sequence.addSequenceItem(findViewById(R.id.webButton),
                    "The I button opens the event listing on ticketmaster.com.", "GOT IT");


            sequence.start();
            mEditor.putBoolean("singleToturial", true).commit();
            mEditor.apply();
        }

    }







//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            System.out.println("Pressed Settings");
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    public String fixCity(String city){
        String newCity = city.replaceAll("\\s","+");
        System.out.println(newCity);
        return newCity;
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

    public synchronized void getEvent() throws InterruptedIOException{

        //Ticket Master Work

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try  {
                    //how can I get the city from the user location or google places??? ask!
                      city = fixCity(GPSLocationService.currentCity);
                    //city = fixCity("Los Angeles");

                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                    Date date1 = new Date();
                    String date = dateFormat.format(date1);
                    String urlString = "https://app.ticketmaster.com/discovery/v2/events.json?apikey="+apiKey+"&localDate="+date+"&city="+city;
                    URL url = new URL(urlString);
                    HttpRequest a = new HttpRequest(url);
                    String response2 = a.sendAndReadString();


                    //System.out.println(response2);
                    Gson gson = new GsonBuilder().create();
                    result = gson.fromJson(response2, TmResult.class);
                    limit = result.getSizeOfResult();
                    Random randIndex = new Random();
                    int eventIndex = randIndex.nextInt(limit);
                    while(eventIndex == previousIndex) {
                        eventIndex = randIndex.nextInt(limit);
                        System.out.println("random index = " + eventIndex);
                    }
                    previousIndex = eventIndex;

                    System.out.println("random index = " + eventIndex);

                    System.out.println("SIZE = " + limit);
                    getNextEvent(eventIndex);

                    System.out.println("-----------------------BREAK---------------------------------");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });
        thread.start();

    }
    public void getNextEvent(int index) {
        final int in = index;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                System.out.println("---------START OF EVENT NUMBER " + in + "---------");
                final basicInfo resultInfo = result.getevent(in);
                eventAddress = resultInfo._embedded.venues.get(0).address.line1 + " "
                        + resultInfo._embedded.venues.get(0).city.name + " "
                        + resultInfo._embedded.venues.get(0).state.stateCode + " "
                        + resultInfo._embedded.venues.get(0).postalCode;
                webURL = resultInfo.url;
                System.out.println("Event Name: " + resultInfo.name);
                System.out.println("Event URL: " + resultInfo.url);
//        System.out.println("Event Type: "+ resultInfo.type);
//        System.out.println("Event Image: " + resultInfo.images.get(0).url);
//        System.out.println("Name of venue: " + resultInfo._embedded.venues.get(0).name);
                System.out.println("Postal Code of Venue: " + resultInfo._embedded.venues.get(0).postalCode);
                System.out.println("City: " + resultInfo._embedded.venues.get(0).city.name);
                System.out.println("State: " + resultInfo._embedded.venues.get(0).state.stateCode);
                System.out.println("Address: " + resultInfo._embedded.venues.get(0).address.line1);
                System.out.println("Start Date: " + resultInfo.dates.start.localDate);
                System.out.println("Start Time: " + resultInfo.dates.start.localTime);
//        System.out.println("Time Zone: " + resultInfo.dates.timezone);
                TextView name = (TextView) findViewById(R.id.event_name);
                TextView venue = (TextView) findViewById(R.id.Venue);
                //TextView address = (TextView) findViewById(R.id.Address);
//        TextView city = (TextView) findViewById(R.id.City);
//        TextView state = (TextView) findViewById(R.id.State);
//        TextView postalCode = (TextView) findViewById(R.id.PostalCode);

            TextView startTime = (TextView) findViewById(R.id.startTime);
            TextView startDate = (TextView) findViewById(R.id.startDate);

                name.setText("Event: " + resultInfo.name);
                venue.setText("Venue: " + resultInfo._embedded.venues.get(0).name);
                System.out.println("Venue : " + resultInfo._embedded.venues.get(0).name);
//        address.setText("Address: "+resultInfo._embedded.venues.get(0).address.line1);
//        city.setText("City: "+resultInfo._embedded.venues.get(0).city.name);
//        state.setText("State: "+resultInfo._embedded.venues.get(0).state.stateCode);
//        postalCode.setText("Zip Code: "+resultInfo._embedded.venues.get(0).postalCode);
                String startDateString = "";
                String startTimeString = "";
                if(resultInfo.dates.start.localDate == null){
                    startDateString = " No Data Avaliable";
                }else {
                    startDateString = resultInfo.dates.start.localDate;
                }

                if(resultInfo.dates.start.localTime == null){
                    startTimeString = " No Data Avaliable";
                }else {
                    startTimeString = resultInfo.dates.start.localTime;
                }

                startDate.setText("Start Date Of Event: "+startDateString);
                startTime.setText("Start Time Of Event: "+startTimeString);
//
                new DownloadImageTask((ImageView) findViewById(R.id.eventsImage))
                        .execute(resultInfo.images.get(0).url);
            }
        });
    }



    public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.First) {
            System.out.println("First Button");
            final Intent mainIntent = new Intent(EventsActivity.this, MainActivity.class);
            EventsActivity.this.startActivity(mainIntent);
            finish();
        } else if (id == R.id.Second) {
            final Intent restaurant = new Intent(EventsActivity.this, ResturantActivity.class);
            EventsActivity.this.startActivity(restaurant);
            finish();


        } else if (id == R.id.Third) {
            final Intent bar = new Intent(EventsActivity.this, BarHoppingMode.class);
            EventsActivity.this.startActivity(bar);
            finish();
            System.out.println("Third Button");


        } else if (id == R.id.Fourth) {
            System.out.println("Fourth Button");


        } else if (id == R.id.sectionOne) {
            System.out.println("First Section Button");
            final Intent mainIntent = new Intent(EventsActivity.this, Setting_Page.class);
            EventsActivity.this.startActivity(mainIntent);
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
            loadingSpinner = new ProgressDialog(EventsActivity.this);
            loadingSpinner.setCancelable(false);
            loadingSpinner.setMessage("Generating a plan ...");
            loadingSpinner.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            loadingSpinner.setProgress(0);
            loadingSpinner.setMax(100);
            loadingSpinner.show();

        }

        @Override
        protected Void doInBackground(Void... params) {
            try{
                getEvent();
                Thread.sleep(1000);
            } catch (InterruptedIOException e ){
                e.printStackTrace();
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

}

