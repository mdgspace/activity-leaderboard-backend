package com.mdgspace.activityleaderboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mdgspace.activityleaderboard.models.keys.OrgRoleKey;
import com.mdgspace.activityleaderboard.models.roles.OrgRole;

public interface OrgRoleRepository extends JpaRepository<OrgRole,OrgRoleKey>{
    
}
