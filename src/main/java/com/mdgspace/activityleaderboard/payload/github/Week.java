package com.mdgspace.activityleaderboard.payload.github;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class Week {
    


    @JsonProperty("w") 
    private int w;
    @JsonProperty("a")
    private int a;
    @JsonProperty("d")
    private int d;
    @JsonProperty("c")
    private int c;


}
