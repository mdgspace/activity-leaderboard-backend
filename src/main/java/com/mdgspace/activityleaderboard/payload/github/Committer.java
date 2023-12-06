package com.mdgspace.activityleaderboard.payload.github;

import java.time.LocalDateTime;


import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class Committer {
    
    @JsonProperty("name")
    private String name;

    @JsonProperty("email")
    private String email;

    @JsonProperty("date")
    private LocalDateTime date;

    @JsonProperty("login")
    private String username;
}
