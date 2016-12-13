package com.example.uidesign;
import org.scribe.model.Token;
import org.scribe.builder.api.DefaultApi10a;
/**
 * Created by Abdullah on 9/8/16.
 */
public class YelpApi2 extends DefaultApi10a  {
    /**
     * get Token endpoint
     * (this Class is not used anymore)
     * @return
     */
    @Override
    public String getAccessTokenEndpoint() {
        return null;
    }

    /**
     * Return Authorization URL
     * @param arg0
     * @return
     */
    @Override
    public String getAuthorizationUrl(Token arg0) {
        return null;
    }

    /**
     * Return request token
     * @return
     */
    @Override
    public String getRequestTokenEndpoint() {
        return null;
    }
}
