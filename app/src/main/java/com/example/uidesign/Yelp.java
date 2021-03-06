package com.example.uidesign;
import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;
/**
 * Created by Abdullah on 9/8/16.
 */
public class Yelp {



    //Data for sending request
    OAuthService service;
    Token accessToken;
    int limit = 19;
    double radius =  5.0;


    /**
     * Setup the Yelp API OAuth credentials.
     *
     * OAuth credentials are available from the developer site, under Manage API access (version 2 API).
     *
     * @param consumerKey Consumer key
     * @param consumerSecret Consumer secret
     * @param token Token
     * @param tokenSecret Token secret
     */
    public Yelp(String consumerKey, String consumerSecret, String token, String tokenSecret) {
        this.service = new ServiceBuilder().provider(YelpApi2.class).apiKey(consumerKey).apiSecret(consumerSecret).build();
        this.accessToken = new Token(token, tokenSecret);
    }

//    public void setLimit(int limitPar){
//        limit = limitPar;
//    }
//
//    public int getLimit(){
//        return limit;
//    }

    /**
     * Getting adjusted Radius
     * @param radiusPar
     */
    public void setRadius(double radiusPar){
        //Validate radius
        if(radiusPar > 24.85)
            radiusPar = 40000;
        else if ( radiusPar < 1) {
            radiusPar = 1609.34;
        }
        else{
            radiusPar *= 1609.34;
        }
        radius = radiusPar;
    }

    /**
     *
     * @return radius
     */
    public double getraduis(){return radius;}

    /**
     * Searching Yelp by user location
     * @param term
     * @param address
     * @return
     */
    public String searchByLocation(String term, String address){

        //Correcting radius
        if(radius > 24.85)
            radius = 40000;
        else if ( radius < 1) {
            radius = 1609.34;
        }
        else
        {
            radius *= 1609.34;
        }

        //Set yelp parameters
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://api.yelp.com/v2/search/");
        request.addQuerystringParameter("term", term);
        request.addQuerystringParameter("location", address);
        request.addQuerystringParameter("limit", "19");
        request.addQuerystringParameter("radius_filter", String.valueOf(radius));
        //Connect to yelp web service
        this.service.signRequest(this.accessToken, request);
        request.setConnectionKeepAlive(false);
        //Get a response
        Response response = request.send();
        //Return the response
        return response.getBody();
    }

}
