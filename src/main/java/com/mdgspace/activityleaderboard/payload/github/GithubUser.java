package com.mdgspace.activityleaderboard.payload.github;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;

//  For deserializing githubapi/user response


@AllArgsConstructor
public class GithubUser implements Serializable {
    
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
