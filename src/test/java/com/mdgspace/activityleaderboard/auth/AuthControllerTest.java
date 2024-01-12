package com.mdgspace.activityleaderboard.auth;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mdgspace.activityleaderboard.controllers.AuthControler;
import com.mdgspace.activityleaderboard.payload.request.LoginRequest;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:application.properties")
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;



    @Autowired
    private ObjectMapper objectMapper; 

    @Test
    public void whenInvalidINput_thenReturns400() throws Exception {

        LoginRequest loginRequest= new LoginRequest("12opjwyui56");
        mockMvc.perform(post("api/auth/login").contentType("application/json").content(objectMapper.writeValueAsString(loginRequest))).andExpect(status().isBadRequest());
    }

    
}
