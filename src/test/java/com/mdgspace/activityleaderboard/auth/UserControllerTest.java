package com.mdgspace.activityleaderboard.auth;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.security.test.context.support.WithMockUser;

import com.mdgspace.activityleaderboard.ActivityleaderboardApplication;
import com.mdgspace.activityleaderboard.repository.UserRepository;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = ActivityleaderboardApplication.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
public class UserControllerTest {
    
   
    @Autowired
    private WebApplicationContext context;


    @MockBean
    private UserRepository userRepository;

    private MockMvc mvc;

    @Before
    public  void setup() {
         mvc= MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }



    
    @WithMockUser(value="spring")
    @Test
    public void givenAuthRequestOnPrivateService_shouldSucceedWith200() throws Exception{
        when(userRepository.existsByUsername(anyString())).thenReturn(true);
         mvc.perform(get("/api/protected/user/getUser").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }


}
