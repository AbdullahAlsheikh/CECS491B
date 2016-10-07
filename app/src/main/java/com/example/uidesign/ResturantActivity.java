package com.example.uidesign;

import android.content.Intent;
import android.os.Bundle;
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
import android.view.Menu;
import com.google.gson.*;
import java.util.Random;
import com.daimajia.swipe.SwipeLayout;

/**
 * Created by Abdullah on 10/3/16.
 */
public class ResturantActivity  extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    String consumerKey = "dudmo3ssHxvpUP_i_Lw60A";
    String consumerSecret = "fOhwH5mUo_CyzX2D2vcDUc8FNw8";
    String token = "yPhkb0u9cRxGE8ikWRkH3ceMCCpKYpQA";
    String tokenSecret = "-WoZd39mwu4X9iVDXo5bxDNOBBU";
    Yelp yelp = new Yelp(consumerKey, consumerSecret, token, tokenSecret);


    int limit = 5;
    String address = "California State University, Long Beach, Long Beach, CA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resturant_activity_main);
        setTitle("Single Activity");


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        SwipeLayout swipeLayout = (SwipeLayout)findViewById(R.id.resutrant_activity);
        swipeLayout.setDragEdge(SwipeLayout.DragEdge.Bottom);
//
//        TextView bottominformation = (TextView) findViewById(R.id.information);




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

                    Random r = new Random();

                    int randNum = r.nextInt(limit);
                    System.out.println("Random Number:  " + randNum + " limit: " + limit);
                    getYelpSearchResult(randNum, "Restaurant", singleCube);

                } catch (Exception e) {
                    System.out.println("full day set on click :Error -> " + e);
                    e.printStackTrace();
                }


            }
        });

//        bottominformation.setText("Address:\n " + GPSLocationService.currentLocation + "\nlimit:" + limit + "\n");
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
                Log.d(MainActivity.class.getName(), "click on surface");


            }
        });
        display.getSurfaceView().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(ResturantActivity.this, display.getId() + "longClick on surface", Toast.LENGTH_SHORT).show();
                Log.d(MainActivity.class.getName(), "longClick on surface");
                return true;
            }
        });
        display.findViewById(R.id.star2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ResturantActivity.this, display.getId() + "Star", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(ResturantActivity.this, display.getId() + "Magnifier", Toast.LENGTH_SHORT).show();
            }
        });

        display.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ResturantActivity.this, display.getId() + "Delete", Toast.LENGTH_SHORT).show();
            }
        });

    }


    public synchronized void getYelpSearchResult(final int index, final String term, final SwipeLayout display) throws InterruptedException{
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

            // Handle the camera action
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

}

