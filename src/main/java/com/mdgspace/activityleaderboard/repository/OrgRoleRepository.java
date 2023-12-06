package com.mdgspace.activityleaderboard.repository;

import java.util.Optional;



import org.springframework.data.jpa.repository.JpaRepository;

import com.mdgspace.activityleaderboard.models.Organization;
import com.mdgspace.activityleaderboard.models.User;
import com.mdgspace.activityleaderboard.models.roles.OrgRole;
import java.util.List;


public interface OrgRoleRepository extends JpaRepository<OrgRole,Long>{
    Optional<OrgRole> findByOrganizationAndUser(Organization organization, User user);
    List<OrgRole> findByUser(User user);
    
}
