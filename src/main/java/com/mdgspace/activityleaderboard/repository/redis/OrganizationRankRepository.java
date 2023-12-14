package com.mdgspace.activityleaderboard.repository.redis;

import java.util.Map;
import java.util.Optional;

import com.mdgspace.activityleaderboard.models.Organization;
import com.mdgspace.activityleaderboard.models.redis.OrganizationRank;

public interface OrganizationRankRepository {
    
    void save(OrganizationRank organizationRank);
    void update(OrganizationRank organizationRank);
    Map<String,OrganizationRank> findAll();
    Optional<OrganizationRank> findByOrganizationAndMonthly(Organization organization, Boolean monthly);
}
