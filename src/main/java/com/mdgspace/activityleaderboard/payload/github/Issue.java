package com.mdgspace.activityleaderboard.payload.github;

import java.io.Serializable;
import java.time.LocalDateTime;


import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Issue implements Serializable{
    

    @JsonProperty("title")
    public String title;

    @JsonProperty("user")
    public UserObject user;

    @JsonProperty("created_at")
    public LocalDateTime created_at;

    @JsonProperty("updated_at")
    public LocalDateTime updated_at;

}
