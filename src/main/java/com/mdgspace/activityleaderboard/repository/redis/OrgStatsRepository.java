package com.mdgspace.activityleaderboard.repository.redis;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mdgspace.activityleaderboard.models.Organization;
import com.mdgspace.activityleaderboard.models.redis.OrgStats;

@Repository
public interface OrgStatsRepository extends JpaRepository<OrgStats, String> {
   Optional<OrgStats> findByOrganizationAndMonthly(Organization organization, Boolean monthly);
    
} 
