package com.mdgspace.activityleaderboard.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mdgspace.activityleaderboard.models.Organization;

@Repository
public interface OrgRepository extends JpaRepository<Organization,Long> {
    
    Optional<Organization> findByOrgname(String orgName);

    Boolean existsByOrgname(String orgName);
    
}
