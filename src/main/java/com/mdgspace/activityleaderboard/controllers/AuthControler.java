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

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthControler {

    // code=290da3ef69167465a0ec
    private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtUtils jwtUtils;

    @Value("${GITHUB_AUTH_URL}")
    private String githubAuthUrl;

    @Value("${GITHUB_CLIENT_ID}")
    private String client_id;

    @Value("${GITHUB_CLIENT_SECRET}")
    private String client_secret;

    @Value("${GITHUB_API_URL}")
    private String githubApiUrl;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            String code = loginRequest.getCode();
            String params = "?client_id=" + client_id + "&client_secret=" + client_secret + "&code=" + code;

            String auth_url = githubAuthUrl + params;

            WebClient.Builder builder = WebClient.builder();

            Accesstoken accesstokenResponse = builder.build().get().uri(auth_url).header("Accept", "application/json")
                    .retrieve().bodyToMono(Accesstoken.class).block();


            String user_url = githubApiUrl + "/user";
            String access_token = accesstokenResponse.getAccesstoken();

            if(access_token==null){
                return  new ResponseEntity<>("Invalid code", HttpStatus.BAD_REQUEST);
            }
            GithubUser userResponse = builder.build().get().uri(user_url).header("Accept", "application/json")
                    .header("Authorization", "Bearer " + access_token).header("X-GitHub-Api-Version", "2022-11-28")
                    .retrieve().bodyToMono(GithubUser.class).block();
            
            String username=userResponse.getUsername();
            if (username== null) {
                return new ResponseEntity<>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
            }
       
            boolean isUser= userRepository.existsByUsername(username);

            Optional<User> user=userRepository.findByUsername(username);

            if(!isUser){
                User new_user= new User(username, access_token,encoder.encode(username));
                userRepository.save(new_user);
            }
            if(isUser){                
                user.orElseThrow(null).setAccesstoken(access_token);
            }
            
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,username ,null));
            
            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetailsImpl userDetails= (UserDetailsImpl) authentication.getPrincipal();

            String jwt = jwtUtils.generateJwtToken(authentication);

            return ResponseEntity.ok(new JwtResponse(userDetails.getAccesstoken(), userDetails.getId(), userDetails.getUsername(), jwt));

        } catch (Exception e) {
            logger.error("Internal server error", e);
            return new ResponseEntity<>("Internal server error",HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

}
