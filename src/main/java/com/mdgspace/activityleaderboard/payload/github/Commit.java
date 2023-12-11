package com.mdgspace.activityleaderboard.payload.github;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
public class Commit {
    
    @JsonProperty("committer")
    private Committer committer;

}
