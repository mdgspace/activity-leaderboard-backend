package com.mdgspace.activityleaderboard.repository;

import org.springframework.stereotype.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mdgspace.activityleaderboard.models.Project;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    Optional<Project> findByName(String projectName);

    Boolean existsByName(String projectName);

    Boolean existsByLink(String link);

}
