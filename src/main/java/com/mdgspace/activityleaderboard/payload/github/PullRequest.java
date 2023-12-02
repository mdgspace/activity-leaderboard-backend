package com.mdgspace.activityleaderboard.payload.github;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class PullRequest {
    
    @JsonProperty("user")
    private UserObject user;

    @JsonProperty("title")
    private String title;

    @JsonProperty("created_at")
    private Date created_at;

    @JsonProperty("updated_at")
    private Date updated_at;
}
