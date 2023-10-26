package com.mdgspace.activityleaderboard.models.keys;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;


@Embeddable
public class ProjectRoleKey implements Serializable {
    
    @Column(name="project_id")
    private Long projectId;

    @Column(name="user_id")
    private Long userId;

    public ProjectRoleKey(){

    }
    public ProjectRoleKey(Long projectId, Long userId){
       this.projectId= projectId;
       this.userId=userId;
    }

    
    public Long getProjectId(){
        return projectId;
    }

    public Long getuserId(){
        return userId;
    }

    public void setProjectId(Long projectId){
        this.projectId=projectId;
    }

    public void setuserId(Long userId){
        this.userId=userId;
    }


    
}
