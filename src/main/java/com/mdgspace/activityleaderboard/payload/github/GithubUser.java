package com.mdgspace.activityleaderboard.payload.github;

import com.fasterxml.jackson.annotation.JsonProperty;

//  For deserializing githubapi/user response
public class GithubUser {
    
    @JsonProperty("login")
    private String username;

    public GithubUser(){

    }
//  Getters and setters
    public String getUsername(){
        return username;
    }

    public void setUsername(String username){
        this.username=username;
    }
}
