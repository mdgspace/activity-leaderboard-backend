package com.mdgspace.activityleaderboard.repository.redis;


import java.util.Map;
import java.util.Optional;

import com.mdgspace.activityleaderboard.models.Organization;
import com.mdgspace.activityleaderboard.models.redis.OrgStats;




public interface OrgStatsRepository {


   void save(OrgStats orgStats);
   Map<String,OrgStats> findAll();
   void update(OrgStats orgStats);
   Optional<OrgStats> findByOrganizationAndMonthly(Organization org, Boolean monthly);


}
