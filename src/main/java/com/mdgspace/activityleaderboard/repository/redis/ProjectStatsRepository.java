package com.mdgspace.activityleaderboard.repository.redis;

import java.util.Optional;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.mdgspace.activityleaderboard.models.Organization;
import com.mdgspace.activityleaderboard.models.Project;
import com.mdgspace.activityleaderboard.models.redis.ProjectStats;

@Repository
public interface ProjectStatsRepository extends CrudRepository<ProjectStats ,String>{
    Optional<ProjectStats> findfindByOrganizationAndProjectAndMonthly(Organization organization,Project project, Boolean monthly);
}
