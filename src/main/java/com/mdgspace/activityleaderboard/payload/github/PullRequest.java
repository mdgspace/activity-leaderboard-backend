package com.mdgspace.activityleaderboard.payload.github;

import java.time.LocalDateTime;
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
    private LocalDateTime created_at;

    @JsonProperty("updated_at")
    private LocalDateTime updated_at;
}
