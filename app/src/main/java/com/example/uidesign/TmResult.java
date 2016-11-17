package com.example.uidesign;

import java.util.ArrayList;

/**
 * Created by muhannad on 11/10/16.
 */

public class TmResult {
    public eventsInfo _embedded = new eventsInfo();
    public eventsSize page = new eventsSize();


    public basicInfo getevent(int index){
        basicInfo a = _embedded.getBusinnessInfo(index);
//        Log.i("result", "Name: " + a.name);
//        Log.i("result", "type: " + a.type);
//        Log.i("result", "url: " + a.url);
//        Log.i("result", "imageUrl: " + a.images.get(0).url);
//        Log.i("result", "name of venue: " + a._embedded.venues.get(0).name);
//        Log.i("result", "postal code of venue: " + a._embedded.venues.get(0).postalCode);

        return a;
    }
    public int getSizeOfResult(){
//        Log.i("result", "size of info: " + page.size);
        return page.size;
    }


}

class eventsInfo{
    public ArrayList<basicInfo> events = new ArrayList<>();

    public basicInfo getBusinnessInfo(int index){
        return events.get(index);
    }
}

class eventsSize{
    public int size;

}


class basicInfo{
    public String name;
    public String url;
    public String type;
    public ArrayList<imagesInfo> images = new ArrayList<>();
    public embeddedClass _embedded;
    public Dates dates;

}

class Dates {
    public StartDate start;
    // "America/Los_Angeles"
    public String timezone;
}

class StartDate {
    //"2016-11-12"
    String localDate;
    //"19:30:00"
    String localTime;
    //"2016-11-13T03:30:00Z"
    String dateTime;
}


class imagesInfo{
    String url;
}

class embeddedClass{
    public ArrayList<Venues> venues = new ArrayList<>();
}

class Venues {
    public String name;
    public String postalCode;
    public City city;
    public State state;
    public Address address;
}

class City {
    //long beach
    String name;
}

class State {
    //CA
    String stateCode;
}

class Address {
    //102 Pine Avenue
    String line1;
}