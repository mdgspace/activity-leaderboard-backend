package com.mdgspace.activityleaderboard.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mdgspace.activityleaderboard.models.Organization;
import com.mdgspace.activityleaderboard.models.Project;
import com.mdgspace.activityleaderboard.models.User;
import com.mdgspace.activityleaderboard.models.enums.EOrgRole;
import com.mdgspace.activityleaderboard.models.roles.OrgRole;
import com.mdgspace.activityleaderboard.models.roles.ProjectRole;
import com.mdgspace.activityleaderboard.payload.request.AddMembersRequest;
import com.mdgspace.activityleaderboard.payload.request.AddOrgRequest;
import com.mdgspace.activityleaderboard.payload.request.ChangeOrgMembersStatusRequest;
import com.mdgspace.activityleaderboard.payload.request.SetArcheiveStatusRequest;
import com.mdgspace.activityleaderboard.payload.request.SetBookmarkStatusRequest;
import com.mdgspace.activityleaderboard.payload.response.AddMembersResponse;
import com.mdgspace.activityleaderboard.payload.response.GetAllOrgsResponse;
import com.mdgspace.activityleaderboard.payload.response.GetMembersResponse;
import com.mdgspace.activityleaderboard.payload.response.GetProjectsResponse;
import com.mdgspace.activityleaderboard.payload.response.RemoveMembersResponse;
import com.mdgspace.activityleaderboard.repository.OrgRepository;
import com.mdgspace.activityleaderboard.repository.OrgRoleRepository;
import com.mdgspace.activityleaderboard.repository.ProjectRepository;
import com.mdgspace.activityleaderboard.repository.ProjectRoleRepository;
import com.mdgspace.activityleaderboard.repository.UserRepository;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PutMapping;



@CrossOrigin(origins = "*",maxAge=3600)
@RestController
@Slf4j
@RequestMapping("/api/protected/org")
@Validated
public class OrgController {
      
    @Autowired
    UserRepository userRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    OrgRoleRepository orgRoleRepository;

    @Autowired
    ProjectRoleRepository projectRoleRepository;

    @Autowired
    OrgRepository orgRepository;


    @PostMapping("/add")
    public ResponseEntity<?> addOrg(@Valid @RequestBody AddOrgRequest addOrgRequest, Principal principal){
        try{
            String username = principal.getName();
            User user= userRepository.findByUsername(username).orElse(null);
            if(!isValidName(addOrgRequest.getName())){
              return ResponseEntity.badRequest().body("Not a valid name");
            }
            Organization org= orgRepository.findByName(addOrgRequest.getName()).orElse(null);
            if(org!=null){
                return ResponseEntity.badRequest().body("Organization with this name already created, please select another name");
            }
            Organization new_org = new Organization(addOrgRequest.getName(),addOrgRequest.getDescription(),null); 
            orgRepository.save(new_org);
            OrgRole orgRole=new OrgRole(EOrgRole.ADMIN,new_org, user);
            orgRoleRepository.save(orgRole);
            return ResponseEntity.ok().body("Organization created successfully");

        }catch(Exception e){
            log.error("Internal server error",e);
            return ResponseEntity.internalServerError().body("Internal server error");
        }

    }

    @Transactional
    @DeleteMapping("/delete/{orgName}")
    public ResponseEntity<?> deleteOrg(@PathVariable String orgName, Principal principal ){
        try{
            
            Organization org=orgRepository.findByName(orgName).orElse(null);
            if(org==null){
                return ResponseEntity.badRequest().body("Organization does not exists");
            }

            String username= principal.getName();
            if(orgName.equals(username+"-userspace")){
                return ResponseEntity.badRequest().body("Can'not delete userspace");
            }
            User user=userRepository.findByUsername(username).orElse(null);
            OrgRole orgRole=orgRoleRepository.findByOrganizationAndUser(org, user).orElse(null);
            if(orgRole==null){
                return ResponseEntity.badRequest().body("User is not a part of organization");
            }
            if(orgRole.getRole()!=EOrgRole.ADMIN){
                return ResponseEntity.badRequest().body("User is not the admin of the organization");
            }
            Set<Project> projects=org.getProjects();
            for(Project project: projects){
                projectRoleRepository.deleteByProject(project);
            }
            projectRepository.deleteByOrganization(org);
            orgRoleRepository.deleteByOrganization(org);
            orgRepository.deleteById(org.getId());
            
            return ResponseEntity.ok().body("Organization deleted successfully");
            
            
        }catch(Exception e){
          log.error("Internal server error", e);
          return ResponseEntity.internalServerError().body("Internal server error");
        }
    }


