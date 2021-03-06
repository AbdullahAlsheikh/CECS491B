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
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.google.android.gms.ads.AdView;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;
import uk.co.deanwild.materialshowcaseview.shape.RectangleShape;

import static com.example.uidesign.MainMenu.userId;

public class BarHoppingMode extends  AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    //Data loading Varables
    private ProgressDialog loadingSpinner;
    private String planExcuteText = "";
    private boolean firstPlanActivite = true;

    private Context mContext;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;


    //String Data for each activiy
    String breakfast;
    String lunch;
    String dinner;


    //Yelp server Kays
    String businessName;
    private String consumerKey = "dudmo3ssHxvpUP_i_Lw60A";
    private String consumerSecret = "fOhwH5mUo_CyzX2D2vcDUc8FNw8";
    private String token = "yPhkb0u9cRxGE8ikWRkH3ceMCCpKYpQA";
    private String tokenSecret = "-WoZd39mwu4X9iVDXo5bxDNOBBU";
    Yelp yelp = new Yelp(consumerKey, consumerSecret, token, tokenSecret);


    //Varables for SwipLayout display
    SwipeLayout list_cube0 = null;
    SwipeLayout list_cube = null;
    SwipeLayout list_cube2 = null;
    SwipeLayout list_cube3 = null;
    SwipeLayout list_cube4 = null;

    //Data Relating to store Yelp's Bussiness data
    BussnessInfo[] cube_info = new BussnessInfo[5];
    int refreshIndex = 0;
    int limit = 19;
    double radius = yelp.getraduis();
    String address = "California State University, Long Beach, CA";
    SwipeLayout swipeLayout;
    Toolbar toolbar;
    LinearLayout listCubes;


    //Data for FaceBook data withtin the side header
    String[] business;
    LoginButton facebookButton;
    CallbackManager callbackManager;
    private AccessToken accessToken;
    AccessTokenTracker accessTokenTracker;
    String[] fbInfo;
    NavigationView navView;
    View header;
    TextView facebookName;
    ProfilePictureView profilePictureView;


    /**
     *  Main onClick method
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = getApplicationContext();
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        accessToken = AccessToken.getCurrentAccessToken();

        limit = 19;
        radius = (double)mSharedPreferences.getInt("radius", 0);
        yelp.setRadius(radius);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        //initialize business names array
        business = new String[5];


        //Changing back ground color
        toolbar.setBackgroundColor(getResources().getColor(R.color.DarkPurple));
        swipeLayout = (SwipeLayout)findViewById(R.id.content_main);
        swipeLayout.setDragEdge(SwipeLayout.DragEdge.Bottom);
        swipeLayout.setBackgroundColor(getResources().getColor(R.color.Purple));
        listCubes = (LinearLayout) findViewById(R.id.listCubes);



        //
        //Getting Data from Phone's Navigation
        //
        final PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setText(GPSLocationService.currentLocation);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.

                stopService(new Intent(BarHoppingMode.this, GPSLocationService.class));

                GPSLocationService.currentLocation = (String) place.getAddress();

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

                startService(new Intent(BarHoppingMode.this, GPSLocationService.class));

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        autocompleteFragment.setText(GPSLocationService.currentLocation);

                    }
                }, 2500);

                autocompleteFragment.setText(GPSLocationService.currentLocation);

                Log.i(MainActivity.class.getName(), "Place: " + GPSLocationService.currentLocation);

            }
        });//End of navigation



        //Initalizing The swipeLayout cubes

        //initalize each cube with it's layout counterpart

        list_cube0 = (SwipeLayout) findViewById(R.id.first_cube);
        list_cube0.setShowMode(SwipeLayout.ShowMode.PullOut);
        //Setting Drag to each layout
        //Left swipe
        list_cube0.addDrag(SwipeLayout.DragEdge.Left, list_cube0.findViewById(R.id.leftside));
        //Right swipe
        list_cube0.addDrag(SwipeLayout.DragEdge.Right, list_cube0.findViewById(R.id.rightside));
        PressedAction(list_cube0, 0);


        //applied throughout the rest of the swipeLayouts

        list_cube = (SwipeLayout) findViewById(R.id.second_cube);
        list_cube.setShowMode(SwipeLayout.ShowMode.PullOut);

        list_cube.addDrag(SwipeLayout.DragEdge.Left, list_cube.findViewById(R.id.leftside));
        list_cube.addDrag(SwipeLayout.DragEdge.Right, list_cube.findViewById(R.id.rightside));
        PressedAction(list_cube, 1);


        list_cube2 = (SwipeLayout) findViewById(R.id.thrid_cube);
        list_cube2.setShowMode(SwipeLayout.ShowMode.PullOut);

        list_cube2.addDrag(SwipeLayout.DragEdge.Left, list_cube2.findViewById(R.id.leftside));
        list_cube2.addDrag(SwipeLayout.DragEdge.Right, list_cube2.findViewById(R.id.rightside));
        PressedAction(list_cube2,2);


        list_cube3 = (SwipeLayout) findViewById(R.id.fourth_cube);
        list_cube3.setShowMode(SwipeLayout.ShowMode.PullOut);

        list_cube3.addDrag(SwipeLayout.DragEdge.Left, list_cube3.findViewById(R.id.leftside));
        list_cube3.addDrag(SwipeLayout.DragEdge.Right, list_cube3.findViewById(R.id.rightside));
        PressedAction(list_cube3,3 );


        list_cube4 = (SwipeLayout) findViewById(R.id.fifth_cube);
        list_cube4.setShowMode(SwipeLayout.ShowMode.PullOut);

        list_cube4.addDrag(SwipeLayout.DragEdge.Left, list_cube4.findViewById(R.id.leftside));
        list_cube4.addDrag(SwipeLayout.DragEdge.Right, list_cube4.findViewById(R.id.rightside));
        PressedAction(list_cube4,4);


        FloatingActionButton fullday = (FloatingActionButton) findViewById(R.id.fullday);
        assert fullday != null;



        //Chaning background color
        LinearLayout lyout_cube0 = (LinearLayout) list_cube0.findViewById(R.id.cubeInterface);
        lyout_cube0.setBackgroundColor(getResources().getColor(R.color.DarkPurple));

        LinearLayout lyout_cube1 = (LinearLayout) list_cube.findViewById(R.id.cubeInterface);
        lyout_cube1 .setBackgroundColor(getResources().getColor(R.color.DarkPurple));

        LinearLayout lyout_cube2 = (LinearLayout) list_cube2.findViewById(R.id.cubeInterface);
        lyout_cube2 .setBackgroundColor(getResources().getColor(R.color.DarkPurple));

        LinearLayout lyout_cube3 = (LinearLayout) list_cube3.findViewById(R.id.cubeInterface);
        lyout_cube3.setBackgroundColor(getResources().getColor(R.color.DarkPurple));

        LinearLayout lyout_cube4 = (LinearLayout) list_cube4.findViewById(R.id.cubeInterface);
        lyout_cube4.setBackgroundColor(getResources().getColor(R.color.DarkPurple));


        //If the user just entered the page it would activate
        if(firstPlanActivite && isNetworkAvailable() && GPSLocationService.currentLocation != null){
            planExcuteText = "Loading Data";
            new GeneratePlanTask().execute();
            firstPlanActivite = false;
        }

        fullday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Pressed Full Day", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                //If the network and the gps location is activie
                if (isNetworkAvailable() && GPSLocationService.currentLocation != null) {

                    try {
                        //Making each cube visible
                        list_cube0.setVisibility(list_cube0.VISIBLE);
                        list_cube.setVisibility(list_cube.VISIBLE);
                        list_cube2.setVisibility(list_cube2.VISIBLE);
                        list_cube3.setVisibility(list_cube3.VISIBLE);
                        list_cube4.setVisibility(list_cube4.VISIBLE);
                        //Set the loading screen text
                        planExcuteText = "Refreshing Entire Plan";
                        //Send request Yelp request
                        new GeneratePlanTask().execute();

                    } catch (Exception e) {
                        System.out.println("full day set on click :Error -> " + e);
                        e.printStackTrace();
                    }

                }

            }
        });




        //Getting side menu Data for facebook login
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Checks if Facebook is logged in
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

        //Tutorial Data
        updateWithToken(accessToken);
        updateWithToken(accessToken);
        mContext = getApplicationContext();
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        mEditor = mSharedPreferences.edit();

        boolean fullToturial = mSharedPreferences.getBoolean("fullToturial",false);
        if(!fullToturial) {

            ShowcaseConfig config = new ShowcaseConfig();
            config.setDelay(500); // half second between each showcase view

            MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this);

            sequence.setConfig(config);

            sequence.addSequenceItem(autoCompleteSearch,
                    "Click and type in the desired address.", "GOT IT");

            sequence.addSequenceItem(autocompleteClear,
                    "The X button clears the entered address and activates the GPS.", "GOT IT");

            sequence.addSequenceItem(fullday,
                    "This floating button refreshes the search.", "GOT IT");

            config.setShape(new RectangleShape(10, 10));

            sequence.addSequenceItem(list_cube0,
                    "The cube has different options dependent on the gestures used.\n\n" +
                            "Long Press: Open Yelp\n" +
                            "Swipe Left: Refresh, Navigate, Call\n" +
                            "Swipe Right: Delete\n", "GOT IT");

            sequence.start();
            mEditor.putBoolean("fullToturial",true).commit();
            mEditor.apply();

        }
    }

    /**
     * When pressing any of the activities, here to process
     * @param display
     */
    public void PressedAction(final SwipeLayout display,final int index ) {

        display.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                //Closing a cube if another cube is opened
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    switch (index) {
                        case 0:
                            list_cube.close();
                            list_cube2.close();
                            list_cube3.close();
                            list_cube4.close();
                            break;
                        case 1:
                            list_cube0.close();
                            list_cube2.close();
                            list_cube3.close();
                            list_cube4.close();
                            break;
                        case 2:
                            list_cube0.close();
                            list_cube.close();
                            list_cube3.close();
                            list_cube4.close();
                            break;
                        case 3:
                            list_cube0.close();
                            list_cube2.close();
                            list_cube.close();
                            list_cube4.close();
                            break;
                        case 4:
                            list_cube0.close();
                            list_cube2.close();
                            list_cube3.close();
                            list_cube.close();
                            break;

                    }
                }
                return false;
            }
        });



        display.getSurfaceView().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //LongCLick For Yelp Site View
                try {
                    Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(cube_info[index].mobile_url));
                    startActivity(myIntent);
                } catch (Exception e) {
                    System.out.println("Been in on click method");
                }
                return true;
            }
        });

        display.findViewById(R.id.star2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Show Navigation
                String searchAddre = cube_info[index].location.display_address.get(0) + " "+ cube_info[index].location.display_address.get(1);
                Log.i("test", "" +  searchAddre);
                String map = "http://maps.google.co.in/maps?q=" + searchAddre;
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(map));
                display.close();
                startActivity(i);

            }
        });

        display.findViewById(R.id.trash2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Refresh Screen
                refreshIndex = index;
                display.close();
                new RefreshTask().execute(display);
            }
        });

        display.findViewById(R.id.magnifier2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Call event
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + cube_info[index].phone));
                display.close();
                try{
                    startActivity(callIntent);
                } catch (android.content.ActivityNotFoundException ex){
                    Toast.makeText(getApplicationContext(),"Call permission denied ",Toast.LENGTH_SHORT).show();
                }
            }
        });

        display.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Delete Event
                display.setVisibility(SwipeLayout.INVISIBLE);
                display.close();

            }
        });

    }


    /**
     * Request and receive yelp data
     * @param ran
     * @param index
     * @param term
     * @param display
     * @throws InterruptedException
     */
    public synchronized void getYelpSearchResult(final int ran, final int index ,final String term, final SwipeLayout display) throws InterruptedException{

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                System.out.println("Index:  " + ran +  " Term:" + term);
                try{


                    String response = yelp.searchByLocation(term, address);
//                    String response = yelp.searchByLocation(term, GPSLocationService.currentLocation);
                    System.out.println(response);
                    Gson gson = new GsonBuilder().create();

                    ResultInfo result = gson.fromJson(response, ResultInfo.class);
                    final BussnessInfo bussnessInfo = result.getBussnessInfo(ran);
                    cube_info[index] = bussnessInfo;

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                System.out.println(bussnessInfo.name);
                                TextView title = (TextView) display.findViewById(R.id.activity_title);
                                title.setText(bussnessInfo.name);

                                try{
                                    //Processing Main Bussness Image
                                    ImageView icon = (ImageView) display.findViewById(R.id.activity_icon);
                                    icon.setImageBitmap(bussnessInfo.icon_img);
                                    //Processing Rating
                                    ImageView ratingImg = (ImageView) display.findViewById(R.id.rating_imag);
                                    ratingImg.setImageBitmap(bussnessInfo.rating_img);
                                }catch (Exception img){
                                    System.out.println("Thread Error -> " + img);
                                    img.printStackTrace();
                                }

                                businessName = bussnessInfo.name;

                            } catch (Exception e) {
                                System.out.println("Thread Error -> " + e);
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


    /**
     * in the event an Back pressed
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            super.onBackPressed();

        }
    }

    /**
     * Slide menu options selection
     * @param item
     * @return
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.First) {
            //Sending user to Full day
            final Intent fullDay = new Intent(BarHoppingMode.this, MainActivity.class);
            BarHoppingMode.this.startActivity(fullDay);
            finish();


        } else if (id == R.id.Second) {
            //sending user to single resturant
            final Intent singleResturant = new Intent(BarHoppingMode.this, ResturantActivity.class);
            BarHoppingMode.this.startActivity(singleResturant);
            finish();

        } else if (id == R.id.Third) {
            //Same activity

        } else if (id == R.id.Fourth) {
            //sending user to event
            final Intent events = new Intent(BarHoppingMode.this, EventsActivity.class);
            BarHoppingMode.this.startActivity(events);
            finish();


        } else if (id == R.id.sectionOne) {
            //sending user to settings menu
            final Intent mainIntent = new Intent(BarHoppingMode.this, Setting_Page.class);
            BarHoppingMode.this.startActivity(mainIntent);
            finish();

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * checking if user has network
     * @return
     */
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

    /**
     * Loading Page to generating bars
     */
    public class GeneratePlanTask extends AsyncTask<Void, Void, Void> {
        protected void onPreExecute() {
            //loading spinner
            loadingSpinner = new ProgressDialog(BarHoppingMode.this);
            loadingSpinner.setCancelable(false);
            loadingSpinner.setMessage(planExcuteText + " ...");
            loadingSpinner.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            loadingSpinner.setProgress(0);
            loadingSpinner.setMax(100);
            loadingSpinner.show();

        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                //Sending request to yelp
                ArrayList<String> businesses = new ArrayList<>(4);
                Random r = new Random();

                int randNum = r.nextInt(limit);


                System.out.println("Random Number:  " + randNum + " limit: " + limit + " Catagory:" + breakfast);
                Log.i("Test", "Random Number:  " + randNum + " limit: " + limit + " Catagory:" + dinner);
                getYelpSearchResult(randNum, 0, "Bar Club NightClub Lounge", list_cube0);
                //add to business array
                Thread.sleep(100);


                business[0] = businessName;
                //businesses.add(0,businessName);

                Log.i("Var", "BussnessName: " + businessName + " Ran" + randNum);


                System.out.println("Random Number:  " + randNum + " limit: " + limit);
                Log.i("Test", "Random Number:  " + randNum + " limit: " + limit + " Catagory:" + dinner);
                do {
                    randNum = r.nextInt(limit);
                    getYelpSearchResult(randNum, 1, "Bar Club NightClub Lounge", list_cube);
                    Thread.sleep(100);
                    Log.i("Var", "BussnessName: " + businessName + " Ran" + randNum);
                }while (contains(business,businessName));
                business[1] = businessName;
                //businesses.add(1,businessName);


                System.out.println("Random Number:  " + randNum + " limit: " + limit + " Catagory:" + lunch);
                Log.i("Test", "Random Number:  " + randNum + " limit: " + limit + " Catagory:" + dinner);
                do {
                    randNum = r.nextInt(limit);
                    getYelpSearchResult(randNum, 2, "Bar Club NightClub Lounge", list_cube2);
                    Thread.sleep(100);
                    Log.i("Var", "BussnessName: " + businessName + " Ran" + randNum);
                }while(contains(business,businessName));
                business[2] = businessName;
                //businesses.add(2,businessName);




                System.out.println("Random Number:  " + randNum + " limit: " + limit);
                Log.i("Test", "Random Number:  " + randNum + " limit: " + limit + " Catagory:" + dinner);
                do{
                    randNum = r.nextInt(limit);
                    getYelpSearchResult(randNum, 3, "Bar Club NightClub Lounge", list_cube3);
                    Thread.sleep(100);
                    Log.i("Var", "BussnessName: " + businessName + " Ran" + randNum);
                }while(contains(business,businessName));
                business[3] = businessName;
                //businesses.add(3,businessName);




                System.out.println("Random Number:  " + randNum + " limit: " + limit + " Catagory:" + dinner);
                Log.i("Test", "Random Number:  " + randNum + " limit: " + limit + " Catagory:" + dinner);
                do{
                    randNum = r.nextInt(limit);
                    getYelpSearchResult(randNum, 4, "Bar Club NightClub Lounge", list_cube4);
                    Thread.sleep(100);
                    Log.i("Var", "BussnessName: " + businessName + " Ran" + randNum);
                }while(contains(business,businessName));
                business[4] = businessName;
                //businesses.add(4,businessName);


               // businesses.clear();

                for (String s : business){
                    //Log.i("XXX",business[i]);
                    System.out.println("businessNameWr: " + s);
                    //businesses.remove(i);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }



            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            //Ending request
            loadingSpinner.dismiss();
        }
    }

    public static boolean contains(String[] arr, String targetValue) {
        Set<String> set = new HashSet<String>(Arrays.asList(arr));
        return set.contains(targetValue);
    }


    /**
     * Refresh for one event
     */
    public class RefreshTask extends AsyncTask<SwipeLayout, Void, Void> {

        protected void onPreExecute() {
            //spinner start
            loadingSpinner = new ProgressDialog(BarHoppingMode.this);
            loadingSpinner.setCancelable(false);
            loadingSpinner.setMessage("Refreshing Event ...");
            loadingSpinner.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            loadingSpinner.setProgress(0);
            loadingSpinner.setMax(100);
            loadingSpinner.show();

        }

        @Override
        protected Void doInBackground(SwipeLayout... params) {
            //Sending request for yelp
            SwipeLayout r = params[0];
            try {

                Random random = new Random();

                do {
                    int randNum = random.nextInt(limit);
                    System.out.println("Random Number:  " + randNum + " limit: " + limit + " Catagory:" + breakfast);
                    getYelpSearchResult(randNum, refreshIndex, "Bar Club NightClub Lounge", r);
                    Thread.sleep(100);
                }while (contains(business,businessName));
                    business[refreshIndex] = businessName;
                    for (String s : business){
                        //Log.i("XXX",business[i]);
                        System.out.println("businessNameWr: " + s);
                        //businesses.remove(i);
                }

                    //TODO: add an array with the bussiness instead of arraylist,


            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }



        @Override
        protected void onPostExecute(Void aVoid)
        {
            //Ending request
            loadingSpinner.dismiss();
        }

    }

    /**
     * Checking if the user is logged in
     * @return
     */
    public boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }

    /**
     * get Facebook information
     * @return
     * @throws InterruptedException
     * @throws ExecutionException
     */

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


    /**
     * Load facebook image
     * @param id
     */
    public void loadImage(String id) {

        profilePictureView.setProfileId(id);
        profilePictureView.setPresetSize(ProfilePictureView.SMALL);
    }

    /**
     * update FaceBook token
     * @param currentAccessToken
     */
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





