package com.mdgspace.activityleaderboard.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.HashSet;
import java.util.Set;


// User Model
@Entity
@Table(name="users",
       uniqueConstraints = {
        @UniqueConstraint(columnNames = "username"),
        @UniqueConstraint(columnNames = "accesstoken")
       })
public class User {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank 
    @Size(max=20)
    private String username;

    @NotBlank 
    @Size(max=20)
    private String password;

    @NotBlank
    @Size(max=20)
    private String accesstoken;
 
   public User(){

   }

   public User(String username, String accesstoken, String password){
    this.username=username;
    this.accesstoken=accesstoken;
    this.password=password;
   }

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
