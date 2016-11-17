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
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Random;

import static com.example.uidesign.BarHoppingMode.contains;

public class MainActivity extends  AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ProgressDialog loadingSpinner;
    private String planExcuteText = "";
    private boolean firstPlanActivite = true;

    private Context mContext;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    String breakfast;
    String lunch;
    String dinner, activity;

    String businessName;
    String[] business;



    private String consumerKey = "dudmo3ssHxvpUP_i_Lw60A";
    private String consumerSecret = "fOhwH5mUo_CyzX2D2vcDUc8FNw8";
    private String token = "yPhkb0u9cRxGE8ikWRkH3ceMCCpKYpQA";
    private String tokenSecret = "-WoZd39mwu4X9iVDXo5bxDNOBBU";
    Yelp yelp = new Yelp(consumerKey, consumerSecret, token, tokenSecret);

    SwipeLayout list_cube0 = null;
    SwipeLayout list_cube = null;
    SwipeLayout list_cube2 = null;
    SwipeLayout list_cube3 = null;
    SwipeLayout list_cube4 = null;


    BussnessInfo[] cube_info = new BussnessInfo[5];

    int refreshIndex = 0;


    int limit = 19;
    double radius = yelp.getraduis();
    String address = "California State University, Long Beach, CA";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = getApplicationContext();
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);

        //limit = mSharedPreferences.getInt("limit", 0);
        limit = 19;
        //yelp.setLimit(limit);

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

        TextView bottominformation = (TextView) findViewById(R.id.information);



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
        });


        System.out.println("TestNetwork: " + isNetworkAvailable());


        list_cube0 = (SwipeLayout) findViewById(R.id.first_cube);
        list_cube0.setShowMode(SwipeLayout.ShowMode.PullOut);
        View starBottView = list_cube0.findViewById(R.id.starbott);

        list_cube0.addDrag(SwipeLayout.DragEdge.Left, list_cube0.findViewById(R.id.leftside));
        list_cube0.addDrag(SwipeLayout.DragEdge.Right, list_cube0.findViewById(R.id.rightside));
        list_cube0.addDrag(SwipeLayout.DragEdge.Top, starBottView);
        list_cube0.addDrag(SwipeLayout.DragEdge.Bottom, starBottView);
        PressedAction(list_cube0, 0);


        list_cube = (SwipeLayout) findViewById(R.id.second_cube);
        list_cube.setShowMode(SwipeLayout.ShowMode.PullOut);
        View starBottView_2 = list_cube.findViewById(R.id.starbott);

        list_cube.addDrag(SwipeLayout.DragEdge.Left, list_cube.findViewById(R.id.leftside));
        list_cube.addDrag(SwipeLayout.DragEdge.Right, list_cube.findViewById(R.id.rightside));
        list_cube.addDrag(SwipeLayout.DragEdge.Top, starBottView_2);
        list_cube.addDrag(SwipeLayout.DragEdge.Bottom, starBottView_2);
        PressedAction(list_cube, 1);


        list_cube2 = (SwipeLayout) findViewById(R.id.thrid_cube);
        list_cube2.setShowMode(SwipeLayout.ShowMode.PullOut);
        View starBottView_3 = list_cube2.findViewById(R.id.starbott);

        list_cube2.addDrag(SwipeLayout.DragEdge.Left, list_cube2.findViewById(R.id.leftside));
        list_cube2.addDrag(SwipeLayout.DragEdge.Right, list_cube2.findViewById(R.id.rightside));
        list_cube2.addDrag(SwipeLayout.DragEdge.Top, starBottView_3);
        list_cube2.addDrag(SwipeLayout.DragEdge.Bottom, starBottView_3);
        PressedAction(list_cube2,2);


        list_cube3 = (SwipeLayout) findViewById(R.id.fourth_cube);
        list_cube3.setShowMode(SwipeLayout.ShowMode.PullOut);
        View starBottView_4 = list_cube3.findViewById(R.id.starbott);

        list_cube3.addDrag(SwipeLayout.DragEdge.Left, list_cube3.findViewById(R.id.leftside));
        list_cube3.addDrag(SwipeLayout.DragEdge.Right, list_cube3.findViewById(R.id.rightside));
        list_cube3.addDrag(SwipeLayout.DragEdge.Top, starBottView_4);
        list_cube3.addDrag(SwipeLayout.DragEdge.Bottom, starBottView_4);
        PressedAction(list_cube3,3 );


        list_cube4 = (SwipeLayout) findViewById(R.id.fifth_cube);
        list_cube4.setShowMode(SwipeLayout.ShowMode.PullOut);
        View starBottView_5 = list_cube4.findViewById(R.id.starbott);

        list_cube4.addDrag(SwipeLayout.DragEdge.Left, list_cube4.findViewById(R.id.leftside));
        list_cube4.addDrag(SwipeLayout.DragEdge.Right, list_cube4.findViewById(R.id.rightside));
        list_cube4.addDrag(SwipeLayout.DragEdge.Top, starBottView_5);
        list_cube4.addDrag(SwipeLayout.DragEdge.Bottom, starBottView_5);
        PressedAction(list_cube4,4);


        FloatingActionButton fullday = (FloatingActionButton) findViewById(R.id.fullday);
        assert fullday != null;


        if(firstPlanActivite && isNetworkAvailable()){

            planExcuteText = "Loading Data";
            new GeneratePlanTask().execute();
            firstPlanActivite = false;
        }

            fullday.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Snackbar.make(view, "Pressed Full Day", Snackbar.LENGTH_LONG)
                    //.setAction("Action", null).show();
                    if (isNetworkAvailable()) {
                    try {

                        list_cube0.setVisibility(list_cube0.VISIBLE);
                        list_cube.setVisibility(list_cube.VISIBLE);
                        list_cube2.setVisibility(list_cube2.VISIBLE);
                        list_cube3.setVisibility(list_cube3.VISIBLE);
                        list_cube4.setVisibility(list_cube4.VISIBLE);
                        planExcuteText = "Refreshing Entire Plan";
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



        bottominformation.setText("Address:\n " + GPSLocationService.currentLocation + "\nlimit:" +
                limit + "\nRadius:" + radius + "\n" + "breakfast" + breakfast  + "\nlunch" + lunch
                + "\nDinner" + dinner + " Activity: " + activity);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    /**
     * When pressing any of the activities, here to process
     * @param display
     */
    public void PressedAction(final SwipeLayout display,final int index ) {

        display.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    switch (index) {
                        case 0:
//                            Log.i("list", "is list0");
//                            list_cube0.open();
                            list_cube.close();
                            list_cube2.close();
                            list_cube3.close();
                            list_cube4.close();
                            break;
                        case 1:
//                            Log.i("list", "is list");
//                            list_cube.open();
                            list_cube0.close();
                            list_cube2.close();
                            list_cube3.close();
                            list_cube4.close();
                            break;
                        case 2:
//                            Log.i("list", "is list2");
//                            list_cube2.open();
                            list_cube0.close();
                            list_cube.close();
                            list_cube3.close();
                            list_cube4.close();
                            break;
                        case 3:
//                            Log.i("list", "is list3");
//                            list_cube3.open();
                            list_cube0.close();
                            list_cube2.close();
                            list_cube.close();
                            list_cube4.close();
                            break;
                        case 4:
                            Log.i("list", "is list4");
//                            list_cube4.open();
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


        display.addRevealListener(R.id.delete, new SwipeLayout.OnRevealListener() {
            @Override
            public void onReveal(View child, SwipeLayout.DragEdge edge, float fraction, int distance) {

            }
        });

        display.getSurfaceView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                //Toast.makeText(MainActivity.this, display.getId() + "Magnifier", Toast.LENGTH_SHORT).show();
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
                //display.setVisibility(display.INVISIBLE);
                display.setVisibility(SwipeLayout.INVISIBLE);
                display.close();


            }
        });

    }


    public synchronized void getYelpSearchResult(final int ran, final int index ,final String term, final SwipeLayout display) throws InterruptedException{
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                System.out.println("Index:  " + ran +  " Term:" + term);
                try{

//                    String response = yelp.searchByLocation(term, GPSLocationService.currentLocation);
                    String response = yelp.searchByLocation(term, address);
                    System.out.println(response);
                    Gson gson = new GsonBuilder().create();
                    final String[] crit = term.split(" ");

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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {

            drawer.closeDrawer(GravityCompat.START);

        } else {

            super.onBackPressed();

            }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.First) {

            System.out.println("within Full Day Activity");

            // Handle the camera action
        } else if (id == R.id.Second) {
            final Intent singleResturant = new Intent(MainActivity.this, ResturantActivity.class);
            MainActivity.this.startActivity(singleResturant);
            finish();
            System.out.println("Second Button");

        } else if (id == R.id.Third) {
            //this takes you to the bar hopping mode
            System.out.println("Third Button");
            final Intent barMode = new Intent(MainActivity.this, BarHoppingMode.class);
            MainActivity.this.startActivity(barMode);
            finish();



        } else if (id == R.id.Fourth) {
            System.out.println("Fourth Button");


        } else if (id == R.id.sectionOne) {
            System.out.println("First Section Button");
            final Intent mainIntent = new Intent(MainActivity.this, Setting_Page.class);
            MainActivity.this.startActivity(mainIntent);
            finish();


        } else if (id == R.id.sectionTwo) {
            System.out.println("Second Section Button");

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public class GeneratePlanTask extends AsyncTask<Void, Void, Void> {
        protected void onPreExecute() {

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
            try {
                ArrayList<String> businesses = new ArrayList<>(4);
                Random r = new Random();

                int randNum = r.nextInt(limit);


                System.out.println("Random Number:  " + randNum + " limit: " + limit + " Catagory:" + breakfast);
                getYelpSearchResult(randNum, 0, "breakfast " + breakfast, list_cube0);


                Thread.sleep(100);


                business[0] = businessName;


                Log.i("Var", "BussnessName: " + businessName + " Ran" + randNum);

                do {
                    randNum = r.nextInt(limit);
                    System.out.println("Random Number:  " + randNum + " limit: " + limit);
                    getYelpSearchResult(randNum, 1, "Activity " + activity, list_cube);
                    Thread.sleep(100);
                    Log.i("Var", "BussnessName: " + businessName + " Ran" + randNum);
                }while (contains(business,businessName));
                business[1] = businessName;


                System.out.println("Random Number:  " + randNum + " limit: " + limit + " Catagory:" + lunch);
                Log.i("Test", "Random Number:  " + randNum + " limit: " + limit + " Catagory:" + dinner);
                do {
                    randNum = r.nextInt(limit);
                    System.out.println("Random Number:  " + randNum + " limit: " + limit + " Catagory:" + lunch);
                    getYelpSearchResult(randNum, 2, "Lunch " + lunch, list_cube2);
                    Thread.sleep(100);
                    Log.i("Var", "BussnessName: " + businessName + " Ran" + randNum);
                }while(contains(business,businessName));
                business[2] = businessName;
                //

                System.out.println("Random Number:  " + randNum + " limit: " + limit);
                Log.i("Test", "Random Number:  " + randNum + " limit: " + limit + " Catagory:" + dinner);
                do{
                    randNum = r.nextInt(limit);
                    getYelpSearchResult(randNum, 3, "Activity "+ activity, list_cube3);
                    Thread.sleep(100);
                    Log.i("Var", "BussnessName: " + businessName + " Ran" + randNum);
                }while(contains(business,businessName));
                business[3] = businessName;

                System.out.println("Random Number:  " + randNum + " limit: " + limit + " Catagory:" + dinner);
                Log.i("Test", "Random Number:  " + randNum + " limit: " + limit + " Catagory:" + dinner);
                do{
                    randNum = r.nextInt(limit);
                    getYelpSearchResult(randNum, 4, "Dinner " + dinner, list_cube4);
                    Thread.sleep(100);
                    Log.i("Var", "BussnessName: " + businessName + " Ran" + randNum);
                }while(contains(business,businessName));
                business[4] = businessName;

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
        protected void onPostExecute(Void aVoid) {
            loadingSpinner.dismiss();
        }
    }


    public class RefreshTask extends AsyncTask<SwipeLayout, Void, Void> {

        protected void onPreExecute() {

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
            SwipeLayout r = params[0];
            try {

                Random random = new Random();

                int randNum = random.nextInt(limit);


                System.out.println("Random Number:  " + randNum + " limit: " + limit + " Catagory:" + breakfast);
                getYelpSearchResult(randNum, refreshIndex, getCriteria(), r);


            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        private String getCriteria(){
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
        protected void onPostExecute(Void aVoid) {
            loadingSpinner.dismiss();
        }

    }

    private boolean isNetworkAvailable() {
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



