package com.mdgspace.activityleaderboard.payload.github;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
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
    
    


}



