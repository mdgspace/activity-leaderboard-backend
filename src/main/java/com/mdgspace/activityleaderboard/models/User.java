package com.mdgspace.activityleaderboard.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mdgspace.activityleaderboard.models.roles.OrgRole;
import com.mdgspace.activityleaderboard.models.roles.ProjectRole;


// User Model for user with non-admin access.

@Entity
@Table(name = "users", uniqueConstraints = {
      @UniqueConstraint(columnNames = "username"),
      @UniqueConstraint(columnNames = "accesstoken")
})
public class User {

   // Primary key to identify the user.
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   // Username, will be same to the github handle
   @NotBlank
   @Size(max = 20)
   private String username;

  
    //todo => Reimplement using proper method
   @JsonIgnore
   @NotBlank
   @Size(max = 20)
   private String password;

   // github_oauth access token
   @JsonIgnore
   @NotBlank
   @Size(max = 20)
   private String accesstoken;

   @JsonIgnore   
   @OneToMany(mappedBy = "user")
   private Set<ProjectRole> projectRoles=new HashSet<>();
  

   @JsonIgnore
   @OneToMany(mappedBy = "user")
   private Set<OrgRole> orgRoles=new HashSet<>();

   // Constructor for dependency injection
   public User() {

   }

   public User(String username, String accesstoken, String password) {
      this.username = username;
      this.accesstoken = accesstoken;
      this.password = password;
   }

   // Defining getters and setters

   public Long getId() {
      return id;
   }

   public String getUsername() {
      return username;
   }

   public String getPassword() {
      return password;
   }

   public String getAccesstoken() {
      return accesstoken;
   }

   public Set<ProjectRole> getProjectRoles(){
      return projectRoles;
   }

   public Set<OrgRole> getOrgRoles(){
      return orgRoles;
   }

   public void setOrgRoles(Set<OrgRole>orgRoles){
      this.orgRoles=orgRoles;
   }

   public void setUsername(String username) {
      this.username = username;
   }

   public void setPassword(String password) {
      this.password = password;
   }

   public void setAccesstoken(String accesstoken) {
      this.accesstoken = accesstoken;
   }

   public void setProjectRoles(Set<ProjectRole> projectRoles){
      this.projectRoles=projectRoles;
   }
}

