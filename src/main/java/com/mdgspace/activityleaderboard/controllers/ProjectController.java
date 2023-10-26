package com.mdgspace.activityleaderboard.controllers;

import java.security.Principal;
import java.util.Optional;
import java.util.Set;

import org.aspectj.bridge.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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
import com.mdgspace.activityleaderboard.models.keys.ProjectRoleKey;
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
    private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);


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


    // @PostMapping("/add/{orgName}")
    // public ResponseEntity<?> addProject(@Valid @RequestBody AddProjectRequest addProjectRequest,@PathVariable String orgName,Principal principal){
    //     try{
         
    //        String username=principal.getName();

    //        Organization org= orgRepository.findByName(orgName).orElse(null);

    //        if(org==null){
    //          return ResponseEntity.badRequest().body(new MessageResponse("This organisation doesnot exists"));
    //        }
           
    //        User user= userRepository.findByUsername(username).orElse(null);
    //        Set<OrgRole> orgRoles=org.getOrgRoles();

    //        OrgRole orgRole=orgRoleRepository.findByOrganizationAndUser(org, user).orElse(null);
    //        if(orgRole==null){
    //         return ResponseEntity.badRequest().body(new MessageAggregationException("User doesnot belongs to Organization"));
    //        }
    //        if(orgRole.getRole()!=EOrgRole.ADMIN){
    //         return ResponseEntity.badRequest().body(new MessageResponse("User is not the admin of the Organization"));
    //        }
           
    //        Project new_Project=new Project(addProjectRequest.getName(), addProjectRequest.getLink(),addProjectRequest.getDescription(), org);

    //        projectRepository.save(new_Project);

    //        if(org.getName()==username+"-space"){
    //         ProjectRoleKey projectRoleKey= new ProjectRoleKey(new_Project.getId(),user.getId());
    //         ProjectRole projectRole= new ProjectRole(EProjectRole.ADMIN, projectRoleKey);
    //         projectRoleRepository.save(projectRole);
    //         Set<ProjectRole> project_roles=new_Project.getProjectRoles();


    //        }



           
    //     }catch (Exception e){

    //     }
    // }




}
