package com.mdgspace.activityleaderboard.controllers;


import jakarta.validation.Valid;

import java.util.Optional;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mdgspace.activityleaderboard.models.Organization;
import com.mdgspace.activityleaderboard.models.User;
import com.mdgspace.activityleaderboard.models.enums.EOrgRole;
import com.mdgspace.activityleaderboard.models.roles.OrgRole;
import com.mdgspace.activityleaderboard.payload.request.LoginRequest;
import com.mdgspace.activityleaderboard.payload.response.JwtResponse;
import com.mdgspace.activityleaderboard.repository.OrgRepository;
import com.mdgspace.activityleaderboard.repository.OrgRoleRepository;
import com.mdgspace.activityleaderboard.repository.UserRepository;
import com.mdgspace.activityleaderboard.security.jwt.AuthEntryPointJwt;
import com.mdgspace.activityleaderboard.security.jwt.JwtUtils;
import com.mdgspace.activityleaderboard.security.services.UserDetailsImpl;
import com.mdgspace.activityleaderboard.services.github.service.GithubService;


// For handling cors errors
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthControler {

    // For debug porpose
    private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);

    // Lib by spring boot to handle the authentication
    @Autowired
    AuthenticationManager authenticationManager;

    // For hashing the password
    @Autowired
    PasswordEncoder encoder;


    // Qeuries to get userdetails using username
    @Autowired
    UserRepository userRepository;

    // for making the JWT token
    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    OrgRepository orgRepository;

    @Autowired
    OrgRoleRepository orgRoleRepository;


    @Autowired
    GithubService githubService;

    // POST request is defined for login
    @Transactional
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {

            // Code sent to user by github
            String code = loginRequest.getCode();

            String access_token = githubService.getAccesstoken(code).orElse(null) ;

            // Return badrequest if code sent is wrong
            if(access_token==null){
                return  new ResponseEntity<>("Invalid code", HttpStatus.BAD_REQUEST);
            }

            String username= githubService.getGithubUserName(access_token).orElse(null);
            if (username== null) {
                return new ResponseEntity<>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
            }
       
            // Checking if the user exists
            boolean isUser= userRepository.existsByUsername(username);

            //  getting user from the database
            Optional<User> user=userRepository.findByUsername(username);

            // if no user exists with similar name 
            if(!isUser){
                // Crete new user object
                User new_user= new User(username, access_token,encoder.encode(username));

                userRepository.save(new_user);
                // Saving in database
           
                String orgName=username+"-userspace";
                Boolean isOrg= orgRepository.existsByName(orgName);
                if(isOrg){
                   return  ResponseEntity.internalServerError().body("Internal server error");
                }
                Organization new_org= new Organization(orgName, null, null);
                orgRepository.save(new_org);
                EOrgRole eOrgRole= EOrgRole.ADMIN;
                OrgRole new_org_role= new OrgRole(eOrgRole,new_org,new_user);
                orgRoleRepository.save(new_org_role);


            }
            if(isUser){           
                // Setting access token in database
                user.orElseThrow(null).setAccesstoken(access_token);
            }
            
            //  Steps to create the token
            
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,username ,null));
            
            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetailsImpl userDetails= (UserDetailsImpl) authentication.getPrincipal();

            String jwt = jwtUtils.generateJwtToken(authentication);

            // Response sent to frontend
            return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername()));

        } catch (Exception e) {

            // Response if some error occurs 
            logger.error("Internal server error", e);
            return new ResponseEntity<>("Internal server error",HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

}
