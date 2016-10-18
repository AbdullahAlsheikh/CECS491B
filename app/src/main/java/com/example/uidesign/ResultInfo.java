package com.example.uidesign;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Abdullah on 9/9/16.
 */

public class ResultInfo {

    private ArrayList<BussnessInfo> businesses = new ArrayList<BussnessInfo>();

    public BussnessInfo   getBussnessInfo(int index){

        try{

            businesses.get(index).icon_img = BitmapFactory.decodeStream(businesses.get(index).image_url.openConnection().getInputStream());
            businesses.get(index).rating_img = BitmapFactory.decodeStream(businesses.get(index).rating_img_url.openConnection().getInputStream());

        }catch (Exception e)
        {
            System.out.println("Could Not process Images " + " error -> " + e);
            businesses.get(index).icon_img  = null;
            businesses.get(index).rating_img  = null;
        }

        return businesses.get(index);
    }

//    public String getName(int index){ return businesses.get(index).name; }
//
//    public String getRating(int index) { return businesses.get(index).rating; }
//
//    public void setImages(int index){
//        try{
//
//            businesses.get(index).icon_img = BitmapFactory.decodeStream(businesses.get(index).image_url.openConnection().getInputStream());
//
//
//        }catch (Exception e)
//        {
//            businesses.get(index).icon_img  = null;
//        }
//    }
}


class BussnessInfo{
    //name Of Bussness
    public String name;
    //Rating of Business
    public String rating;
    //Location and it's sub information
    public mainlocation location;
    //URL of the Yelp acount(Web account)
    public String mobile_url;
    //The main Image of Business (String)
    public URL image_url;
    //Image of Business Rating (String)
    public URL rating_img_url;
    //The distance from address entered
    public double distance;
    //The phone numebr of bussness
    public String phone;
    //The main Image of Business (URL)
    public Bitmap icon_img;
    //The main Image of rating
    public Bitmap rating_img;
}

class mainlocation{
    List<String> display_address;

}
