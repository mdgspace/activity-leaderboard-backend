package com.mdgspace.activityleaderboard.models.keys;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class OrgRoleKey implements Serializable {
 
    @Column(name="organoization_id")
    private Long organizationId;

    @Column(name = "user_id")
    private Long userId;

    public OrgRoleKey(){

    }

    public OrgRoleKey(Long organizationId, Long userId){
        this.organizationId=organizationId;
        this.userId=userId;
    }

    public Long getUserId(){
        return userId;
    }

    public Long getOrgnaizationId(){
        return organizationId;
    }

    public void setuserId(Long userId){
        this.userId=userId;
    }

    public void setOrganizationId(Long organizationId){
        this.organizationId=organizationId;
    }
}
