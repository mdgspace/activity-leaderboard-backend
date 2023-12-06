package com.mdgspace.activityleaderboard.models.redis;

import org.springframework.data.redis.core.RedisHash;

import com.mdgspace.activityleaderboard.models.Organization;
import com.mdgspace.activityleaderboard.payload.response.GetOrgStatsResponse;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@RedisHash("OrgStats")
public class OrgStats {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;
    
    private Organization organization;

    private Boolean monthly;

    private Long time;

    private  GetOrgStatsResponse response;

    public OrgStats(){

    }

    public OrgStats(Organization organization, GetOrgStatsResponse response, Boolean monthly, Long time){
        this.organization=organization;
        this.response=response;
        this.monthly=monthly;
        this.time=time;
        
    }


}
