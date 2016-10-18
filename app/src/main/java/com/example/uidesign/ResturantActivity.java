package com.example.uidesign;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.util.Log;
import android.widget.*;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView.ScaleType;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.gson.*;
import java.util.Random;
import com.daimajia.swipe.SwipeLayout;

/**
 * Created by Abdullah on 10/3/16.
 */
public class ResturantActivity  extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private ProgressDialog loadingSpinner;

    LinearLayout togetherlayout;

    String consumerKey = "dudmo3ssHxvpUP_i_Lw60A";
    String consumerSecret = "fOhwH5mUo_CyzX2D2vcDUc8FNw8";
    String token = "yPhkb0u9cRxGE8ikWRkH3ceMCCpKYpQA";
    String tokenSecret = "-WoZd39mwu4X9iVDXo5bxDNOBBU";
    Yelp yelp = new Yelp(consumerKey, consumerSecret, token, tokenSecret);


    BussnessInfo single_activty_info = null;
    int limit = yelp.getLimit();
    String address = "California State University, Long Beach, Long Beach, CA";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resturant_activity_main);
        setTitle("Single Activity");
        togetherlayout = (LinearLayout) findViewById(R.id.resutrant_together);

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




        final SwipeLayout singleCube = (SwipeLayout) findViewById(R.id.singleResutant);
        singleCube.setShowMode(SwipeLayout.ShowMode.PullOut);
        View starBottView = singleCube.findViewById(R.id.starbott);

        singleCube.addDrag(SwipeLayout.DragEdge.Left, singleCube.findViewById(R.id.wrapper));
        singleCube.addDrag(SwipeLayout.DragEdge.Right, singleCube.findViewById(R.id.single_bottom_wrapper));
        singleCube.addDrag(SwipeLayout.DragEdge.Top, starBottView);
        singleCube.addDrag(SwipeLayout.DragEdge.Bottom, starBottView);
        PressedAction(singleCube);


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

//        bottominformation.setText("Address:\n " + address + "\nlimit:" + limit + "\n");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }


    public void PressedAction(final SwipeLayout display) {


        display.addRevealListener(R.id.delete, new SwipeLayout.OnRevealListener() {
            @Override
            public void onReveal(View child, SwipeLayout.DragEdge edge, float fraction, int distance) {

            }
        });

        display.getSurfaceView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ResturantActivity.this, display.getId() + "Click on surface", Toast.LENGTH_SHORT).show();


            }
        });
        display.getSurfaceView().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(ResturantActivity.this, display.getId() + "longClick on surface", Toast.LENGTH_SHORT).show();
                Log.i("test", single_activty_info.mobile_url);
                try {
                    Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(single_activty_info.mobile_url));
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
                //navigation functionality
                Toast.makeText(ResturantActivity.this, display.getId() + "Star", Toast.LENGTH_SHORT).show();
                String searchAddre = single_activty_info.location.display_address.get(0) + " "+ single_activty_info.location.display_address.get(1);
                Log.i("test", "" +  searchAddre);
                String map = "http://maps.google.co.in/maps?q=" + searchAddre;
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(map));
                startActivity(i);
            }
        });

        display.findViewById(R.id.trash2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ResturantActivity.this, display.getId() + "Trash Bin", Toast.LENGTH_SHORT).show();
            }
        });

        display.findViewById(R.id.magnifier2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Calling Functionality
                Toast.makeText(ResturantActivity.this, display.getId() + "Magnifier", Toast.LENGTH_SHORT).show();
                Log.i("test", single_activty_info.phone);
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + single_activty_info.phone));
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
                Toast.makeText(ResturantActivity.this, display.getId() + "Delete", Toast.LENGTH_SHORT).show();
            }
        });

    }


    public synchronized void getYelpSearchResult(final int index, final String term, final LinearLayout display) throws InterruptedException{
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                System.out.println("Index:  " + index +  " Term:" + term);
                try{

//                    String response = yelp.searchByLocation(term, GPSLocationService.currentLocation);
                    String response = yelp.searchByLocation(term, address);

                    System.out.println(response);
                    Gson gson = new GsonBuilder().create();

                    ResultInfo result = gson.fromJson(response, ResultInfo.class);
                    final BussnessInfo bussnessInfo = result.getBussnessInfo(index);
                    single_activty_info = bussnessInfo;

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {

                                ImageView icon = (ImageView) display.findViewById(R.id.activity_icon);
                                icon.setImageBitmap(bussnessInfo.icon_img);

                                icon.setScaleType(ScaleType.FIT_XY);


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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.First) {
            System.out.println("First Button");
            final Intent mainIntent = new Intent(ResturantActivity.this, MainActivity.class);
            ResturantActivity.this.startActivity(mainIntent);
        } else if (id == R.id.Second) {
            System.out.println("Already in Single Activiy");

        } else if (id == R.id.Third) {
            System.out.println("Third Button");


        } else if (id == R.id.Fourth) {
            System.out.println("Fourth Button");


        } else if (id == R.id.sectionOne) {
            System.out.println("First Section Button");
            final Intent mainIntent = new Intent(ResturantActivity.this, Setting_Page.class);
            ResturantActivity.this.startActivity(mainIntent);

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
            //if (fullDayPlan) {
                try {
                    Random r = new Random();

                    int randNum = r.nextInt(limit);
                    System.out.println("Random Number:  " + randNum + " limit: " + limit);
                    getYelpSearchResult(randNum, "Restaurant", togetherlayout);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
//            } else {
//                try {
//                    getYelpSearchResult(0, "Restaurant", finalAddress);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            //arrayAdapter.notifyDataSetChanged();
            loadingSpinner.dismiss();
        }
    }

}

