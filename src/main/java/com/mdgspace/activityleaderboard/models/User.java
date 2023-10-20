package com.mdgspace.activityleaderboard.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.HashSet;
import java.util.Set;


// User Model for user with non-admin access.

@Entity
@Table(name="users",
       uniqueConstraints = {
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
    @Size(max=20)
    private String username;

    // TODO: Reimplement using proper method
    @NotBlank 
    @Size(max=20)
    private String password;


    // github_oauth access token
    @NotBlank
    @Size(max=20)
    private String accesstoken;
 

   // Constructor for dependency injection 
   public User(){

   }


   public User(String username, String accesstoken, String password){
    this.username=username;
    this.accesstoken=accesstoken;
    this.password=password;
   }

   // Defining getters and setters

   public Long getId(){
    return id;
   }

   public String getUsername(){
    return username;
   }

 public String getPassword(){
    return password;
   }

   public String getAccesstoken(){
    return accesstoken;
   }

   public void setUsername(String username){
    this.username=username;
   }

   public void setPassword(String password){
    this.password=password;
   }
   
   
   public void setAccesstoken(String accesstoken){
    this.accesstoken=accesstoken;
   }
   

}
