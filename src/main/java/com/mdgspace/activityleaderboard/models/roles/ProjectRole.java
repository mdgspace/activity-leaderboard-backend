package com.mdgspace.activityleaderboard.models.roles;

import com.mdgspace.activityleaderboard.models.Project;
import com.mdgspace.activityleaderboard.models.User;
import com.mdgspace.activityleaderboard.models.enums.EProjectRole;
import com.mdgspace.activityleaderboard.models.keys.ProjectRoleKey;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;

@Entity
public class ProjectRole {
    
    @EmbeddedId
    private ProjectRoleKey id;

    @ManyToOne
    @MapsId("projectId")
    @JoinColumn(name="project_id")
    private  Project project;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name="user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private EProjectRole role;

    public ProjectRole(){

    }

    public ProjectRole(EProjectRole role,ProjectRoleKey id){
        this.role=role;
        this.id=id;
    }

    public EProjectRole getRole(){
        return role;
    }

    public ProjectRoleKey getId(){
        return id;
    }

    public Project getProject(){
        return project;
    }

    public User getUser(){
        return user;
    }

    public void setId(ProjectRoleKey id){
        this.id=id;
    }
    public void setUser(User user){
        this.user=user;
    }

    public void setProject(Project project){
        this.project=project;
    }

    public void setRole(EProjectRole role){
        this.role=role;
    }
    


}
