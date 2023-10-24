package com.mdgspace.activityleaderboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mdgspace.activityleaderboard.models.keys.ProjectRoleKey;
import com.mdgspace.activityleaderboard.models.roles.ProjectRole;

public interface ProjectRoleRepository extends JpaRepository<ProjectRole,ProjectRoleKey> {
    
}
