package com.mdgspace.activityleaderboard.payload.request;

import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AddProjectRequest implements Serializable{
    
    @NotBlank
    @Size(min=3,max=10)
    private String name;

    @NotBlank
    @Size(min=5,max=30)
    private String description;

    @NotBlank
    @Size(min=6,max=15)
    private String link;

    public String getName(){
        return name;
    }

    public String getDescription(){
        return description;
    }

    public String getLink(){
        return link;
    }

    public void setName(String name){
        this.name=name;
    }

    public void setLink(String link){
        this.link=link;
    }

    public void setDescription(String description){
        this.description=description;
    }

}
