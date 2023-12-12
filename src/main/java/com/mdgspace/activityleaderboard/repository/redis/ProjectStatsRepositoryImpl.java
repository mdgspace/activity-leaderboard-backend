package com.mdgspace.activityleaderboard.repository.redis;

import java.util.Map;
import java.util.Optional;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Repository;

import com.mdgspace.activityleaderboard.models.Organization;
import com.mdgspace.activityleaderboard.models.Project;
import com.mdgspace.activityleaderboard.models.redis.ProjectStats;

import jakarta.annotation.Resource;

@Repository
public class ProjectStatsRepositoryImpl implements ProjectStatsRepository{
    
    private final String hashReference="ProjectStats";

    @Resource(name="redisTemplate")
    private HashOperations<String, String, ProjectStats> hashOperations;

    @Override
    public void save(ProjectStats projectStats){
        hashOperations.putIfAbsent(hashReference, projectStats.getId(), projectStats);
    }

    @Override
    public Map<String , ProjectStats> findAll(){
        return hashOperations.entries(hashReference);
    }

    @Override
    public void update(ProjectStats projectStats){
        hashOperations.put(hashReference, projectStats.getId(), projectStats);
    }

    @Override
    public Optional<ProjectStats> findByOrganizationAndProjectAndMonthly(Organization org, Project project, Boolean monthly){
        Map<String, ProjectStats> map=hashOperations.entries(hashReference);
        for(Map.Entry<String, ProjectStats> entry: map.entrySet()){
            ProjectStats val=entry.getValue();
            if(org.equals(val.getOrganization())&&project.equals(val.getProject())&&val.getMonthly()==monthly){
                return Optional.of(val);
            }
        }
        return Optional.empty();
    }
}
