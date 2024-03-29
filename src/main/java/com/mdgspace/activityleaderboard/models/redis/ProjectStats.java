package com.mdgspace.activityleaderboard.models.redis;

import java.io.Serializable;

import org.springframework.data.redis.core.RedisHash;

import com.mdgspace.activityleaderboard.models.Organization;
import com.mdgspace.activityleaderboard.models.Project;
import com.mdgspace.activityleaderboard.payload.response.GetProjectStatsResponse;


import jakarta.persistence.Id;
import lombok.Data;

@Data
@RedisHash("ProjectStats")
public class ProjectStats implements Serializable {
    
    @Id
    private String id;

    private Organization organization;

    private Project project;

    private Boolean monthly;

    private Long time;

    private GetProjectStatsResponse response;

    public ProjectStats(){

    }
    public ProjectStats(String id,Organization org, Project project,GetProjectStatsResponse response ,Boolean monthly, Long time){
       this.organization=org;
       this.project=project;
       this.monthly=monthly;
       this.time=time;
       this.response=response;
       this.id=id;
    }



}
