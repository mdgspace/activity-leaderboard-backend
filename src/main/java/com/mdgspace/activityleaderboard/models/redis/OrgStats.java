package com.mdgspace.activityleaderboard.models.redis;

import java.io.Serializable;

import org.springframework.data.redis.core.RedisHash;

import com.mdgspace.activityleaderboard.models.Organization;
import com.mdgspace.activityleaderboard.payload.response.GetOrgStatsResponse;



import jakarta.persistence.Id;


@RedisHash("OrgStats")
public class  OrgStats implements Serializable{


    @Id
    private String id;
    
    private Organization organization;

    private Boolean monthly;

    private Long time;

    private  GetOrgStatsResponse response;

    public OrgStats(){

    }

    public OrgStats(String id,Organization organization, GetOrgStatsResponse response, Boolean monthly, Long time){
        this.organization=organization;
        this.response=response;
        this.monthly=monthly;
        this.time=time;
        this.id=id;        
    }

    public String getId(){
        return id;
    }

    public Organization getOrganization(){
        return organization;
    }

    public boolean getMonthly(){
        return monthly;
    }

    public Long getTime(){
        return time;
    }

    public GetOrgStatsResponse getResponse(){
        return response;
    }

    public void setId(String id){
        this.id=id;
    }

    public void setOrganization(Organization organization){
        this.organization=organization;
    }

    public void setMonthly(boolean monthly){
        this.monthly=monthly;
    }

    public void setTime(Long time){
        this.time=time;
    }

    public void setResponse(GetOrgStatsResponse response){
        this.response=response;
    }


}
