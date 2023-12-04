package com.mdgspace.activityleaderboard.controllers;

import java.security.Principal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import com.mdgspace.activityleaderboard.models.Organization;
import com.mdgspace.activityleaderboard.models.Project;
import com.mdgspace.activityleaderboard.models.User;
import com.mdgspace.activityleaderboard.models.roles.ProjectRole;
import com.mdgspace.activityleaderboard.payload.github.Commit;
import com.mdgspace.activityleaderboard.payload.github.Committer;
import com.mdgspace.activityleaderboard.payload.github.Issue;
import com.mdgspace.activityleaderboard.payload.github.PullRequest;
import com.mdgspace.activityleaderboard.payload.github.UserObject;
import com.mdgspace.activityleaderboard.payload.response.ProjectStatsResponse;
import com.mdgspace.activityleaderboard.repository.OrgRepository;
import com.mdgspace.activityleaderboard.repository.OrgRoleRepository;
import com.mdgspace.activityleaderboard.repository.ProjectRepository;
import com.mdgspace.activityleaderboard.repository.ProjectRoleRepository;
import com.mdgspace.activityleaderboard.repository.UserRepository;
import com.mdgspace.activityleaderboard.services.github.service.GithubService;

import lombok.extern.slf4j.Slf4j;

@CrossOrigin(origins = "*",maxAge = 3600)
@RestController
@Slf4j
@RequestMapping("/api/protected/github")
@Validated
public class GithubController {
    
    @Autowired
    UserRepository userRepository;

    @Autowired
    OrgRepository orgRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    OrgRoleRepository orgRoleRepository;


    @Autowired
    ProjectRoleRepository projectRoleRepository;


    @Autowired
    GithubService githubService;


    @GetMapping("/{orgName}")
    public ResponseEntity<?> getOrgStatus(@PathVariable String orgName,@RequestParam(required = true) Boolean monthly, Principal principal){
     try{
          Organization org=orgRepository.findByName(orgName).orElse(null);
       if(org==null){
        return ResponseEntity.badRequest().body("Organization does not exists");
       }
       User user= userRepository.findByUsername(principal.getName()).orElse(null);
       Set<Project> projects=org.getProjects();
       Map<String,Map<String,Integer>> res = new HashMap<>();
       for(Project project: projects){
       try{
        String link= project.getLink();
        Set<ProjectRole> projectRoles=project.getProjectRoles();
        Set<String> members=new HashSet<>();
        for(ProjectRole role: projectRoles){
            User use_r= role.getUser();
            members.add(use_r.getUsername());
        }
        PullRequest[] pullRequests=githubService.totalPullRequests(link, user.getAccesstoken(), monthly);
        Issue[] totalIssues=githubService.totalIssues(link, user.getAccesstoken(), monthly);
        Commit[] totalCommits= githubService.totalCommits(link, user.getAccesstoken(), monthly);
        Integer pullRequestsCount=0;
        Integer issuesCount=0;
        Integer commitsCounts=0;
        for(PullRequest pullRequest:pullRequests){
            UserObject u_ser=pullRequest.getUser();
            String username=u_ser.getUsername();
            if(members.contains(username)){
                pullRequestsCount++;
            }
        }
        for(Issue issue: totalIssues){
            UserObject u_ser=issue.getUser();
            String username=u_ser.getUsername();
            if(members.contains(username)){
                issuesCount++;
            }
        }
        for(Commit commit:totalCommits){
            Committer committer=commit.getCommitter();
            String username=committer.getUsername();
            if(members.contains(username)){
                commitsCounts++;
            }
        }
        Map<String, Integer> stats= new HashMap<>();
        stats.put("pulls", pullRequestsCount);
        stats.put("commits",commitsCounts);
        stats.put("issues", issuesCount);
        res.put(project.getName(), stats);


       }catch(Exception e){
        log.error("Github fetch error", e);
        Map<String, Integer> stats= new HashMap<>();
        stats.put("pulls", 0);
        stats.put("commits",0);
        stats.put("issues", 0);
        res.put(project.getName(), stats);
       }

       }

       return ResponseEntity.ok().body(new ProjectStatsResponse(res));
     }catch(Exception e){
        log.error("Internal Server Error", e);
        return ResponseEntity.internalServerError().body("Internal Server Error");
     }
    }


}
