package com.mdgspace.activityleaderboard.repository;

import org.springframework.stereotype.Repository;

import java.util.Optional;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.mdgspace.activityleaderboard.models.Organization;
import com.mdgspace.activityleaderboard.models.Project;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findByName(String projectName);

    Boolean existsByName(String projectName);

    Boolean existsByLink(String link);

    Optional<Project> findByNameAndOrganization(String name, Organization organization);

    Boolean deleteByNameAndOrganization(String name, Organization organization);

   

}
