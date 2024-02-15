package com.mdgspace.activityleaderboard.user;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mdgspace.activityleaderboard.ActivityleaderboardApplication;
import com.mdgspace.activityleaderboard.models.Organization;
import com.mdgspace.activityleaderboard.models.Project;
import com.mdgspace.activityleaderboard.models.User;
import com.mdgspace.activityleaderboard.models.enums.EProjectRole;
import com.mdgspace.activityleaderboard.models.roles.ProjectRole;
import com.mdgspace.activityleaderboard.payload.request.SetArcheiveStatusRequest;
import com.mdgspace.activityleaderboard.payload.request.SetBookmarkStatusRequest;
import com.mdgspace.activityleaderboard.repository.OrgRepository;
import com.mdgspace.activityleaderboard.repository.OrgRoleRepository;
import com.mdgspace.activityleaderboard.repository.ProjectRoleRepository;
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

    @MockBean
    private OrgRepository orgRepository;

    @MockBean
    private OrgRoleRepository orgRoleRepository;

    @MockBean
    private ProjectRoleRepository projectRoleRepository;


    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mvc;

    @Before
    public void  setup() throws Exception {
       mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    @WithMockUser("spring")
    @Test
    public void Test_Get_All_User() throws Exception{
         mvc.perform(get("/api/protected/user/all").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @WithMockUser("spring")
    @Test 
    public void Test_Get_User_Orgs() throws Exception{
      
        User user= new User("spring", "spring", "spring"); 
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        mvc.perform(get("/api/protected/user/getUserOrgs/spring").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

    }

    @WithMockUser("spring")
    @Test
    public void Test_Get_Users_Projects() throws Exception{
        User user= new User("spring", "spring", "spring"); 
        ProjectRole projectRole= new ProjectRole(EProjectRole.ADMIN,new Project("test", "test","test", new Organization("test", "test", null)), user);
        List<ProjectRole> projectRoles= List.of(projectRole);
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(projectRoleRepository.findByUser(any(User.class))).thenReturn(projectRoles);
        
        mvc.perform(get("/api/protected/user/getUsersProjects/spring").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }


    @WithMockUser("spring")
    @Test
    public void Test_SetBookmark_Stattus() throws Exception{

        User user= new User("spring", "spring", "spring"); 
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        
        Map<String, Boolean> map = Map.of("test",true);

        SetBookmarkStatusRequest bookStatus= new SetBookmarkStatusRequest(map);
        mvc.perform(put("/api/protected/user/setBookmarkStatus").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(bookStatus))).andExpect(status().isOk());

    }

    @WithMockUser("spring")
    @Test
    public void Test_SetArchive_Stattus() throws Exception{

        User user= new User("spring", "spring", "spring"); 
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        
        Map<String, Boolean> map = Map.of("test",true);

        SetArcheiveStatusRequest acrReq= new SetArcheiveStatusRequest(map);
        mvc.perform(put("/api/protected/user/setArcheiveStatus").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(acrReq))).andExpect(status().isOk());

    }


}
