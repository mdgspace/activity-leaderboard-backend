package com.mdgspace.activityleaderboard.controllers;


import java.security.Principal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
import com.mdgspace.activityleaderboard.models.redis.OrgStats;
import com.mdgspace.activityleaderboard.models.redis.OrganizationRank;
import com.mdgspace.activityleaderboard.models.redis.ProjectStats;
import com.mdgspace.activityleaderboard.models.roles.OrgRole;
import com.mdgspace.activityleaderboard.models.roles.ProjectRole;
import com.mdgspace.activityleaderboard.payload.github.Commit;
import com.mdgspace.activityleaderboard.payload.github.Committer;
import com.mdgspace.activityleaderboard.payload.github.Issue;
import com.mdgspace.activityleaderboard.payload.github.PullRequest;
import com.mdgspace.activityleaderboard.payload.github.UserObject;
import com.mdgspace.activityleaderboard.payload.response.GetOrgStatsResponse;
import com.mdgspace.activityleaderboard.payload.response.GetProjectStatsResponse;
import com.mdgspace.activityleaderboard.repository.OrgRepository;
import com.mdgspace.activityleaderboard.repository.OrgRoleRepository;
import com.mdgspace.activityleaderboard.repository.ProjectRepository;
import com.mdgspace.activityleaderboard.repository.ProjectRoleRepository;
import com.mdgspace.activityleaderboard.repository.UserRepository;
import com.mdgspace.activityleaderboard.repository.redis.OrgStatsRepository;
import com.mdgspace.activityleaderboard.repository.redis.OrganizationRankRepository;
import com.mdgspace.activityleaderboard.repository.redis.ProjectStatsRepository;
import com.mdgspace.activityleaderboard.services.github.service.GithubService;

import lombok.extern.slf4j.Slf4j;

