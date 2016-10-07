package com.example.uidesign;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Abdullah on 9/9/16.
 */

public class ResultInfo {

    private ArrayList<BussnessInfo> businesses = new ArrayList<BussnessInfo>();

    public BussnessInfo   getBussnessInfo(int index){

        try{

            businesses.get(index).Iconimg = BitmapFactory.decodeStream(businesses.get(index).image_url.openConnection().getInputStream());


        }catch (Exception e)
        {
            System.out.println("Could Not process Image " + " error -> " + e);
            businesses.get(index).Iconimg  = null;
        }

        return businesses.get(index);
    }

    public String getName(int index){ return businesses.get(index).name; }

    public String getRating(int index) { return businesses.get(index).rating; }

    public void setImages(int index){
        try{

            businesses.get(index).Iconimg = BitmapFactory.decodeStream(businesses.get(index).image_url.openConnection().getInputStream());


        }catch (Exception e)
        {
            businesses.get(index).Iconimg  = null;
        }
    }

}


class BussnessInfo{


    //name Of Bussness
    public String name;
    //Rating of Business
    public String rating;
    //URL of the Yelp acount(Web account)
    public String mobile_url;
    //The main Image of Business (String)
    public URL image_url;
    //The main Image of Business (URL)
    public Bitmap Iconimg;
}