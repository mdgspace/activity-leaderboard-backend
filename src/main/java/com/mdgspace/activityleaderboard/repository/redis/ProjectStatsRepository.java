package com.mdgspace.activityleaderboard.repository.redis;

import java.util.Map;
import java.util.Optional;

import com.mdgspace.activityleaderboard.models.Organization;
import com.mdgspace.activityleaderboard.models.Project;
import com.mdgspace.activityleaderboard.models.redis.ProjectStats;


public interface ProjectStatsRepository {
   
    void save(ProjectStats projectStats);
    void update(ProjectStats projectStats);
    Map<String,ProjectStats> findAll();
    Optional<ProjectStats> findByOrganizationAndProjectAndMonthly(Organization organization, Project project, Boolean monthly);

}
