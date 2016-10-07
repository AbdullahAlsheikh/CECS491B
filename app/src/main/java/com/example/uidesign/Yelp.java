package com.example.uidesign;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

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

    OAuthService service;
    Token accessToken;
    int limit = 5;

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

    public void setLimit(int limitPar){
        limit = limitPar;
    }

    public int getLimit(){
        return limit;
    }

    /**
     * Search with term and location.
     *
     * @param term Search term
     * @param latitude Latitude
     * @param longitude Longitude
     * @return JSON string response
     */
    public String search(String term, double latitude, double longitude) {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://api.yelp.com/v2/search");
        request.addQuerystringParameter("term", term);
        request.addQuerystringParameter("ll", latitude + "," + longitude);
        request.addQuerystringParameter("limit", String.valueOf(limit));
        this.service.signRequest(this.accessToken, request);
        request.setConnectionKeepAlive(false);
        Response response = request.send();

        return response.getBody();
    }

    public String searchByLocation(String term, String address){

        OAuthRequest request = new OAuthRequest(Verb.GET, "http://api.yelp.com/v2/search/");
        request.addQuerystringParameter("term", term);
        request.addQuerystringParameter("location", address);
        request.addQuerystringParameter("limit", String.valueOf(limit));
        this.service.signRequest(this.accessToken, request);
        request.setConnectionKeepAlive(false);
        Response response = request.send();

        return response.getBody();
    }

    public String searchByLocation(String term, String address, double radius){
        //Validate radius
        if(radius > 24.85)
            radius = 40000;
        else if ( radius < 1) {
            radius = 1609.34;
        }
        else
            radius *= 1609.34;
        //Create a new request
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://api.yelp.com/v2/search");
        //Send the parameters
        request.addQuerystringParameter("term", term);
        request.addQuerystringParameter("location", address);
        request.addQuerystringParameter("limit", String.valueOf(limit));
        request.addQuerystringParameter("radius_filter", String.valueOf(radius));

        //Connect to yelp web service
        this.service.signRequest(this.accessToken, request);
        request.setConnectionKeepAlive(false);
        //Get a response
        Response response = request.send();
        //Return the response
        System.out.println(response.getBody());
        return response.getBody();
    }//End searchByLocation()




}
