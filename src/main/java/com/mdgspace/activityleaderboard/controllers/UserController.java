package com.mdgspace.activityleaderboard.controllers;

import java.security.Principal;
import java.util.Optional;
import java.util.List;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mdgspace.activityleaderboard.models.User;
import com.mdgspace.activityleaderboard.payload.response.MessageResponse;
import com.mdgspace.activityleaderboard.payload.response.UsersResponse;
import com.mdgspace.activityleaderboard.repository.UserRepository;
import com.mdgspace.activityleaderboard.security.jwt.AuthEntryPointJwt;
import org.springframework.web.bind.annotation.RequestParam;


@CrossOrigin(origins = "*",maxAge = 3600)
@RestController


@RequestMapping("/api/protected/user")
public class UserController {
     private static final Logger logger = LoggerFactory.getLogger(UserController.class);

     @Autowired
     UserRepository userRepository;

     @GetMapping("/getUser")
     public ResponseEntity<?> getLogginedUser(Principal principal){
        try{

          String username=principal.getName();
          Boolean isUser=userRepository.existsByUsername(username);
          if(!isUser){
               return ResponseEntity.badRequest().body(new MessageResponse("Invalid token"));
          }

          return ResponseEntity.ok().body(new MessageResponse(username));
        
        
        }catch (Exception e){
          logger.error("Internal Server Error;", e);
         return ResponseEntity.internalServerError().body(new MessageResponse("Internal server error"));
        }
     }

     @GetMapping("/all")
     public ResponseEntity<?> getAllUsers(){
          try{
               List<User> users=userRepository.findAll();
               return ResponseEntity.ok().body(new UsersResponse(users));

          }catch (Exception e){
               return ResponseEntity.internalServerError().body(new MessageResponse("Internal Server Error"));
          }
     }

}
