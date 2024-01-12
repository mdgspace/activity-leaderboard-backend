package com.mdgspace.activityleaderboard.services.github.service;

import java.util.Optional;

import com.mdgspace.activityleaderboard.payload.github.Commit;
import com.mdgspace.activityleaderboard.payload.github.Issue;
import com.mdgspace.activityleaderboard.payload.github.PullRequest;

public interface GithubService {
   
    public Boolean isValidLink(String repoLink,String accessToken);

    public PullRequest[] totalPullRequests(String repoLink,String accessToken , Boolean month);

    public Issue[] totalIssues(String link,String accessToken, Boolean month);


    public Commit[]  totalCommits(String repolink, String accessToken,Boolean month);

    Optional<String> getAccesstoken(String code);

    Optional<String> getGithubUserName(String access_token);

} 