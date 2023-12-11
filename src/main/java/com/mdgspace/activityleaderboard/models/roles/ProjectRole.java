package com.mdgspace.activityleaderboard.models.roles;

import java.io.Serializable;

import com.mdgspace.activityleaderboard.models.Project;
import com.mdgspace.activityleaderboard.models.User;
import com.mdgspace.activityleaderboard.models.enums.EProjectRole;



import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;


@Entity
public class ProjectRole implements Serializable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="project_id")
    private  Project project;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private EProjectRole role;

    public ProjectRole(){

    }

    public ProjectRole(EProjectRole role,Project project,User user){
        this.role=role;
        this.project=project;
        this.user=user;
       
    }

    public EProjectRole getRole(){
        return role;
    }

    public Long getId(){
        return id;
    }

    public Project getProject(){
        return project;
    }

    public User getUser(){
        return user;
    }

    public void setId(Long id){
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
