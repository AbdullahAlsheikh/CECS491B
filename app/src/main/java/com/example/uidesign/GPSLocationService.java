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

/**
 * Created by Abdullah on 9/27/16.
 */
public class GPSLocationService extends Service
{
    private static final String TAG = "GPSService";
    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 0;
    private static final float LOCATION_DISTANCE = 0;
    public static String currentLocation = null;
    public static String currentCity = null;

    private class LocationListener implements android.location.LocationListener
    {
        Location mLastLocation;

        public LocationListener(String provider)
        {
            mLastLocation = new Location(provider);
        }

        @Override
        public void onLocationChanged(Location location)
        {
            mLastLocation.set(location);

            try {
                currentLocation = reverseGeocode(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            } catch(IOException ie){}
        }

        @Override
        public void onProviderDisabled(String provider)
        {

        }

        @Override
        public void onProviderEnabled(String provider)
        {

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras)
        {

        }
    }

    LocationListener[] mLocationListeners = new LocationListener[] {
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };

    @Override
    public IBinder onBind(Intent arg0)
    {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

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

    private void initializeLocationManager() {
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }


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
                //str.append(address.getAddressLine(0) + ", ");
                //str.append(address.getLocality() + ", ");
                //str.append(address.getAdminArea() + " ");
                //str.append(address.getPostalCode() + ", ");
                //str.append(address.getCountryName());
                //str.append("USA");

                //String strAddress = str.toString();

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
