package com.mdgspace.activityleaderboard.controllers;

import java.security.Principal;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mdgspace.activityleaderboard.models.Organization;
import com.mdgspace.activityleaderboard.models.Project;
import com.mdgspace.activityleaderboard.models.User;
import com.mdgspace.activityleaderboard.models.enums.EOrgRole;
import com.mdgspace.activityleaderboard.models.enums.EProjectRole;
import com.mdgspace.activityleaderboard.models.roles.OrgRole;
import com.mdgspace.activityleaderboard.models.roles.ProjectRole;
import com.mdgspace.activityleaderboard.payload.request.AddProjectRequest;
import com.mdgspace.activityleaderboard.payload.response.MessageResponse;
import com.mdgspace.activityleaderboard.repository.OrgRepository;
import com.mdgspace.activityleaderboard.repository.OrgRoleRepository;
import com.mdgspace.activityleaderboard.repository.ProjectRepository;
import com.mdgspace.activityleaderboard.repository.ProjectRoleRepository;
import com.mdgspace.activityleaderboard.repository.UserRepository;
import com.mdgspace.activityleaderboard.security.jwt.AuthEntryPointJwt;

import io.netty.handler.codec.MessageAggregationException;
import jakarta.validation.Valid;


@CrossOrigin(origins = "*",maxAge=3600)
@RestController

@RequestMapping("/api/protected/project")
public class ProjectController {
    private static final Logger logger = LoggerFactory.getLogger(ProjectController.class);


    @Autowired
    UserRepository userRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    OrgRepository orgRepository;

    @Autowired
    OrgRoleRepository orgRoleRepository;

    @Autowired
    ProjectRoleRepository projectRoleRepository;


    @PostMapping("/add/{orgName}")
    public ResponseEntity<?> addProject(@Valid @RequestBody AddProjectRequest addProjectRequest,@PathVariable String orgName,Principal principal){
        try{
         
           String username=principal.getName();

           Organization org= orgRepository.findByName(orgName).orElse(null);

           if(org==null){
             return ResponseEntity.badRequest().body(new MessageResponse("This organisation doesnot exists"));
           }
           
           User user= userRepository.findByUsername(username).orElse(null);

           OrgRole orgRole=orgRoleRepository.findByOrganizationAndUser(org, user).orElse(null);
           if(orgRole==null){
            return ResponseEntity.badRequest().body(new MessageAggregationException("User doesnot belongs to Organization"));
           }
           if(orgRole.getRole()!=EOrgRole.ADMIN){
            return ResponseEntity.badRequest().body(new MessageResponse("User is not the admin of the Organization"));
           }

           Project isProject= projectRepository.findByNameAndOrganization(addProjectRequest.getName(), org).orElse(null);
           if(isProject!=null){
            return ResponseEntity.badRequest().body("Project Name Already Taken");
           }
           
           Project new_Project=new Project(addProjectRequest.getName(), addProjectRequest.getLink(),addProjectRequest.getDescription(), org);

           projectRepository.save(new_Project);

           if(org.getName()==username+"/userspace"){
            
            ProjectRole projectRole= new ProjectRole(EProjectRole.ADMIN, new_Project,user);
            projectRoleRepository.save(projectRole);
           }

          return ResponseEntity.ok().body("Project added to this organization successfully");

           
        }catch (Exception e){
          logger.error("Internal server error:", e);
          return ResponseEntity.internalServerError().body("Internal Server Error");

        }
    }


    @DeleteMapping("/delete/{projectName}/{orgName}")
    public ResponseEntity<?> deleteProject(@PathVariable String projectName,@PathVariable String orgName,Principal principal){
      try{

        String username=principal.getName();
        Organization org= orgRepository.findByName(orgName).orElse(null);
        User user= userRepository.findByUsername(username).orElse(null);
        if(org==null){
             return ResponseEntity.badRequest().body(new MessageResponse("This organisation doesnot exists"));
        }
        Project project= projectRepository.findByNameAndOrganization(projectName, org).orElse(null);
        if(project==null){
          return ResponseEntity.badRequest().body(new MessageResponse("This projectName does not exists in this organization"));
        }

        OrgRole orgRole=orgRoleRepository.findByOrganizationAndUser(org, user).orElse(null);
        if(orgRole.getRole()!=EOrgRole.ADMIN){
          return ResponseEntity.badRequest().body(new MessageResponse("User is not the admin of organization"));
        }

        projectRepository.deleteById(project.getId());
        
        ProjectRole projectRole= projectRoleRepository.findByProjectAndUser(project,user).orElse(null);

        if(projectRole!=null){
          projectRoleRepository.deleteById(projectRole.getId());
        }
        
        return ResponseEntity.ok().body(new MessageResponse("Project deleted successfully"));

      }catch (Exception e){
          logger.error("Internal server error", e);
          return ResponseEntity.internalServerError().body(new MessageResponse("Internal Server Error"));
      }
    }

}