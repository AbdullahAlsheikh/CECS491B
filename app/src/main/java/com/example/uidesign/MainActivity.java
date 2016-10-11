package com.example.uidesign;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.*;
import android.os.Handler;
import android.widget.*;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import com.google.gson.*;

import java.util.ArrayList;
import java.util.Random;
import com.daimajia.swipe.SwipeLayout;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

public class MainActivity extends  AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ProgressDialog loadingSpinner;

    private Context mContext;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    String breakfast;
    String lunch;
    String dinner;


    String consumerKey = "dudmo3ssHxvpUP_i_Lw60A";
    String consumerSecret = "fOhwH5mUo_CyzX2D2vcDUc8FNw8";
    String token = "yPhkb0u9cRxGE8ikWRkH3ceMCCpKYpQA";
    String tokenSecret = "-WoZd39mwu4X9iVDXo5bxDNOBBU";
    Yelp yelp = new Yelp(consumerKey, consumerSecret, token, tokenSecret);

    SwipeLayout list_cube0 = null;
    SwipeLayout list_cube = null;
    SwipeLayout list_cube2 = null;
    SwipeLayout list_cube3 = null;
    SwipeLayout list_cube4 = null;

    BussnessInfo[] cube_info = new BussnessInfo[5];


    int limit = yelp.getLimit();
    String address = "California State University, Long Beach, Long Beach, CA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = getApplicationContext();
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);

        limit = mSharedPreferences.getInt("limit", 0);
        yelp.setLimit(limit);

        breakfast = mSharedPreferences.getString("breakfastCat", "");
        lunch = mSharedPreferences.getString("lunchCat", "");
        dinner = mSharedPreferences.getString("dinnerCat", "");


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

                GPSLocationService.currentLocation = (String) place.getName();

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




        fullday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Pressed Full Day", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                try {

                    new GeneratePlanTask().execute();

                } catch (Exception e) {
                    System.out.println("full day set on click :Error -> " + e);
                    e.printStackTrace();
                }


            }
        });

//        if(!(GPSLocationService.currentLocation.isEmpty()))
//        {
//            address = GPSLocationService.currentLocation;
//        }

        bottominformation.setText("Address:\n " + GPSLocationService.currentLocation + "\nlimit:" + limit + "\n" + "breakfast" + breakfast  + "\nlunch" + lunch + "\nDinner" + dinner);


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


        display.addRevealListener(R.id.delete, new SwipeLayout.OnRevealListener() {
            @Override
            public void onReveal(View child, SwipeLayout.DragEdge edge, float fraction, int distance) {

            }
        });

        display.getSurfaceView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(MainActivity.this, display.getId() + "Click on surface", Toast.LENGTH_SHORT).show();
                Log.d(MainActivity.class.getName(), "click on surface");
                Log.i("Phone Number", cube_info[index].phone + " Phone");
            }
        });

        display.getSurfaceView().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //Toast.makeText(MainActivity.this, display.getId() + "longClick on surface", Toast.LENGTH_SHORT).show();
                Log.d(MainActivity.class.getName(), "longClick on surface");
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
                Toast.makeText(MainActivity.this, display.getId() + "Star", Toast.LENGTH_SHORT).show();
                String searchAddre = cube_info[index].location.display_address.get(0) + " "+ cube_info[index].location.display_address.get(1);
                Log.i("test", "" +  searchAddre);
                String map = "http://maps.google.co.in/maps?q=" + searchAddre;
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(map));
                startActivity(i);

            }
        });

        display.findViewById(R.id.trash2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, display.getId() + "Trash Bin", Toast.LENGTH_SHORT).show();
            }
        });

        display.findViewById(R.id.magnifier2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, display.getId() + "Magnifier", Toast.LENGTH_SHORT).show();
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + cube_info[index].phone));
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
                Toast.makeText(MainActivity.this, display.getId() + "Delete", Toast.LENGTH_SHORT).show();
            }
        });

    }


    public synchronized void getYelpSearchResult(final int ran, final int index ,final String term, final SwipeLayout display) throws InterruptedException{
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                System.out.println("Index:  " + ran +  " Term:" + term);
                try{

                    String response = yelp.searchByLocation(term, GPSLocationService.currentLocation);
                    System.out.println(response);
                    Gson gson = new GsonBuilder().create();

                    ResultInfo result = gson.fromJson(response, ResultInfo.class);
                    final BussnessInfo bussnessInfo = result.getBussnessInfo(ran);
                    cube_info[index] = bussnessInfo;

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {

                                ImageView icon = (ImageView) display.findViewById(R.id.activity_icon);
                                icon.setImageBitmap(bussnessInfo.Iconimg);

                            } catch (Exception e) {
                                System.out.println("Image Error -> " + e);
                                e.printStackTrace();
                            }

                            System.out.println(bussnessInfo.name);
                            TextView title = (TextView) display.findViewById(R.id.activity_title);
                            title.setText(bussnessInfo.name);

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
            System.out.println("Third Button");


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
            //progressBarStatus = 0;

            //loadingSpinner = new ProgressDialog(senderView.getContext());
            loadingSpinner = new ProgressDialog(MainActivity.this);
            loadingSpinner.setCancelable(false);
            loadingSpinner.setMessage("Generating a plan ...");
            loadingSpinner.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            loadingSpinner.setProgress(0);
            loadingSpinner.setMax(100);
            loadingSpinner.show();

        }

        @Override
        protected Void doInBackground(Void... params) {
            //if (fullDayPlan) {
            try {

                Random r = new Random();

                int randNum = r.nextInt(limit);


                System.out.println("Random Number:  " + randNum + " limit: " + limit + " Catagory:" +breakfast);
                getYelpSearchResult(randNum, 0, "breakfast " + breakfast, list_cube0);


                randNum = r.nextInt(limit);
                System.out.println("Random Number:  " + randNum + " limit: " + limit);
                getYelpSearchResult(randNum, 1, "Park", list_cube);


                randNum = r.nextInt(limit);

                System.out.println("Random Number:  " + randNum + " limit: " + limit + " Catagory:" +lunch);
                getYelpSearchResult(randNum, 2, "Lunch " + lunch, list_cube2);



                randNum = r.nextInt(limit);
                System.out.println("Random Number:  " + randNum + " limit: " + limit);
                getYelpSearchResult(randNum, 3, "Activity", list_cube3);



                randNum = r.nextInt(limit);
                System.out.println("Random Number:  " + randNum + " limit: " + limit + " Catagory:" +dinner);
                getYelpSearchResult(randNum, 4,"Dinner " + dinner, list_cube4);

            } catch (Exception e) {
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
}




