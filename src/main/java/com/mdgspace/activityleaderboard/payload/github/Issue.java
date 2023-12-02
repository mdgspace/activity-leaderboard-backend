package com.mdgspace.activityleaderboard.payload.github;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class Issue {
    

    @JsonProperty("title")
    public String title;

    @JsonProperty("user")
    public UserObject user;

    @JsonProperty("created_at")
    public Date created_at;

    @JsonProperty("updated_at")
    public Date updated_at;

}
