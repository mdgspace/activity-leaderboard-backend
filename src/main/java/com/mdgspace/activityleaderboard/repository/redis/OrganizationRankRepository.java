package com.mdgspace.activityleaderboard.repository.redis;




import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.mdgspace.activityleaderboard.models.redis.OrganizationRank;

import java.util.Optional;

import com.mdgspace.activityleaderboard.models.Organization;


@Repository
public  interface OrganizationRankRepository extends CrudRepository<OrganizationRank, String>  {
     Optional<OrganizationRank>  findByOrganizationAndMonthly(Organization organization, Boolean monthly);
}
