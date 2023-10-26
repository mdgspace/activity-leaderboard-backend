package com.mdgspace.activityleaderboard.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mdgspace.activityleaderboard.models.Organization;
import com.mdgspace.activityleaderboard.models.User;
import com.mdgspace.activityleaderboard.models.keys.OrgRoleKey;
import com.mdgspace.activityleaderboard.models.roles.OrgRole;

public interface OrgRoleRepository extends JpaRepository<OrgRole,OrgRoleKey>{
    Optional<OrgRole> findByOrganizationAndUser(Organization organization, User user);
}
