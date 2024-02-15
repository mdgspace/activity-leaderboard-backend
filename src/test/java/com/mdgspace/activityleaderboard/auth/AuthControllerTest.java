package com.mdgspace.activityleaderboard.auth;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mdgspace.activityleaderboard.ActivityleaderboardApplication;
import com.mdgspace.activityleaderboard.models.Organization;
import com.mdgspace.activityleaderboard.models.User;
import com.mdgspace.activityleaderboard.models.enums.EOrgRole;
import com.mdgspace.activityleaderboard.models.roles.OrgRole;
import com.mdgspace.activityleaderboard.payload.request.LoginRequest;
import com.mdgspace.activityleaderboard.repository.OrgRepository;
import com.mdgspace.activityleaderboard.repository.OrgRoleRepository;
import com.mdgspace.activityleaderboard.repository.UserRepository;
import com.mdgspace.activityleaderboard.services.github.service.GithubService;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = ActivityleaderboardApplication.class)
@SpringBootTest 
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GithubService githubService;

    @MockBean
    UserRepository userRepository;

    @MockBean
    OrgRepository orgRepository;

    @MockBean
    OrgRoleRepository orgRoleRepository;

    @Autowired
    private ObjectMapper objectMapper; 

    @Test
    public void whenInvalidInput_thenReturns400() throws Exception {
        
        when(githubService.getAccesstoken(anyString())).thenReturn(Optional.empty());
        LoginRequest loginRequest= new LoginRequest("123456789");
        mockMvc.perform(post("/api/auth/login").contentType("application/json").content(objectMapper.writeValueAsString(loginRequest))).andExpect(status().isBadRequest());
    }

    @Test 
    public void whenValidInput_thenReturns200() throws Exception {
        
        LoginRequest loginRequest = new LoginRequest("123456789"); // let it be correct code
        
        when(githubService.getAccesstoken(anyString())).thenReturn(Optional.of("123456789"));
        when(githubService.getGithubUserName(anyString())).thenReturn(Optional.of("yp969803"));
        mockMvc.perform(post("/api/auth/login").contentType("application/json").content(objectMapper.writeValueAsString(loginRequest))).andExpect(status().isInternalServerError());
    
    }

    @Test
    public void createUserAnd_userSpace_ForFirstTime() throws Exception {

        LoginRequest loginRequest = new LoginRequest("123456789");
         
        when(githubService.getAccesstoken("123456789")).thenReturn(Optional.of("123456789"));
        when(githubService.getGithubUserName(anyString())).thenReturn(Optional.of("yp969803"));
        mockMvc.perform(post("/api/auth/login").contentType("application/json").content(objectMapper.writeValueAsString(loginRequest))).andExpect(status().isInternalServerError());
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(1)).save(userCaptor.capture());
        
        ArgumentCaptor<Organization> orgCaptor= ArgumentCaptor.forClass(Organization.class);
        verify(orgRepository,times(1)).save(orgCaptor.capture());
        assertThat(orgCaptor.getValue().getName()).isEqualTo("yp969803-userspace");
        ArgumentCaptor<OrgRole> orgRoleCaptor =  ArgumentCaptor.forClass(OrgRole.class);
        verify(orgRoleRepository,times(1)).save(orgRoleCaptor.capture());
        assertThat(orgRoleCaptor.getValue().getRole()).isEqualTo(EOrgRole.ADMIN);
        assertThat(userCaptor.getValue().getUsername()).isEqualTo("yp969803");
        
    }



    @Test
    public void doNotCreateUserAndSpaceIfAlreadyExist() throws Exception{
        LoginRequest loginRequest = new LoginRequest("123456789");
         
        User user= new User("yp969803","123456789","123456789");

        when(userRepository.findByUsername("yp969803")).thenReturn(Optional.of(user));
        when(githubService.getAccesstoken("123456789")).thenReturn(Optional.of("123456789"));
        when(githubService.getGithubUserName(anyString())).thenReturn(Optional.of("yp969803"));
        mockMvc.perform(post("/api/auth/login").contentType("application/json").content(objectMapper.writeValueAsString(loginRequest))).andExpect(status().isInternalServerError());
        
    
    }
    
}
