package com.mdgspace.activityleaderboard.payload.github;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class ContributorsStats {
    
    @JsonProperty("total")
    public int total;

    @JsonProperty("weeks")
    public ArrayList<Week> weeks;

    @JsonProperty("author")
    public UserObject user;
}
