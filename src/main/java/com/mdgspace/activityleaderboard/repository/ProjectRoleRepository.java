package com.mdgspace.activityleaderboard.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mdgspace.activityleaderboard.models.Project;
import com.mdgspace.activityleaderboard.models.User;
import com.mdgspace.activityleaderboard.models.roles.ProjectRole;
import java.util.List;


public interface ProjectRoleRepository extends JpaRepository<ProjectRole,Long> {
    Optional<ProjectRole> findByProjectAndUser(Project project,User user);
    void deleteByProjectAndUser(Project project,User user);
    List<ProjectRole> findByUser(User user);

}