@CrossOrigin(origins = "*", maxAge = 3600)
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


    @Autowired
    ProjectStatsRepository projectStatsRepository;

    @Autowired
    OrgStatsRepository orgStatsRepository;

    @Autowired
    OrganizationRankRepository organizationRankRepository;

    @GetMapping("/{orgName}")
    public ResponseEntity<?> getOrgStatus(@PathVariable String orgName, @RequestParam(required = true) Boolean monthly,
            Principal principal) {
        try {
            Organization org = orgRepository.findByName(orgName).orElse(null);
            if (org == null) {
                return ResponseEntity.badRequest().body("Organization does not exists");
            }
            User user = userRepository.findByUsername(principal.getName()).orElse(null);
          
          
            OrgStats orgStats=orgStatsRepository.findByOrganizationAndMonthly(org, monthly).orElse(null);
       
            if(orgStats != null && Instant.now().toEpochMilli() - orgStats.getTime() <= 60000){
               return ResponseEntity.ok().body(orgStats.getResponse());
            }
            else if (orgStats != null ) {
              ExecutorService executorService= Executors.newFixedThreadPool(1);
              executorService.submit(()->{
                 try {

                    Set<Project> projects = org.getProjects();

                    Map<String, Map<String, Integer>> res = new HashMap<>();
                    for (Project project : projects) {
                        try {
                            String link = project.getLink();
                            Set<ProjectRole> projectRoles = project.getProjectRoles();
                            Set<String> members = new HashSet<>();
                            for (ProjectRole role : projectRoles) {
                                User use_r = role.getUser();
                                members.add(use_r.getUsername());
                            }
                            PullRequest[] pullRequests = githubService.totalPullRequests(link, user.getAccesstoken(),
                                    monthly);
                            Issue[] totalIssues = githubService.totalIssues(link, user.getAccesstoken(), monthly);
                            Commit[] totalCommits = githubService.totalCommits(link, user.getAccesstoken(), monthly);
                            Integer pullRequestsCount = 0;
                            Integer issuesCount = 0;
                            Integer commitsCounts = 0;
                            for (PullRequest pullRequest : pullRequests) {
                                UserObject u_ser = pullRequest.getUser();
                                String username = u_ser.getUsername();
                                if (members.contains(username)) {
                                    pullRequestsCount++;
                                }
                            }
                            for (Issue issue : totalIssues) {
                                UserObject u_ser = issue.getUser();
                                String username = u_ser.getUsername();
                                if (members.contains(username)) {
                                    issuesCount++;
                                }
                            }
                            for (Commit commit : totalCommits) {
                                Committer committer = commit.getCommitter();
                                String username = committer.getUsername();
                                if (members.contains(username)) {
                                    commitsCounts++;
                                }
                            }
                            Map<String, Integer> stats = new HashMap<>();
                            stats.put("pulls", pullRequestsCount);
                            stats.put("commits", commitsCounts);
                            stats.put("issues", issuesCount);
                            res.put(project.getName(), stats);

                        } catch (Exception e) {
                            log.error("Github fetch error", e);
                            Map<String, Integer> stats = new HashMap<>();
                            stats.put("pulls", 0);
                            stats.put("commits", 0);
                            stats.put("issues", 0);
                            res.put(project.getName(), stats);
                        }
                        
                      
                        orgStats.setMonthly(monthly);
                        orgStats.setTime(Instant.now().toEpochMilli());
                        orgStats.setResponse(new GetOrgStatsResponse(res));
                        
                        orgStatsRepository.update(orgStats);

                    }
                } catch (Exception e) {
                    log.error("Thread error", e);
                }
                executorService.shutdown();
                return ResponseEntity.ok().body("hello");
              });
            }
            Set<Project> projects = org.getProjects();
            Map<String, Map<String, Integer>> res = new HashMap<>();
            for (Project project : projects) {
                try {
                    String link = project.getLink();
                    Set<ProjectRole> projectRoles = project.getProjectRoles();
                    Set<String> members = new HashSet<>();
                    for (ProjectRole role : projectRoles) {
                        User use_r = role.getUser();
                        members.add(use_r.getUsername());
                    }
                    PullRequest[] pullRequests = githubService.totalPullRequests(link, user.getAccesstoken(), monthly);
                    Issue[] totalIssues = githubService.totalIssues(link, user.getAccesstoken(), monthly);
                    Commit[] totalCommits = githubService.totalCommits(link, user.getAccesstoken(), monthly);
                    Integer pullRequestsCount = 0;
                    Integer issuesCount = 0;
                    Integer commitsCounts = 0;
                    for (PullRequest pullRequest : pullRequests) {
                        UserObject u_ser = pullRequest.getUser();
                        String username = u_ser.getUsername();
                        if (members.contains(username)) {
                            pullRequestsCount++;
                        }
                    }
                    for (Issue issue : totalIssues) {
                        UserObject u_ser = issue.getUser();
                        String username = u_ser.getUsername();
                        if (members.contains(username)) {
                            issuesCount++;
                        }
                    }
                    for (Commit commit : totalCommits) {
                        Committer committer = commit.getCommitter();
                        String username = committer.getUsername();
                        if (members.contains(username)) {
                            commitsCounts++;
                        }
                    }
                    Map<String, Integer> stats = new HashMap<>();
                    stats.put("pulls", pullRequestsCount);
                    stats.put("commits", commitsCounts);
                    stats.put("issues", issuesCount);
                    res.put(project.getName(), stats);

                } catch (Exception e) {
                    log.error("Github fetch error", e);
                    Map<String, Integer> stats = new HashMap<>();
                    stats.put("pulls", 0);
                    stats.put("commits", 0);
                    stats.put("issues", 0);
                    res.put(project.getName(), stats);
                }

            }
      
            String id= generateRandomString();
            OrgStats org_Stats= new OrgStats(id,org, new GetOrgStatsResponse(res), monthly, Instant.now().toEpochMilli());
            orgStatsRepository.save(org_Stats);
            return ResponseEntity.ok().body(new GetOrgStatsResponse(res));
        } catch (Exception e) {
            log.error("Internal Server Error", e);
            return ResponseEntity.internalServerError().body("Internal Server Error");
        }
    }

    @GetMapping("/{orgName}/{projectName}")
    public ResponseEntity<?> getProjectStats(@PathVariable String orgName, @PathVariable String projectName,
            @RequestParam(required = true) Boolean monthly, Principal principal) {
        try {
            Organization org = orgRepository.findByName(orgName).orElse(null);
            if (org == null) {
                return ResponseEntity.badRequest().body("Organization does not exists");
            }
            User user = userRepository.findByUsername(principal.getName()).orElse(null);
            Project project = projectRepository.findByNameAndOrganization(projectName, org).orElse(null);
            if (project == null) {
                return ResponseEntity.badRequest().body("Project in this organisation do not exists");
            }

            ProjectStats projectStats = projectStatsRepository
                    .findByOrganizationAndProjectAndMonthly(org, project, monthly).orElse(null);
            if (projectStats != null && Instant.now().toEpochMilli() - projectStats.getTime() >= 60000) {
                return ResponseEntity.ok().body(projectStats.getResponse());
            } else if (projectStats != null) {
                ExecutorService executorService = Executors.newFixedThreadPool(1);
                executorService.submit(() -> {
                    try {

                        Map<String, Map<String, Integer>> res = new HashMap<>();
                        Set<ProjectRole> projectRoles = project.getProjectRoles();
                        for (ProjectRole role : projectRoles) {
                            Map<String, Integer> memStats = new HashMap<>();
                            User use_r = role.getUser();
                            memStats.put("pulls", 0);
                            memStats.put("issues", 0);
                            memStats.put("commits", 0);
                            res.put(use_r.getUsername(), memStats);

                        }
                        String link = project.getLink();

                        try {
                            PullRequest[] pullRequests = githubService.totalPullRequests(link, user.getAccesstoken(),
                                    monthly);
                            Issue[] totalIssues = githubService.totalIssues(link, user.getAccesstoken(), monthly);
                            Commit[] totalCommits = githubService.totalCommits(link, user.getAccesstoken(), monthly);
                            for (PullRequest pullRequest : pullRequests) {
                                UserObject u_ser = pullRequest.getUser();
                                String username = u_ser.getUsername();
                                if (res.containsKey(username)) {
                                    Map<String, Integer> stat = res.get(username);
                                    stat.put("pulls", stat.get("pulls") + 1);
                                }
                            }

                            for (Issue issue : totalIssues) {
                                UserObject u_ser = issue.getUser();
                                String username = u_ser.getUsername();
                                if (res.containsKey(username)) {
                                    Map<String, Integer> stat = res.get(username);
                                    stat.put("issues", stat.get("issues") + 1);

                                }
                            }
                            for (Commit commit : totalCommits) {
                                Committer committer = commit.getCommitter();
                                String username = committer.getUsername();
                                if (res.containsKey(username)) {
                                    Map<String, Integer> stat = res.get(username);
                                    stat.put("commits", stat.get("commits") + 1);
                                }
                            }

                        } catch (Exception e) {
                            log.error("Github fetch error: ", e);
                        }
                        Map<String, Map<String, Integer>> sortedRes = sortByInnerMapValue(res, "pulls");
                        long unixTimeMillis = Instant.now().toEpochMilli();
                        projectStats.setTime(unixTimeMillis);
                        projectStats.setResponse(new GetProjectStatsResponse(sortedRes));
                        projectStats.setMonthly(monthly);
                        projectStatsRepository.update(projectStats);

                    } catch (Exception e) {
                        log.error("Thread Error", e);
                    }
                });
                executorService.shutdown();

                return ResponseEntity.ok().body(projectStats.getResponse());

            }

            Map<String, Map<String, Integer>> res = new HashMap<>();
            Set<ProjectRole> projectRoles = project.getProjectRoles();
            for (ProjectRole role : projectRoles) {
                Map<String, Integer> memStats = new HashMap<>();
                User use_r = role.getUser();
                memStats.put("pulls", 0);
                memStats.put("issues", 0);
                memStats.put("commits", 0);
                res.put(use_r.getUsername(), memStats);

            }
            String link = project.getLink();

            try {
                PullRequest[] pullRequests = githubService.totalPullRequests(link, user.getAccesstoken(), monthly);
                Issue[] totalIssues = githubService.totalIssues(link, user.getAccesstoken(), monthly);
                Commit[] totalCommits = githubService.totalCommits(link, user.getAccesstoken(), monthly);
                for (PullRequest pullRequest : pullRequests) {
                    UserObject u_ser = pullRequest.getUser();
                    String username = u_ser.getUsername();
                    if (res.containsKey(username)) {
                        Map<String, Integer> stat = res.get(username);
                        stat.put("pulls", stat.get("pulls") + 1);
                    }
                }

                for (Issue issue : totalIssues) {
                    UserObject u_ser = issue.getUser();
                    String username = u_ser.getUsername();
                    if (res.containsKey(username)) {
                        Map<String, Integer> stat = res.get(username);
                        stat.put("issues", stat.get("issues") + 1);

                    }
                }
                for (Commit commit : totalCommits) {
                    Committer committer = commit.getCommitter();
                    String username = committer.getUsername();
                    if (res.containsKey(username)) {
                        Map<String, Integer> stat = res.get(username);
                        stat.put("commits", stat.get("commits") + 1);
                    }
                }

            } catch (Exception e) {
                log.error("Github fetch error: ", e);
            }

            Map<String, Map<String, Integer>> sortedRes = sortByInnerMapValue(res, "pulls");

            String id=generateRandomString();
            ProjectStats projectStats2 = new ProjectStats(id,org, project, new GetProjectStatsResponse(sortedRes), monthly,
                    Instant.now().toEpochMilli());
            projectStatsRepository.save(projectStats2);
            return ResponseEntity.ok().body(new GetProjectStatsResponse(sortedRes));

        } catch (Exception e) {
            log.error("Internal Server Error ", e);
            return ResponseEntity.internalServerError().body("Internal Server Error");
        }
    }

  
    @GetMapping("/{orgName}/getRanks")
    public ResponseEntity<?> getRankings(@RequestParam(required = true) Boolean monthly, @PathVariable String orgName,
            Principal principal) {
        try {
         
           
            Organization org = orgRepository.findByName(orgName).orElse(null);
            if (org == null) {
                return ResponseEntity.badRequest().body("Organization does not exists");
            }
            User user = userRepository.findByUsername(principal.getName()).orElse(null);

            OrganizationRank organizationRank = organizationRankRepository.findByOrganizationAndMonthly(org, monthly)
                    .orElse(null);
                 
            long unixTimeMillis = Instant.now().toEpochMilli();

            if (organizationRank != null && (unixTimeMillis - organizationRank.getTime() <= 60000)) {
                return ResponseEntity.ok().body(organizationRank.getResponse());
            } else if (organizationRank != null) {
                ExecutorService executorService = Executors.newFixedThreadPool(1);
                executorService.submit(() -> {
                    try {
                        Set<Project> projects = org.getProjects();
                        Set<OrgRole> orgRoles = org.getOrgRoles();

                        Map<String, Map<String, Integer>> res = new HashMap<>();

                        for (OrgRole role : orgRoles) {
                            Map<String, Integer> memStats = new HashMap<>();
                            User use_r = role.getUser();
                            memStats.put("pulls", 0);
                            memStats.put("issues", 0);
                            memStats.put("commits", 0);
                            res.put(use_r.getUsername(), memStats);

                        }

                        for (Project project : projects) {
                            String link = project.getLink();
                            try {
                                PullRequest[] pullRequests = githubService.totalPullRequests(link,
                                        user.getAccesstoken(),
                                        monthly);
                                for (PullRequest pullRequest : pullRequests) {
                                    UserObject u_ser = pullRequest.getUser();
                                    String username = u_ser.getUsername();
                                    if (res.containsKey(username)) {
                                        Map<String, Integer> stat = res.get(username);
                                        stat.put("pulls", stat.get("pulls") + 1);
                                    }

                                }
                            } catch (Exception e) {
                                log.error("Github fetch error ", e);
                            }
                        }
                        Map<String, Map<String, Integer>> sortedRes = sortByInnerMapValue(res, "pulls");
                     
                        organizationRank.setTime(Instant.now().toEpochMilli());
                        organizationRank.setResponse(new GetProjectStatsResponse(sortedRes));
                        organizationRankRepository.update(organizationRank);
                        organizationRank.setMonthly(monthly);
                      

                    } catch (Exception e) {
                        log.error("Thread Exception ", e);
                    }

                });

                executorService.shutdown();

                return ResponseEntity.ok().body(organizationRank.getResponse());

            }

            Set<Project> projects = org.getProjects();
            Set<OrgRole> orgRoles = org.getOrgRoles();

            Map<String, Map<String, Integer>> res = new HashMap<>();

            for (OrgRole role : orgRoles) {
                Map<String, Integer> memStats = new HashMap<>();
                User use_r = role.getUser();
                memStats.put("pulls", 0);
                memStats.put("issues", 0);
                memStats.put("commits", 0);
                res.put(use_r.getUsername(), memStats);

            }

            for (Project project : projects) {
                String link = project.getLink();
                try {
                    PullRequest[] pullRequests = githubService.totalPullRequests(link, user.getAccesstoken(), monthly);
                    for (PullRequest pullRequest : pullRequests) {
                        UserObject u_ser = pullRequest.getUser();
                        String username = u_ser.getUsername();
                        if (res.containsKey(username)) {
                            Map<String, Integer> stat = res.get(username);
                            stat.put("pulls", stat.get("pulls") + 1);
                        }

                    }
                } catch (Exception e) {
                    log.error("Github fetch error ", e);
                }
            }
            Map<String, Map<String, Integer>> sortedRes = sortByInnerMapValue(res, "pulls");
            String id=generateRandomString();
            OrganizationRank orgRankRedis = new OrganizationRank(id,org, new GetProjectStatsResponse(sortedRes),
                    monthly, Instant.now().toEpochMilli());
            organizationRankRepository.save(orgRankRedis);

            return ResponseEntity.ok().body(new GetProjectStatsResponse(sortedRes));

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Internal Server Error");
        }
    }

    private static Map<String, Map<String, Integer>> sortByInnerMapValue(Map<String, Map<String, Integer>> map,
            String sortByKey) {
        List<Map.Entry<String, Map<String, Integer>>> entryList = new ArrayList<>(map.entrySet());

        // Comparator to compare entries based on the specified key in the inner map
        Comparator<Map.Entry<String, Map<String, Integer>>> valueComparator = Comparator
                .comparingInt(entry -> entry.getValue().get(sortByKey));

        // Sort the list of entries
        entryList.sort(valueComparator);

        // Create a LinkedHashMap to preserve the order
        Map<String, Map<String, Integer>> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<String, Map<String, Integer>> entry : entryList) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }


    private static String generateRandomString() {
        return UUID.randomUUID().toString().replace("-", "");
    }

}
