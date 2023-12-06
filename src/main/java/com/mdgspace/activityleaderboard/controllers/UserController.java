package com.mdgspace.activityleaderboard.controllers;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
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
import com.mdgspace.activityleaderboard.payload.request.SetArcheiveStatusRequest;
import com.mdgspace.activityleaderboard.payload.request.SetBookmarkStatusRequest;
import com.mdgspace.activityleaderboard.payload.response.GetUsersOrgs;
import com.mdgspace.activityleaderboard.payload.response.GetUsersProjectsResponse;
import com.mdgspace.activityleaderboard.payload.response.MessageResponse;
import com.mdgspace.activityleaderboard.payload.response.UsersResponse;
import com.mdgspace.activityleaderboard.repository.OrgRepository;
import com.mdgspace.activityleaderboard.repository.OrgRoleRepository;
import com.mdgspace.activityleaderboard.repository.ProjectRepository;
import com.mdgspace.activityleaderboard.repository.ProjectRoleRepository;
import com.mdgspace.activityleaderboard.repository.UserRepository;


import jakarta.validation.Valid;



@CrossOrigin(origins = "*", maxAge = 3600)
@RestController

@RequestMapping("/api/protected/user")
public class UserController {
     private static final Logger logger = LoggerFactory.getLogger(UserController.class);

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

     @GetMapping("/getUser")
     public ResponseEntity<?> getLogginedUser(Principal principal) {
          try {

               String username = principal.getName();
               Boolean isUser = userRepository.existsByUsername(username);
               if (!isUser) {
                    return ResponseEntity.badRequest().body(new MessageResponse("Invalid token"));
               }

               return ResponseEntity.ok().body(new MessageResponse(username));

          } catch (Exception e) {
               logger.error("Internal Server Error;", e);
               return ResponseEntity.internalServerError().body(new MessageResponse("Internal server error"));
          }
     }

     @GetMapping("/all")
     public ResponseEntity<?> getAllUsers() {
          try {
               List<User> users = userRepository.findAll();
               return ResponseEntity.ok().body(new UsersResponse(users));

          } catch (Exception e) {
               return ResponseEntity.internalServerError().body(new MessageResponse("Internal Server Error"));
          }
     }

     @PutMapping("/setBookmarkStatus")
     public ResponseEntity<?> setBookMarkStatus(
               @Valid @RequestBody SetBookmarkStatusRequest setBookmarkStatusRequest,
               Principal principal) {
          try {
               String username = principal.getName();
               User user = userRepository.findByUsername(username).orElse(null);
               Map<String, Boolean> newStatus = setBookmarkStatusRequest.getBookmarkStatus();
               for (Map.Entry<String, Boolean> e : newStatus.entrySet()) {
                    String org_name = e.getKey();
                    Boolean status = e.getValue();
                    Organization org=orgRepository.findByName(org_name).orElse(null);
                    if(org==null){
                         continue;
                    }
                    OrgRole orgRole=orgRoleRepository.findByOrganizationAndUser(org, user).orElse(null);
                    if(orgRole==null){
                         continue;
                    }
                    orgRole.setBookmarked(status);  

               }

               return ResponseEntity.ok().body("Bookmark status changed successfully");

          } catch (Exception e) {
            logger.error("Internal Server Error", e);
            return ResponseEntity.internalServerError().body("Internal Server Error");

          }
     }


     @PutMapping("/setArcheiveStatus")
     public ResponseEntity<?> setArcheiveStatus(
               @Valid @RequestBody SetArcheiveStatusRequest setArcheiveStatusRequest,
               Principal principal) {
          try {
               String username = principal.getName();
               User user = userRepository.findByUsername(username).orElse(null);
               Map<String, Boolean> newStatus = setArcheiveStatusRequest.getArcheiveStatus();
               for (Map.Entry<String, Boolean> e : newStatus.entrySet()) {
                    String org_name = e.getKey();
                    Boolean status = e.getValue();
                    Organization org=orgRepository.findByName(org_name).orElse(null);
                    if(org==null){
                         continue;
                    }
                    OrgRole orgRole=orgRoleRepository.findByOrganizationAndUser(org, user).orElse(null);
                    if(orgRole==null){
                         continue;
                    }
                    orgRole.setArcheive(status);;  

               }

               return ResponseEntity.ok().body("Archeive status changed successfully");

          } catch (Exception e) {
            logger.error("Internal Server Error", e);
            return ResponseEntity.internalServerError().body("Internal Server Error");

          }
     }

     @GetMapping("/getUserOrgs/{userName}")
     public ResponseEntity<?> getUserOrgs(@PathVariable String userName){
          try{
            User user =userRepository.findByUsername(userName).orElse(null);
            if(user==null){
               return ResponseEntity.badRequest().body("User with username does not exists");
            }
            List<OrgRole> orgRoles=orgRoleRepository.findByUser(user);
            Map<String,String> response = new HashMap<>();
            for(OrgRole orgRole:orgRoles){
               Organization org=orgRole.getOrganization();
               if(orgRole.getRole()==EOrgRole.ADMIN){
                    response.put(org.getName(),"admin");
               }
               else if(orgRole.getRole()==EOrgRole.MANAGER){
                    response.put(org.getName(), "manager");
               }else{
                    response.put(org.getName(), "member");
               }
            }
            return ResponseEntity.ok().body(new GetUsersOrgs(response));
          }catch(Exception e){
            logger.error("Internal Server Error", e);
            return ResponseEntity.internalServerError().body("Internal Server Error");
          }
     }

     @GetMapping("/getUsersProjects/{userName}")
     public ResponseEntity<?> getUsersProjects(@PathVariable String userName){
          try{

               User user=userRepository.findByUsername(userName).orElse(null);
               if(user==null){
                    return ResponseEntity.badRequest().body("User with this username does not exists");
               }
               List<ProjectRole> projectRoles= projectRoleRepository.findByUser(user);
               Map<String,String> response= new HashMap<>();
               for(ProjectRole role:projectRoles){
                 Project project= role.getProject();
                 if(role.getRole()==EProjectRole.ADMIN){
                    response.put(project.getName(), "admin");
                 }else{
                    response.put(project.getName(), "member");
                 }
                 
               }
               return ResponseEntity.ok().body(new GetUsersProjectsResponse(response));
          }catch(Exception e){
               logger.error("Internal Server Error", e);
               return ResponseEntity.internalServerError().body("Internal Server Error");
          }
     }

}
