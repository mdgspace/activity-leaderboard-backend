package com.mdgspace.activityleaderboard.controllers;

import jakarta.validation.Valid;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import com.mdgspace.activityleaderboard.models.User;
import com.mdgspace.activityleaderboard.payload.github.Accesstoken;
import com.mdgspace.activityleaderboard.payload.github.GithubUser;
import com.mdgspace.activityleaderboard.payload.request.LoginRequest;
import com.mdgspace.activityleaderboard.payload.response.JwtResponse;
import com.mdgspace.activityleaderboard.payload.response.MessageResponse;
import com.mdgspace.activityleaderboard.repository.UserRepository;
import com.mdgspace.activityleaderboard.security.jwt.AuthEntryPointJwt;
import com.mdgspace.activityleaderboard.security.jwt.JwtUtils;
import com.mdgspace.activityleaderboard.security.services.UserDetailsImpl;


// For handling cors errors
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController

//  Append  /api/auth as prefix before using endpoint defined on Authcontroller
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

    // Below env variable are defined, checkout setup

    @Value("${GITHUB_AUTH_URL}")
    private String githubAuthUrl;

    @Value("${GITHUB_CLIENT_ID}")
    private String client_id;

    @Value("${GITHUB_CLIENT_SECRET}")
    private String client_secret;

    @Value("${GITHUB_API_URL}")
    private String githubApiUrl;


    // POST request is defined for login
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {

            // Code sent to user by github
            String code = loginRequest.getCode();

            // param defined by github, these are required form making requestes to github
            String params = "?client_id=" + client_id + "&client_secret=" + client_secret + "&code=" + code;

            // url to fetch access token
            String auth_url = githubAuthUrl + params;

            // fior fetching 
            WebClient.Builder builder = WebClient.builder();

            // github returned this token, this token can be used to get other user details
            Accesstoken accesstokenResponse = builder.build().get().uri(auth_url).header("Accept", "application/json")
                    .retrieve().bodyToMono(Accesstoken.class).block();

            // Github_api endpoint to get userdetails
            String user_url = githubApiUrl + "/user";
            String access_token = accesstokenResponse.getAccesstoken();

            // Return badrequest if code sent is wrong
            if(access_token==null){
                return  new ResponseEntity<>("Invalid code", HttpStatus.BAD_REQUEST);
            }

            // User response sent by guthub
            GithubUser userResponse = builder.build().get().uri(user_url).header("Accept", "application/json")
                    .header("Authorization", "Bearer " + access_token).header("X-GitHub-Api-Version", "2022-11-28")
                    .retrieve().bodyToMono(GithubUser.class).block();
            
            String username=userResponse.getUsername();
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
                
                // Saving in database
                userRepository.save(new_user);
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
