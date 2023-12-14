package com.mdgspace.activityleaderboard.payload.github;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;


//  For deserializing access_token github response
public class Accesstoken implements Serializable{

    @JsonProperty("access_token")
    private String access_token;

    @JsonProperty("token_type")
    private String token_type;

    public Accesstoken(){
     
    }

    //  Getters and setters
    public String getAccesstoken(){
        return access_token;
    }

    public void setAccesstoken(String acess_token){
        this.access_token=acess_token;
    }

    public String getType(){
        return token_type;
    }

    public void setType(String type){
        this.token_type=type;
    }

    // For testing purpose
    @Override
    public String toString(){
        return "Accesstoken: "+access_token+" ,type: " +token_type;
    }
}
