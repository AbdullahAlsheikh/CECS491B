package com.example.uidesign;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import android.view.Menu;
import android.view.MenuItem;
import com.google.gson.*;
import java.util.ArrayList;
import java.util.Random;
import com.daimajia.swipe.SwipeLayout;

public class MainActivity extends  AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

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
        setContentView(R.layout.activity_main);
        //Setting the Titile Bar Text
        setTitle("Full Day Activity");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        SwipeLayout swipeLayout = (SwipeLayout)findViewById(R.id.content_main);
        swipeLayout.setDragEdge(SwipeLayout.DragEdge.Bottom);

        TextView bottominformation = (TextView) findViewById(R.id.information);




        final SwipeLayout list_cube0 = (SwipeLayout) findViewById(R.id.sample1);
        list_cube0.setShowMode(SwipeLayout.ShowMode.PullOut);
        View starBottView = list_cube0.findViewById(R.id.starbott);

        list_cube0.addDrag(SwipeLayout.DragEdge.Left, list_cube0.findViewById(R.id.leftside));
        list_cube0.addDrag(SwipeLayout.DragEdge.Right, list_cube0.findViewById(R.id.rightside));
        list_cube0.addDrag(SwipeLayout.DragEdge.Top, starBottView);
        list_cube0.addDrag(SwipeLayout.DragEdge.Bottom, starBottView);
        PressedAction(list_cube0);


        final SwipeLayout list_cube = (SwipeLayout) findViewById(R.id.list_cube);
        list_cube.setShowMode(SwipeLayout.ShowMode.PullOut);
        View starBottView_2 = list_cube.findViewById(R.id.starbott);

        list_cube.addDrag(SwipeLayout.DragEdge.Left, list_cube.findViewById(R.id.leftside));
        list_cube.addDrag(SwipeLayout.DragEdge.Right, list_cube.findViewById(R.id.rightside));
        list_cube.addDrag(SwipeLayout.DragEdge.Top, starBottView_2);
        list_cube.addDrag(SwipeLayout.DragEdge.Bottom, starBottView_2);
        PressedAction(list_cube);


        final SwipeLayout list_cube2 = (SwipeLayout) findViewById(R.id.list_cube2);
        list_cube2.setShowMode(SwipeLayout.ShowMode.PullOut);
        View starBottView_3 = list_cube2.findViewById(R.id.starbott);

        list_cube2.addDrag(SwipeLayout.DragEdge.Left, list_cube2.findViewById(R.id.leftside));
        list_cube2.addDrag(SwipeLayout.DragEdge.Right, list_cube2.findViewById(R.id.rightside));
        list_cube2.addDrag(SwipeLayout.DragEdge.Top, starBottView_3);
        list_cube2.addDrag(SwipeLayout.DragEdge.Bottom, starBottView_3);
        PressedAction(list_cube2);


        final SwipeLayout list_cube3 = (SwipeLayout) findViewById(R.id.list_cube3);
        list_cube3.setShowMode(SwipeLayout.ShowMode.PullOut);
        View starBottView_4 = list_cube3.findViewById(R.id.starbott);

        list_cube3.addDrag(SwipeLayout.DragEdge.Left, list_cube3.findViewById(R.id.leftside));
        list_cube3.addDrag(SwipeLayout.DragEdge.Right, list_cube3.findViewById(R.id.rightside));
        list_cube3.addDrag(SwipeLayout.DragEdge.Top, starBottView_4);
        list_cube3.addDrag(SwipeLayout.DragEdge.Bottom, starBottView_4);
        PressedAction(list_cube3);


        final SwipeLayout list_cube4 = (SwipeLayout) findViewById(R.id.list_cube4);
        list_cube4.setShowMode(SwipeLayout.ShowMode.PullOut);
        View starBottView_5 = list_cube4.findViewById(R.id.starbott);

        list_cube4.addDrag(SwipeLayout.DragEdge.Left, list_cube4.findViewById(R.id.leftside));
        list_cube4.addDrag(SwipeLayout.DragEdge.Right, list_cube4.findViewById(R.id.rightside));
        list_cube4.addDrag(SwipeLayout.DragEdge.Top, starBottView_5);
        list_cube4.addDrag(SwipeLayout.DragEdge.Bottom, starBottView_5);
        PressedAction(list_cube4);


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
                    getYelpSearchResult(randNum, "breakfast", list_cube0);

                    randNum = r.nextInt(limit);
                    System.out.println("Random Number:  " + randNum + " limit: " + limit);
                    getYelpSearchResult(randNum, "Park", list_cube);

                    randNum = r.nextInt(limit);
                    System.out.println("Random Number:  " + randNum + " limit: " + limit);
                    getYelpSearchResult(randNum, "Lunch", list_cube2);

                    randNum = r.nextInt(limit);
                    System.out.println("Random Number:  " + randNum + " limit: " + limit);
                    getYelpSearchResult(randNum, "Activity", list_cube3);

                    randNum = r.nextInt(limit);
                    System.out.println("Random Number:  " + randNum + " limit: " + limit);
                    getYelpSearchResult(randNum, "Dinner", list_cube4);


                } catch (InterruptedException e) {
                    System.out.println("full day set on click :Error -> " + e);
                    e.printStackTrace();
                }


            }
        });

        bottominformation.setText("Address:\n " + GPSLocationService.currentLocation + "\nlimit:" + limit + "\n");
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
    public void PressedAction(final SwipeLayout display) {


        display.addRevealListener(R.id.delete, new SwipeLayout.OnRevealListener() {
            @Override
            public void onReveal(View child, SwipeLayout.DragEdge edge, float fraction, int distance) {

            }
        });

        display.getSurfaceView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, display.getId() + "Click on surface", Toast.LENGTH_SHORT).show();
                Log.d(MainActivity.class.getName(), "click on surface");


            }
        });
        display.getSurfaceView().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(MainActivity.this, display.getId() + "longClick on surface", Toast.LENGTH_SHORT).show();
                Log.d(MainActivity.class.getName(), "longClick on surface");
                return true;
            }
        });
        display.findViewById(R.id.star2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, display.getId() + "Star", Toast.LENGTH_SHORT).show();
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
            }
        });

        display.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, display.getId() + "Delete", Toast.LENGTH_SHORT).show();
            }
        });

    }


    public synchronized void getYelpSearchResult(final int index, final String term, final SwipeLayout display) throws InterruptedException{
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                System.out.println("Index:  " + index +  " Term:" + term);
                try{

                    //String response = yelp.searchByLocation(term, GPSLocationService.currentLocation);
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
//        int id = item.getItemId();
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

            System.out.println("within Full Day Activity");

            // Handle the camera action
        } else if (id == R.id.Second) {
            final Intent singleResturant = new Intent(MainActivity.this, ResturantActivity.class);
            MainActivity.this.startActivity(singleResturant);
            System.out.println("Second Button");

        } else if (id == R.id.Third) {
            System.out.println("Third Button");


        } else if (id == R.id.Fourth) {
            System.out.println("Fourth Button");


        } else if (id == R.id.sectionOne) {
            System.out.println("First Section Button");
            final Intent mainIntent = new Intent(MainActivity.this, Setting_Page.class);
            MainActivity.this.startActivity(mainIntent);


        } else if (id == R.id.sectionTwo) {
            System.out.println("Second Section Button");

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}