    @PutMapping("/update/{orgName}")
    public ResponseEntity<?> updateOrg(@Valid @RequestBody AddOrgRequest updateOrgRequest,@PathVariable String orgName, Principal principal){
       try{
            String username = principal.getName();
            User user= userRepository.findByUsername(username).orElse(null);
            Organization org= orgRepository.findByName(orgName).orElse(null);
            if(org==null){
                return ResponseEntity.badRequest().body("Organization do not exist");
            }
            OrgRole orgRole=orgRoleRepository.findByOrganizationAndUser(org, user).orElse(null);
            if(orgRole==null){
                return ResponseEntity.badRequest().body("User do not belong to organization");
            }
            if(orgRole.getRole()!=EOrgRole.ADMIN){
                return ResponseEntity.badRequest().body("User is not the admin of organization");
            }
            org.setName(updateOrgRequest.getName());
            org.setDescription(updateOrgRequest.getDescription());
            orgRepository.save(org);
            return ResponseEntity.ok().body("Organization data updated successfully");

       }catch (Exception e){
           log.error("Internal server error", e);
           return ResponseEntity.internalServerError().body("Internal Server Error");

       }
    }

    @PostMapping("/addMembers/{orgName}")
    public ResponseEntity<?> addMembers(@Valid @RequestBody AddMembersRequest addMembersRequest, @PathVariable String orgName,Principal principal){
        try{
          
            
            Organization org= orgRepository.findByName(orgName).orElse(null);
            if(org==null){
                return ResponseEntity.badRequest().body("Organization do not exist");
            }
            String username=principal.getName();
            if(orgName.equals(username+"-userSpace")){
                return ResponseEntity.badRequest().body("Members cant be added to userSpace");
            }
            User user=userRepository.findByUsername(username).orElse(null);
            OrgRole orgRole=orgRoleRepository.findByOrganizationAndUser(org, user).orElse(null);
            if(orgRole==null){
                return ResponseEntity.badRequest().body("User is not the admin of the organization");
            }
            if(orgRole.getRole()!=EOrgRole.ADMIN){
                return ResponseEntity.badRequest().body("User is not the admin of the organization");
            }
            Set<String> newMembersAdded=new HashSet<>();
            for(String member: addMembersRequest.getMembers()){
                User new_member=userRepository.findByUsername(member).orElse(null);
                if(new_member==null){
                    continue;
                }
                OrgRole new_memberOrgRole=orgRoleRepository.findByOrganizationAndUser(org, new_member).orElse(null);
                if(new_memberOrgRole!=null){
                    continue;
                }
                OrgRole newMenberOrgRole=new OrgRole(EOrgRole.MEMBER,org,new_member);
                orgRoleRepository.save(newMenberOrgRole);
                newMembersAdded.add(member);

            }

            return ResponseEntity.ok().body(new AddMembersResponse(newMembersAdded));
            

        }catch(Exception e){
            log.error("Internal Server Error", e);
            return ResponseEntity.internalServerError().body("Internal Server Error");
        }
    }

