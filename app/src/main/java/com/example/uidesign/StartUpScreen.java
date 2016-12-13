package com.example.uidesign;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;


public class StartUpScreen  extends AppCompatActivity {
    //private AccessTokenTracker accessTokenTracker;

    /**
     * OnStart finishes after onclick is closed
     * Starts another Activity
     */
    @Override
    public void onStart(){
        super.onStart();
        startService(new Intent(this, GPSLocationService.class));
    }
    /**
     * Main onCreate
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_up_screen);
        permissionAndLocation();

    }
    /**
     *  Requesting premission from user to get location Data
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case 1: {
                //
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //If first time accepting the user has to restart
                    Toast.makeText(StartUpScreen.this,"Please restart application for changes to take effect.", Toast.LENGTH_LONG).show();
                }//end if
                else{
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            final Intent mainIntent = new Intent(StartUpScreen.this, MainMenu.class);
                            StartUpScreen.this.startActivity(mainIntent);
                            StartUpScreen.this.finish();
                        }//
                    }, 3000);
                }//end else
            }//end case

        }//end switich

    }//end method
    /**
     * Asking for user premission
     * @return
     */
    private boolean permissionAndLocation(){
        //Ask user for premission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        }
        else if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {


            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startService(new Intent(StartUpScreen.this, GPSLocationService.class));
                    final Intent mainIntent = new Intent(StartUpScreen.this, MainMenu.class);
                    StartUpScreen.this.startActivity(mainIntent);
                    StartUpScreen.this.finish();
                }
            }, 3000);
        }
        return true;
    }
}