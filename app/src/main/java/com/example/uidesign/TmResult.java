package com.example.uidesign;

import java.util.ArrayList;

/**
 * Created by muhannad on 11/10/16.
 */

public class TmResult {
    //Ticket Master Gson Object Data for parsing purpuses
    public eventsInfo _embedded = new eventsInfo();
    public eventsSize page = new eventsSize();


    /**
     * Returns Data from TicketMaster
     * @param index
     * @return
     */
    public basicInfo getevent(int index){
        basicInfo a = _embedded.getBusinnessInfo(index);
        return a;
    }

    /**
     * Returns Size of the Result
     * @return
     */
    public int getSizeOfResult(){
        return page.size;
    }

}

class eventsInfo{
    //List of all events
    public ArrayList<basicInfo> events = new ArrayList<>();
    //events informaiton
    public basicInfo getBusinnessInfo(int index){
        return events.get(index);
    }
}

class eventsSize{
    //Returns size of event data
    public int size;
}


class basicInfo{
    //Basic Information of event
    public String name;
    public String url;
    public String type;
    public ArrayList<imagesInfo> images = new ArrayList<>();
    public embeddedClass _embedded;
    public Dates dates;

}

class Dates {
    //Returns Time of Events
    public StartDate start;
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
    //Event Image
    String url;
}

class embeddedClass{
    //List of venues Data
    public ArrayList<Venues> venues = new ArrayList<>();
}

class Venues {
    //Loaction Address
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