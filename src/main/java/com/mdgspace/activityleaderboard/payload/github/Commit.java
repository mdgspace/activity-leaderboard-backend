package com.mdgspace.activityleaderboard.payload.github;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class Commit {
    
    @JsonProperty("committer")
    private Committer committer;

}
