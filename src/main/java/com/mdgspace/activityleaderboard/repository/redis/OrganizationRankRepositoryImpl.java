package com.mdgspace.activityleaderboard.repository.redis;

import java.util.Map;
import java.util.Optional;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Repository;

import com.mdgspace.activityleaderboard.models.Organization;
import com.mdgspace.activityleaderboard.models.redis.OrganizationRank;

import jakarta.annotation.Resource;

@Repository
public class OrganizationRankRepositoryImpl implements OrganizationRankRepository {
    
    private final String hashReference= "OrganizationRank";

    @Resource(name="redisTemplate")
    private HashOperations<String,String,OrganizationRank> hashOperations;

    @Override
    public void save(OrganizationRank organizationRank){
        hashOperations.putIfAbsent(hashReference, organizationRank.getId(), organizationRank);
    }

    @Override
    public Map<String, OrganizationRank> findAll(){
        return hashOperations.entries(hashReference);
    }

    @Override
    public void update(OrganizationRank organizationRank){
        hashOperations.put(hashReference, organizationRank.getId(), organizationRank);
    }

    @Override
    public Optional<OrganizationRank> findByOrganizationAndMonthly(Organization org, Boolean monthly){
            Map<String,OrganizationRank> map=hashOperations.entries(hashReference);
            for(Map.Entry<String,OrganizationRank> entry: map.entrySet()){
                OrganizationRank val=entry.getValue();
                if(org.equals(val.getOrganization())&& val.getMonthly()==monthly){
                    return Optional.of(val);
                }
            }
            return Optional.empty();
    }
}
