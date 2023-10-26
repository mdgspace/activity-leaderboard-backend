package com.mdgspace.activityleaderboard.models.roles;

import com.mdgspace.activityleaderboard.models.Organization;
import com.mdgspace.activityleaderboard.models.User;
import com.mdgspace.activityleaderboard.models.enums.EOrgRole;
import com.mdgspace.activityleaderboard.models.keys.OrgRoleKey;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;

@Entity
public class OrgRole {

    @EmbeddedId
    private OrgRoleKey id;

    @ManyToOne
    @MapsId("organizationId")
    @JoinColumn(name="organization_id")
    private Organization organization;


    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name="user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private EOrgRole role;

    private Boolean bookmarked = false;

    private Boolean archeive= false;

    public OrgRole(){

    }

    public OrgRole(EOrgRole role,OrgRoleKey id){
        this.role=role;
        this.id=id;
    }
    
    

    public EOrgRole getRole(){
        return role;
    }

    public OrgRoleKey getId(){
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

    public void setId(OrgRoleKey id){
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