    @DeleteMapping("/removeMembers/{orgName}")
    public ResponseEntity<?> removeMembers(@Valid @RequestBody AddMembersRequest removeMembersRequest, @PathVariable String orgName, Principal principal){
        try{
          Organization org= orgRepository.findByName(orgName).orElse(null);
            if(org==null){
                return ResponseEntity.badRequest().body("Organization do not exist");
            }
            String username=principal.getName();
            User user=userRepository.findByUsername(username).orElse(null);
            OrgRole orgRole=orgRoleRepository.findByOrganizationAndUser(org, user).orElse(null);
            if(orgRole==null){
                return ResponseEntity.badRequest().body("User is not the admin of the organization");
            }
            if(orgRole.getRole()!=EOrgRole.ADMIN){
                return ResponseEntity.badRequest().body("User is not the admin of the organization");
            }
            Set<String> membersRemoved=new HashSet<>();
            for(String member: removeMembersRequest.getMembers()){
                if(member.equals(username)){
                    continue;
                }
                User remove_member=userRepository.findByUsername(member).orElse(null);
                if(remove_member==null){
                    continue;
                }
                OrgRole remove_memberOrgRole=orgRoleRepository.findByOrganizationAndUser(org, remove_member).orElse(null);
                if(remove_memberOrgRole==null){
                    continue;
                }
                
                Set<Project> projects= org.getProjects();
                for(Project project:projects){
                    ProjectRole projectRole=projectRoleRepository.findByProjectAndUser(project, user).orElse(null);
                    if(projectRole!=null){
                        projectRoleRepository.deleteById(projectRole.getId());
                    }
                }
               
                orgRoleRepository.deleteById(remove_memberOrgRole.getId());
                membersRemoved.add(member);

            }

            return ResponseEntity.ok().body(new RemoveMembersResponse(membersRemoved));

        }catch(Exception e){
          log.error("Internal server error", e);
          return ResponseEntity.internalServerError().body("Internal server error");
        }
    }
    @PutMapping("/changeMembersStatus/{orgName}")
    public ResponseEntity<?> changeMembersStatus(@Valid @RequestBody ChangeOrgMembersStatusRequest changeOrgMembersStatusRequest,@PathVariable String orgName, Principal principal){
     try{
        Organization org= orgRepository.findByName(orgName).orElse(null);
        if(org==null){
            return ResponseEntity.badRequest().body("Organization do not exist");
        }
        String username=principal.getName();
        User user=userRepository.findByUsername(username).orElse(null);
        OrgRole userOrgRole=orgRoleRepository.findByOrganizationAndUser(org, user).orElse(null);
        if(userOrgRole==null){
            return ResponseEntity.badRequest().body("User does not belong to organization");
        }
        if(userOrgRole.getRole()!=EOrgRole.ADMIN){
            return ResponseEntity.badRequest().body("User is not the admin of the organization");
        }
        Map<String,String> newStatus=changeOrgMembersStatusRequest.getOrgMembersStatus();
        for(Map.Entry<String, String> e: newStatus.entrySet()){
           String memberUsername=e.getKey();
          
           String newRole=e.getValue().toLowerCase();
           
           if(memberUsername.equals(username)){
            continue;
           }

           User member=userRepository.findByUsername(memberUsername).orElse(user);
           if(member==null){
            continue;
           }

           OrgRole memberOrgRole=orgRoleRepository.findByOrganizationAndUser(org, member).orElse(null);
           

           if(memberOrgRole==null){
            continue;
           }
          
           if(newRole.equals("admin")){
            memberOrgRole.setRole(EOrgRole.ADMIN);
           }
           else if(newRole.equals("manager")){
            memberOrgRole.setRole(EOrgRole.MANAGER);
           }
           else if(newRole.equals("member")){

            memberOrgRole.setRole(EOrgRole.MEMBER);
           }
           orgRoleRepository.save(memberOrgRole);
           
        }

        return ResponseEntity.ok().body("Roles changed successfully");



     }catch(Exception e){
        log.error("Internal server error", e);
        return ResponseEntity.internalServerError().body("Internal server error");
     }
    }

   @PutMapping("/setArcheiveStatus/{orgName}")
   public ResponseEntity<?> setArcheiveStatus(@Valid @RequestBody SetArcheiveStatusRequest setArcheiveStatusRequest,@PathVariable String orgName, Principal principal){
     try{

        Organization org= orgRepository.findByName(orgName).orElse(null);

        if(org==null){
            return ResponseEntity.badRequest().body("Organization does not exist");
        }

        String username=principal.getName();
        User user=userRepository.findByUsername(username).orElse(null);
        OrgRole orgRole=orgRoleRepository.findByOrganizationAndUser(org, user).orElse(null);
        if(orgRole==null){
            return ResponseEntity.badRequest().body("User does not belong to the organization");
        }
        if(orgRole.getRole()!=EOrgRole.ADMIN && orgRole.getRole()!=EOrgRole.MANAGER){
            return ResponseEntity.badRequest().body("User is not the admin or manager of the organization");
        }
        Map<String, Boolean> newStatus= setArcheiveStatusRequest.getArcheiveStatus();

        for(Map.Entry<String, Boolean> e: newStatus.entrySet()){
            String projectName= e.getKey();
            Boolean status= e.getValue();
            
            Project project= projectRepository.findByNameAndOrganization(projectName, org).orElse(null);
            if(project==null){
                continue;
            }
            project.setArcheive(status);
            projectRepository.save(project);

        }

        return ResponseEntity.ok().body("Archeive Status of projects updated successfully");

     }catch(Exception e){
         log.error("Internal Server Error", e);
         return ResponseEntity.internalServerError().body("Internal Server Error");
     }
   }

