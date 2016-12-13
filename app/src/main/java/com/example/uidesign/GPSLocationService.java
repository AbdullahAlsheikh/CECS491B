package com.example.uidesign;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

import java.io.IOException;
import java.util.List;


public class GPSLocationService extends Service
{
    //Variables used in the GPSLocationService class
    private static final String TAG = "GPSService";
    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 0;
    private static final float LOCATION_DISTANCE = 0;
    public static String currentLocation = null;
    public static String currentCity = null;

    //Inner class as a listener to handle gps actions
    private class LocationListener implements android.location.LocationListener
    {

        //Location object to store use location
        Location mLastLocation;

        //setter of the location object
        public LocationListener(String provider)
        {
            mLastLocation = new Location(provider);
        }

        /**
         * onLocationChanged
         * Description: This method is a listener that is triggered whenever the gps senses the user has physically moved.
         * it then calls the reverseGeocode function to decipher the location object into a string containing the user's address location.
         * @param location
         */
        @Override
        public void onLocationChanged(Location location)
        {
            mLastLocation.set(location);

            try {
                currentLocation = reverseGeocode(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            } catch(IOException ie){}
        }

        //unimplemented function.. required to state but not necessary to use
        @Override
        public void onProviderDisabled(String provider)
        {

        }

        //unimplemented function.. required to state but not necessary to use
        @Override
        public void onProviderEnabled(String provider)
        {

        }

        //unimplemented function.. required to state but not necessary to use
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras)
        {

        }
    }

    //Sets the location listeners for both the gps and network antenna
    LocationListener[] mLocationListeners = new LocationListener[] {
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };

    //used to bind to the gps satellite
    @Override
    public IBinder onBind(Intent arg0)
    {
        return null;
    }

    //Used to start up since this class is really a service running in the background of the application
    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    /**
     * onCreate
     * Description: The method that creates the class attributes on initialization
     */
    @Override
    public void onCreate()
    {
        initializeLocationManager();
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[1]);
        } catch (java.lang.SecurityException ex) {
        } catch (IllegalArgumentException ex) {
        }
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[0]);
        } catch (java.lang.SecurityException ex) {

        } catch (IllegalArgumentException ex) {

        }
    }

    //Ensures that the gps service truly turns off when the appliction has exited. Otherwise the gps service would continue
    //to run regardless of whether the application was alive or not.
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
                    mLocationManager.removeUpdates(mLocationListeners[i]);
                }  catch(SecurityException se){

                }
            }
        }
    }

    //Manages location objects as they come from the gps
    private void initializeLocationManager() {
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }


    /**
     * reverseGeocode
     * Description: When provided a lat/long position, the function runs it through the geocoder class built into android
     * which gives the elements of an address(ex: city/zipcode). From there it builds an address in an order that will work with the
     * yelp api.
     * @param latitude
     * @param longitude
     * @return
     * @throws IOException
     */
    public String reverseGeocode(double latitude, double longitude) throws IOException {
        Geocoder gc = new Geocoder(this);

        if (gc.isPresent()) {
            List<Address> list = gc.getFromLocation(latitude, longitude, 1);

            //(latitude, longitude, 1)
            //33.777043, -118.114395, 1)

            Address address = list.get(0);


            StringBuffer str = new StringBuffer();

            if (address.getAddressLine(0) != null && address.getLocality() != null &&
                    address.getAdminArea() != null && address.getPostalCode() != null &&
                    address.getCountryName() != null) {


                String strAddress = (address.getAddressLine(0) + ", " + address.getLocality() + ", " + address.getAdminArea() + " " + address.getPostalCode() + ", " + "USA");
                currentCity = address.getLocality();

                return strAddress;
            } else {
                return null;
            }
        }

        return null;
    }




}