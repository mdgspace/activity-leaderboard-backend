package com.mdgspace.activityleaderboard.payload.github;

import java.io.Serializable;
import java.time.LocalDateTime;


import com.fasterxml.jackson.annotation.JsonProperty;


import lombok.Data;

@Data
public class Committer  implements Serializable{
    
    @JsonProperty("name")
    private String name;

    @JsonProperty("email")
    private String email;

    @JsonProperty("date")
    private LocalDateTime date;

    @JsonProperty("login")
    private String username;
}