     @PutMapping("/setBookmarkStatus/{orgName}")
   public ResponseEntity<?> setBookmarkStatus(@Valid @RequestBody SetBookmarkStatusRequest setBookmarkStatusRequest,@PathVariable String orgName, Principal principal){
     try{

        Organization org= orgRepository.findByName(orgName).orElse(null);
        if(org==null){
            return ResponseEntity.badRequest().body("Organization does not exist");
        }
        String username=principal.getName();
        User user=userRepository.findByUsername(username).orElse(null);
        OrgRole orgRole=orgRoleRepository.findByOrganizationAndUser(org, user).orElse(null);
        if(orgRole==null){
            return ResponseEntity.badRequest().body("User does not belong to the organization");
        }
        if(orgRole.getRole()!=EOrgRole.ADMIN && orgRole.getRole()!=EOrgRole.MANAGER){
            return ResponseEntity.badRequest().body("User is not the admin or manager of the organization");
        }
        Map<String, Boolean> newStatus= setBookmarkStatusRequest.getBookmarkStatus();

        for(Map.Entry<String, Boolean> e: newStatus.entrySet()){
            String projectName= e.getKey();
            Boolean status= e.getValue();
            
            Project project= projectRepository.findByNameAndOrganization(projectName, org).orElse(null);
            if(project==null){
                continue;
            }
            project.setBookmarked(status);
            projectRepository.save(project);

        }

        return ResponseEntity.ok().body("Bookmark Status of projects updated successfully");

     }catch(Exception e){
         log.error("Internal Server Error", e);
         return ResponseEntity.internalServerError().body("Internal Server Error");
     }
   }

   @GetMapping("/getMembers/{orgName}")
   public ResponseEntity<?> getMembers(@PathVariable String orgName){
    try{
      Organization org = orgRepository.findByName(orgName).orElse(null);
      if(org==null){
        return ResponseEntity.badRequest().body("Organization not found");
      }
      Set<OrgRole> orgRoles=org.getOrgRoles();
      Map<String, String> response= new HashMap<>();

      for(OrgRole role:orgRoles){
        User user= role.getUser();
        EOrgRole eOrgRole=role.getRole();
        if(eOrgRole==EOrgRole.ADMIN){
            response.put(user.getUsername(), "admin");
        }else if(eOrgRole==EOrgRole.MANAGER){
            response.put(user.getUsername(), "manager");
        }else{
            response.put(user.getUsername(),"member");
        }
      }
      return ResponseEntity.ok().body(new GetMembersResponse(response));

    }catch(Exception e){
          log.error("Internal server error", e);
          return ResponseEntity.internalServerError().body("Internal Server error");
    }
   }

   @GetMapping("/getProjects/{orgName}")
   public ResponseEntity<?> getProjects(@PathVariable String orgName){
    try{

      Organization org = orgRepository.findByName(orgName).orElse(null);
      if(org==null){
        return ResponseEntity.badRequest().body("Organization not found");
      }
      Set<Project> projects= org.getProjects();
     Map<String,Map<String,Boolean>> res_project=new HashMap<>();
      for(Project project: projects){
        
        Map<String,Boolean> projectStatus= new HashMap<>();
       
        if(project.getBookmarked()){
          projectStatus.put("bookmark", true);
        }else{
            projectStatus.put("bookmark", false);
        }

        if(project.getArcheive()){
            projectStatus.put("archeive", true);
        }else{
            projectStatus.put("archeive", false);
        }
        res_project.put(project.getName(),projectStatus);


      }
      return ResponseEntity.ok().body(new GetProjectsResponse(res_project));

    }catch (Exception e){
       log.error("Internal Server Error",e);
       return ResponseEntity.internalServerError().body("Internal Server Error");
    }
   }

   @GetMapping("/getOrg/{orgName}")
   public ResponseEntity<?> getOrg(@PathVariable String orgName){
    try{

        Organization org= orgRepository.findByName(orgName).orElse(null);
        if(org==null){
            return ResponseEntity.badRequest().body("Organization not found");
        }
        return ResponseEntity.ok().body(org);

    }catch(Exception e){
       log.error("Internal server error", e);
       return ResponseEntity.internalServerError().body("Internal Server Error");
    }
   }

   @GetMapping("/getAllOrg")
   public ResponseEntity<?> getAllOrgs(){
    try{
      List<Organization> orgs=orgRepository.findAll();
      return ResponseEntity.ok().body(new GetAllOrgsResponse(orgs));
    }catch(Exception e){
      log.error("Internal Server Error", e);
      return ResponseEntity.internalServerError().body("Internal Server Error");
    }
   }

   public static boolean isValidName(String str) {
    // Check if the string does not end with "-userspace", does not have special characters,
    // does not have spaces, and allows "-" and "_"
    return !str.endsWith("-userspace") && str.matches("^[a-zA-Z0-9_-]+$");
}


 }





















