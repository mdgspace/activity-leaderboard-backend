package com.mdgspace.activityleaderboard.models.redis;

import java.io.Serializable;



import org.springframework.data.redis.core.RedisHash;

import com.mdgspace.activityleaderboard.models.Organization;
import com.mdgspace.activityleaderboard.payload.response.GetProjectStatsResponse;



import jakarta.persistence.Id;
import lombok.Data;


@Data
@RedisHash("OrganizationRank")
public class OrganizationRank implements Serializable{

    @Id
    private String id;

    private Organization organization;

    private GetProjectStatsResponse response;

    private Boolean monthly;

    private Long time;

    public OrganizationRank(){

    }
    public OrganizationRank(String id,Organization organization, GetProjectStatsResponse response, Boolean monthly, Long time){
        this.organization=organization;
        this.monthly=monthly;
        this.response=response;
        this.time=time;
        this.id=id;
    }


}
