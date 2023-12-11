package com.mdgspace.activityleaderboard.payload.github;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;



public class Repository {
    

    @JsonProperty("name")
    private String name;

    @JsonProperty("fullname")
    private String fullname;

    @JsonProperty("private")
    private Boolean myPrivate;

    @JsonProperty("open_issues_count")
    private int open_issues_count;

    @JsonProperty("forks")
    private int forks;

    @JsonProperty("open_issues")
    private int open_issues;

    public String getName(){
        return name;
    }

    public String getFullName(){
        return fullname;
    }

    public Boolean getMyPrivate(){
        return myPrivate;
    }
    
    public int getOpen_issues_count(){
        return open_issues_count;
    }

    public int getForks(){
        return forks;
    }

    public int getOpen_issues(){
        return open_issues;
    }

    public void setName(String name){
        this.name=name;
    }

    public void setFullName(String fullname){
        this.fullname=fullname;
    }

    public void setMyPrivate(Boolean myPrivate){
        this.myPrivate=myPrivate;
    }

    public void setForks(int forks){
        this.forks=forks;
    }
    public void setOpen_issues_count(int open_issues_count){
       this.open_issues_count=open_issues_count;
    }
 
    public void setOpen_issues(int open_issues){
        this.open_issues=open_issues;
    }


}



