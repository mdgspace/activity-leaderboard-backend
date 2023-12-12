package com.mdgspace.activityleaderboard.repository.redis;

import java.util.Map;
import java.util.Optional;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Repository;

import com.mdgspace.activityleaderboard.models.Organization;
import com.mdgspace.activityleaderboard.models.redis.OrgStats;

import jakarta.annotation.Resource;

@Repository
public class OrgStatsRepositoryImpl implements OrgStatsRepository {
    
    private final String hashReference= "OrgStats";

    @Resource(name="redisTemplate")
    private HashOperations<String,String,OrgStats> hashOperations;

    @Override
    public void save(OrgStats orgStats){
        hashOperations.putIfAbsent(hashReference, orgStats.getId(), orgStats);
    }

    @Override
    public Map<String,OrgStats> findAll(){
        return hashOperations.entries(hashReference);
    }

    @Override 
    public void update(OrgStats orgStats){
        hashOperations.put(hashReference,orgStats.getId(), orgStats);
    }

    @Override
    public Optional<OrgStats> findByOrganizationAndMonthly(Organization org, Boolean monthly){
         Map<String,OrgStats> map=hashOperations.entries(hashReference);
         for(Map.Entry<String,OrgStats> entry:map.entrySet()){
            OrgStats val=entry.getValue();
            if(org.equals(val.getOrganization())&&val.getMonthly()==monthly){
                return Optional.of(val);
            }
         }
         return Optional.empty();
    }
}
