package com.mdgspace.activityleaderboard.services.github.service;

import com.mdgspace.activityleaderboard.payload.github.Issue;
import com.mdgspace.activityleaderboard.payload.github.PullRequest;

public interface GithubService {
   
    public Boolean isValidLink(String repoLink,String accessToken);

    public PullRequest[] totalPullRequests(String repoLink,String accessToken ,Boolean week, Boolean month);

} 