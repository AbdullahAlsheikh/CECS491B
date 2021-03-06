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
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareButton;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;
import uk.co.deanwild.materialshowcaseview.shape.RectangleShape;

import static com.example.uidesign.BarHoppingMode.contains;
import static com.example.uidesign.MainMenu.userId;

public class MainActivity extends  AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    //Data loading Varables
    private ProgressDialog loadingSpinner;
    private String planExcuteText = "";
    private boolean firstPlanActivite = true;


    //String Data for each activiy
    String breakfast;
    String lunch;
    String dinner, activity;


    //Data Relating to store Yelp's Bussiness data
    String businessName;
    String[] business;
    ShareLinkContent content;
    BussnessInfo[] cube_info = new BussnessInfo[5];
    int refreshIndex = 0;
    int limit = 19;

    String address = "California State University, Long Beach, CA";

    //Data from setting's prefernces
    private Context mContext;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    //Yelp server Kays
    private String consumerKey = "dudmo3ssHxvpUP_i_Lw60A";
    private String consumerSecret = "fOhwH5mUo_CyzX2D2vcDUc8FNw8";
    private String token = "yPhkb0u9cRxGE8ikWRkH3ceMCCpKYpQA";
    private String tokenSecret = "-WoZd39mwu4X9iVDXo5bxDNOBBU";
    Yelp yelp = new Yelp(consumerKey, consumerSecret, token, tokenSecret);
    double radius = yelp.getraduis();

    //Varables for SwipLayout display
    SwipeLayout list_cube0 = null;
    SwipeLayout list_cube = null;
    SwipeLayout list_cube2 = null;
    SwipeLayout list_cube3 = null;
    SwipeLayout list_cube4 = null;

    //Data for FaceBook data withtin the side header
    private InterstitialAd mInterstitial;
    LoginButton facebookButton;
    CallbackManager callbackManager;
    private AccessToken accessToken;
    AccessTokenTracker accessTokenTracker;
    String[] fbInfo;
    private View header;
    private NavigationView navView;
    ProfilePictureView profilePictureView;
    TextView facebookName;
    public boolean[] dealCheck = new boolean[5];


    /**
     * Main onClick method
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = getApplicationContext();
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        accessToken = AccessToken.getCurrentAccessToken();


        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        limit = 19;

        radius = (double) mSharedPreferences.getInt("radius", 0);
        yelp.setRadius(radius);


        business = new String[5];
        breakfast = mSharedPreferences.getString("breakfastCat", "");
        lunch = mSharedPreferences.getString("lunchCat", "");
        dinner = mSharedPreferences.getString("dinnerCat", "");
        activity = mSharedPreferences.getString("activityCat", "");


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        SwipeLayout swipeLayout = (SwipeLayout)findViewById(R.id.content_main);
        swipeLayout.setDragEdge(SwipeLayout.DragEdge.Bottom);



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

                stopService(new Intent(MainActivity.this, GPSLocationService.class));

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

                startService(new Intent(MainActivity.this, GPSLocationService.class));

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
        });//End of Phone navigation




        //Initalizing The swipeLayout cubes

        //initalize each cube with it's layout counterpart
        list_cube0 = (SwipeLayout) findViewById(R.id.first_cube);
        list_cube0.setShowMode(SwipeLayout.ShowMode.PullOut);
        //Get the view of cubes botom swipe
        View starBottView = list_cube0.findViewById(R.id.starbott);
        //Get the view of cubes top swipe
        View topView = list_cube0.findViewById(R.id.top_facebook_share);

        //Setting Drag to each layout
        //Left swipe
        list_cube0.addDrag(SwipeLayout.DragEdge.Left, list_cube0.findViewById(R.id.leftside));
        //Right swipe
        list_cube0.addDrag(SwipeLayout.DragEdge.Right, list_cube0.findViewById(R.id.rightside));
        //top swipe
        list_cube0.addDrag(SwipeLayout.DragEdge.Top, topView);
        //bootm swipe
        list_cube0.addDrag(SwipeLayout.DragEdge.Bottom, starBottView);
        PressedAction(list_cube0, 0);


        //applied throughout the rest of the swipeLayouts

        list_cube = (SwipeLayout) findViewById(R.id.second_cube);
        list_cube.setShowMode(SwipeLayout.ShowMode.PullOut);
        View starBottView_2 = list_cube.findViewById(R.id.starbott);
        View topView2 = list_cube.findViewById(R.id.top_facebook_share);

        list_cube.addDrag(SwipeLayout.DragEdge.Left, list_cube.findViewById(R.id.leftside));
        list_cube.addDrag(SwipeLayout.DragEdge.Right, list_cube.findViewById(R.id.rightside));
        list_cube.addDrag(SwipeLayout.DragEdge.Top, topView2);
        list_cube.addDrag(SwipeLayout.DragEdge.Bottom, starBottView_2);
        PressedAction(list_cube, 1);


        list_cube2 = (SwipeLayout) findViewById(R.id.thrid_cube);
        list_cube2.setShowMode(SwipeLayout.ShowMode.PullOut);
        View starBottView_3 = list_cube2.findViewById(R.id.starbott);
        View topView3 = list_cube2.findViewById(R.id.top_facebook_share);

        list_cube2.addDrag(SwipeLayout.DragEdge.Left, list_cube2.findViewById(R.id.leftside));
        list_cube2.addDrag(SwipeLayout.DragEdge.Right, list_cube2.findViewById(R.id.rightside));
        list_cube2.addDrag(SwipeLayout.DragEdge.Top, topView3);
        list_cube2.addDrag(SwipeLayout.DragEdge.Bottom, starBottView_3);
        PressedAction(list_cube2,2);


        list_cube3 = (SwipeLayout) findViewById(R.id.fourth_cube);
        list_cube3.setShowMode(SwipeLayout.ShowMode.PullOut);
        View starBottView_4 = list_cube3.findViewById(R.id.starbott);
        View topView4 = list_cube3.findViewById(R.id.top_facebook_share);

        list_cube3.addDrag(SwipeLayout.DragEdge.Left, list_cube3.findViewById(R.id.leftside));
        list_cube3.addDrag(SwipeLayout.DragEdge.Right, list_cube3.findViewById(R.id.rightside));
        list_cube3.addDrag(SwipeLayout.DragEdge.Top, topView4);
        list_cube3.addDrag(SwipeLayout.DragEdge.Bottom, starBottView_4);
        PressedAction(list_cube3,3 );


        list_cube4 = (SwipeLayout) findViewById(R.id.fifth_cube);
        list_cube4.setShowMode(SwipeLayout.ShowMode.PullOut);
        View starBottView_5 = list_cube4.findViewById(R.id.starbott);
        View topView5 = list_cube4.findViewById(R.id.top_facebook_share);

        list_cube4.addDrag(SwipeLayout.DragEdge.Left, list_cube4.findViewById(R.id.leftside));
        list_cube4.addDrag(SwipeLayout.DragEdge.Right, list_cube4.findViewById(R.id.rightside));
        list_cube4.addDrag(SwipeLayout.DragEdge.Top, topView5);
        list_cube4.addDrag(SwipeLayout.DragEdge.Bottom, starBottView_5);
        PressedAction(list_cube4,4);
        //Finish initalizing swipelayout


        //Initilize Floating Button
        FloatingActionButton fullday = (FloatingActionButton) findViewById(R.id.fullday);
        assert fullday != null;


        //If the user just entered the page it would activate
        if(firstPlanActivite && isNetworkAvailable()){
            planExcuteText = "Loading Data";
            GeneratePlanTask a = new GeneratePlanTask();
            a.execute();
            firstPlanActivite = false;
        }

        fullday.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

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
                    }else {
                        Toast.makeText(MainActivity.this,"No Internet Connection", Toast.LENGTH_SHORT).show();
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
        navView = (NavigationView) findViewById(R.id.nav_view);
        header = navView.getHeaderView(0);
        profilePictureView = (ProfilePictureView) header.findViewById(R.id.facebook_picture);
        facebookName = (TextView) header.findViewById(R.id.facebook_name);


        //Checks if Facebook is logged in
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
            //loadImage(fbInfo[1]);
        }



        //Tutorial Data
        updateWithToken(accessToken);
        mContext = getApplicationContext();
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        mEditor = mSharedPreferences.edit();

        boolean fullToturial = mSharedPreferences.getBoolean("fullToturial",false);
        if(!fullToturial) {

            //mypart
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
                            "Swipe Up: View Deals\n" +
                            "Swipe Down: Facebook Share\n" +
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
                if (event.getAction() == MotionEvent.ACTION_MOVE) {

                    //Checks if anyother cube
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


        display.addRevealListener(R.id.share_button, new SwipeLayout.OnRevealListener() {
            @Override
            public void onReveal(View child, SwipeLayout.DragEdge edge, float fraction, int distance) {

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
                //Show Google Maps of bussiness
                String searchAddre = cube_info[index].location.display_address.get(0) + " "+ cube_info[index].location.display_address.get(1);
                Log.i("test", "" +  searchAddre);
                String map = "http://maps.google.co.in/maps?q=" + searchAddre;
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(map));
                display.close();
                startActivity(i);

            }
        });

        final ShareButton shareButton = (ShareButton) display.findViewById(R.id.share_button);

        display.findViewById(R.id.share_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Share with FaceBook
                Toast.makeText(MainActivity.this, "FaceBook share been pressed", Toast.LENGTH_SHORT).show();
                content = new ShareLinkContent.Builder()
                        .setContentTitle("I'm going to " + cube_info[index].name)
                        .setContentDescription(cube_info[index].snippet_text)
                        .setContentUrl(Uri.parse(cube_info[index].mobile_url))
                        .setImageUrl(Uri.parse(cube_info[index].image_url.toString()))
                        .build();

                shareButton.setShareContent(content);
                ShareDialog.show(MainActivity.this, content);

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
                //Call bussiness
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
                //Toast.makeText(MainActivity.this, display.getId() + "Delete", Toast.LENGTH_SHORT).show();
                //Delete Event
                display.setVisibility(SwipeLayout.INVISIBLE);
                display.close();


            }
        });

    }

    /**
     *  Sending and Reciving Yelp request
     * @param ran
     * @param index
     * @param term
     * @param display
     * @param dealChecker
     * @throws InterruptedException
     */
    public synchronized void getYelpSearchResult(final int ran, final int index ,final String term, final SwipeLayout display, int dealChecker) throws InterruptedException{

        final int dealChecking = dealChecker;

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                System.out.println("Index:  " + ran +  " Term:" + term);
                try{

                    String response = yelp.searchByLocation(term, GPSLocationService.currentLocation);
//                    String response = yelp.searchByLocation(term, address);
                    System.out.println(response);
                    Gson gson = new GsonBuilder().create();
                    final String[] crit = term.split(" ");

                    ResultInfo result = gson.fromJson(response, ResultInfo.class);
                    Log.d("RANDOM", "" +ran);
                    final BussnessInfo bussnessInfo = result.getBussnessInfo(ran);
                    cube_info[index] = bussnessInfo;


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {

                                System.out.println(bussnessInfo.name);
                                TextView title = (TextView) display.findViewById(R.id.activity_title);
                                title.setText(bussnessInfo.name);

                                TextView cri = (TextView) display.findViewById(R.id.activity_criteria);
                                cri.setText(crit[0].trim());


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

                                TextView dealTitle = (TextView) display.findViewById(R.id.deal_title);
                                dealTitle.setText("No Deal");

                                //If deal is found
                                if (bussnessInfo.deals.get(0) != null){
                                    dealCheck[dealChecking] = true;
                                    dealTitle.setText(bussnessInfo.deals.get(0).title + "\n" + "*see Yelp for more details.");
                                }


                            } catch (Exception e) {
                                System.out.println("Thread Error -> " + e);
                                e.printStackTrace();
                            }
                            businessName = bussnessInfo.name;
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
     * if the user presses back
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);

        }
        else {

            super.onBackPressed();

            }
    }


    /**
     *  Slide menu selection options
     * @param item
     * @return
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.First) {
            //Same Page

        } else if (id == R.id.Second) {
            //Send to Single Resturant Finder
            final Intent singleResturant = new Intent(MainActivity.this, ResturantActivity.class);
            MainActivity.this.startActivity(singleResturant);
            finish();

        } else if (id == R.id.Third) {
            //Send to  BarHopper
            final Intent barMode = new Intent(MainActivity.this, BarHoppingMode.class);
            MainActivity.this.startActivity(barMode);
            finish();

        } else if (id == R.id.Fourth) {
            //Send to  Events Activity
            final Intent activitymod = new Intent(MainActivity.this, EventsActivity.class);
            MainActivity.this.startActivity(activitymod);
            finish();

        } else if (id == R.id.sectionOne) {
            //Send to Settings Page
            final Intent mainIntent = new Intent(MainActivity.this, Setting_Page.class);
            MainActivity.this.startActivity(mainIntent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    /**
     * Loading Page for generating Full Day
     */
    public class GeneratePlanTask extends AsyncTask<Void, Void, Void> {
        long end;
        protected void onPreExecute() {
            long start = System.currentTimeMillis();
            end = start + 20*1000; // 20 seconds * 1000 ms/sec
            //Loading spinner
            loadingSpinner = new ProgressDialog(MainActivity.this);
            loadingSpinner.setCancelable(false);
            loadingSpinner.setMessage(planExcuteText + " ...");
            loadingSpinner.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            loadingSpinner.setProgress(0);
            loadingSpinner.setMax(100);
            loadingSpinner.show();

        }

        @Override
        protected Void doInBackground(Void... params) {
            //Data request from yelp
            if(System.currentTimeMillis() > end)
            {
                Log.d("Timer","Stopped");
                this.cancel(true);
                loadingSpinner.dismiss();
            }

            try {
                ArrayList<String> businesses = new ArrayList<>(4);
                Random r = new Random();

                int randNum = r.nextInt(limit);


                System.out.println("Random Number:  " + randNum + " limit: " + limit + " Catagory:" + breakfast);
                getYelpSearchResult(randNum, 0, "breakfast " + breakfast, list_cube0,0);


                Thread.sleep(100);

                if(System.currentTimeMillis() > end)
                {
                    Log.d("Timer","Stopped");
                    this.cancel(true);
                    loadingSpinner.dismiss();
                }

                business[0] = businessName;


                Log.i("Var", "BussnessName: " + businessName + " Ran" + randNum);

                do {
                    randNum = r.nextInt(limit);
                    System.out.println("Random Number:  " + randNum + " limit: " + limit);
                    getYelpSearchResult(randNum, 1, "Activity " + activity, list_cube,1);
                    Thread.sleep(100);
                    Log.i("Var", "BussnessName: " + businessName + " Ran" + randNum);
                    if(System.currentTimeMillis() > end)
                    {
                        Log.d("Timer","Stopped");
                        this.cancel(true);
                        loadingSpinner.dismiss();
                    }

                }while (contains(business,businessName));
                business[1] = businessName;



                System.out.println("Random Number:  " + randNum + " limit: " + limit + " Catagory:" + lunch);
                Log.i("Test", "Random Number:  " + randNum + " limit: " + limit + " Catagory:" + dinner);
                do {
                    randNum = r.nextInt(limit);
                    System.out.println("Random Number:  " + randNum + " limit: " + limit + " Catagory:" + lunch);
                    getYelpSearchResult(randNum, 2, "Lunch " + lunch, list_cube2,2);
                    Thread.sleep(100);
                    Log.i("Var", "BussnessName: " + businessName + " Ran" + randNum);

                    if(System.currentTimeMillis() > end)
                    {
                        Log.d("Timer","Stopped");
                        this.cancel(true);
                        loadingSpinner.dismiss();
                    }

                }while(contains(business,businessName));
                business[2] = businessName;
                //

                System.out.println("Random Number:  " + randNum + " limit: " + limit);
                Log.i("Test", "Random Number:  " + randNum + " limit: " + limit + " Catagory:" + dinner);
                do{
                    randNum = r.nextInt(limit);
                    getYelpSearchResult(randNum, 3, "Activity "+ activity, list_cube3,3);
                    Thread.sleep(100);
                    Log.i("Var", "BussnessName: " + businessName + " Ran" + randNum);

                    if(System.currentTimeMillis() > end)
                    {
                        Log.d("Timer","Stopped");
                        this.cancel(true);
                        loadingSpinner.dismiss();
                    }
                }while(contains(business,businessName));
                business[3] = businessName;



                System.out.println("Random Number:  " + randNum + " limit: " + limit + " Catagory:" + dinner);
                Log.i("Test", "Random Number:  " + randNum + " limit: " + limit + " Catagory:" + dinner);
                do{
                    randNum = r.nextInt(limit);
                    getYelpSearchResult(randNum, 4, "Dinner " + dinner, list_cube4,4);
                    Thread.sleep(100);
                    Log.i("Var", "BussnessName: " + businessName + " Ran" + randNum);
                    if(System.currentTimeMillis() > end)
                    {
                        Log.d("Timer","Stopped");
                        this.cancel(true);
                        loadingSpinner.dismiss();
                    }
                }while(contains(business,businessName));
                business[4] = businessName;



                for (String s : business){
                    //Log.i("XXX",business[i]);
                    System.out.println("businessNameWr: " + s);
                    //businesses.remove(i);
                }

            } catch (Exception e) {
                //e.printStackTrace();
                this.cancel(true);
                loadingSpinner.dismiss();
            }

            return null;
        }

        protected void onPostExecute(Void aVoid) {
            loadingSpinner.dismiss();
            //After Data is loaded

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    if (dealCheck[0] == true){
                        list_cube0.open(true, SwipeLayout.DragEdge.Bottom);
                    }
                    if (dealCheck[1] == true){
                        list_cube.open(true, SwipeLayout.DragEdge.Bottom);
                    }
                    if (dealCheck[2] == true){
                        list_cube2.open(true, SwipeLayout.DragEdge.Bottom);
                    }
                    if (dealCheck[3] == true){
                        list_cube3.open(true, SwipeLayout.DragEdge.Bottom);
                    }
                    if (dealCheck[4] == true){
                        list_cube4.open(true, SwipeLayout.DragEdge.Bottom);
                    }
                    Arrays.fill(dealCheck, false);

                }
            }, 1000);

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    list_cube0.close(true);
                    list_cube.close(true);
                    list_cube2.close(true);
                    list_cube3.close(true);
                    list_cube4.close(true);

                }
            }, 2000);




        }
    }

    /**
     * Refershing single activity
     */
    public class RefreshTask extends AsyncTask<SwipeLayout, Void, Void> {

        protected void onPreExecute() {
            //Loading Spinner
            loadingSpinner = new ProgressDialog(MainActivity.this);
            loadingSpinner.setCancelable(false);
            loadingSpinner.setMessage("Refreshing Event ...");
            loadingSpinner.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            loadingSpinner.setProgress(0);
            loadingSpinner.setMax(100);
            loadingSpinner.show();

        }

        @Override
        protected Void doInBackground(SwipeLayout... params) {
            //Send request from yelp
            SwipeLayout r = params[0];
            try {

                Random random = new Random();

                int randNum = random.nextInt(limit);


                System.out.println("Random Number:  " + randNum + " limit: " + limit + " Catagory:" + breakfast);
                getYelpSearchResult(randNum, refreshIndex, getCriteria(), r,0);


            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        private String getCriteria(){
            //Getting criteria from setting
            String refreshcriteria = null;
            switch (refreshIndex){
                case 0:
                    refreshcriteria = "breakfast " + breakfast;
                    Log.i("getCriteria", refreshcriteria +" "+refreshIndex);
                    break;
                case 1:
                    refreshcriteria = "Acivity " + activity;
                    Log.i("getCriteria", refreshcriteria +" "+refreshIndex);
                    break;
                case 2:
                    refreshcriteria = "Lunch " + lunch;
                    Log.i("getCriteria", refreshcriteria +" "+refreshIndex);
                    break;
                case 3:
                    refreshcriteria =  "Activity " + activity;
                    Log.i("getCriteria", refreshcriteria +" "+refreshIndex);
                    break;
                case 4:
                    refreshcriteria = "Dinner " + dinner;
                    Log.i("getCriteria", refreshcriteria +" "+refreshIndex);
                    break;
            }

            return refreshcriteria;
        }


        @Override
        protected void onPostExecute(Void aVoid)
        {

            //After data is loaded
            loadingSpinner.dismiss();
        }

    }//End of RefreshTask


    /**
     * Checking if the Network
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
     * Checking if the user is loggedIn in FaceBook
     * @return
     */
    public boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }

    /**
     * Getting Facebook Information
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
     * Loading images from FaceBook
     * @param id
     */
    public void loadImage(String id) {
        profilePictureView.setProfileId(id);
        profilePictureView.setPresetSize(ProfilePictureView.SMALL);
    }

    /**
     * Update FaceBook Data
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



