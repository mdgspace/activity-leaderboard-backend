package com.mdgspace.activityleaderboard.models.roles;

import java.io.Serializable;

import com.mdgspace.activityleaderboard.models.Organization;
import com.mdgspace.activityleaderboard.models.User;
import com.mdgspace.activityleaderboard.models.enums.EOrgRole;



import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;


@Entity
public class OrgRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="organization_id")
    private Organization organization;


    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private EOrgRole role;

    private Boolean bookmarked = false;

    private Boolean archeive= false;

    public OrgRole(){

    }

    public OrgRole(EOrgRole role,Organization organization, User user){
        this.role=role;
        this.organization=organization;
        this.user=user;
    }
    


    public EOrgRole getRole(){
        return role;
    }

    public Long getId(){
        return id;
    }

    public Organization getOrganization(){
        return organization;
    }

    public User getUser(){
        return user;
    }

    public Boolean getBookmarked(){
        return bookmarked;
    }

    public Boolean getArcheive(){
        return archeive;
    }

    public void setId(Long id){
        this.id=id;
    }

    public void setOrganization(Organization organization){
        this.organization=organization;
    }

    public void setUser(User user){
        this.user=user;
    }

    public void setRole(EOrgRole role){
        this.role=role;
    }


    public void setBookmarked(Boolean bookmarked){
        this.bookmarked=bookmarked;
    }

    public void setArcheive(Boolean archeive){
        this.archeive=archeive;
    }
    



}
