package com.mdgspace.activityleaderboard.payload.github;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GithubUser {
    
    @JsonProperty("login")
    private String username;

    public GithubUser(){

    }

    public String getUsername(){
        return username;
    }

    public void setUsername(String username){
        this.username=username;
    }
}
