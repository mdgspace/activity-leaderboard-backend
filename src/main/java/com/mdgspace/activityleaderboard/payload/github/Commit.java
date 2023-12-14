package com.mdgspace.activityleaderboard.payload.github;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;


import lombok.Data;



@Data
public class Commit implements Serializable {
    
    @JsonProperty("committer")
    private Committer committer;

}
