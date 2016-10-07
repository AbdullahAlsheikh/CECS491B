package com.example.uidesign;
import org.scribe.model.Token;
import org.scribe.builder.api.DefaultApi10a;
/**
 * Created by Abdullah on 9/8/16.
 */
public class YelpApi2 extends DefaultApi10a  {
    @Override
    public String getAccessTokenEndpoint() {
        return null;
    }

    @Override
    public String getAuthorizationUrl(Token arg0) {
        return null;
    }

    @Override
    public String getRequestTokenEndpoint() {
        return null;
    }
}
