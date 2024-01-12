package com.mdgspace.activityleaderboard.services.github.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.mdgspace.activityleaderboard.payload.github.Accesstoken;
import com.mdgspace.activityleaderboard.payload.github.Commit;
import com.mdgspace.activityleaderboard.payload.github.GithubUser;
import com.mdgspace.activityleaderboard.payload.github.Issue;
import com.mdgspace.activityleaderboard.payload.github.PullRequest;
import com.mdgspace.activityleaderboard.payload.github.Repository;
import com.nimbusds.oauth2.sdk.token.AccessToken;

import lombok.extern.slf4j.Slf4j;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;

@Slf4j
@Service
public class GithubServiceImpl implements GithubService {

    @Value("${GITHUB_CLIENT_SECRET}")
    private String client_secret;


    @Value("${GITHUB_CLIENT_ID}")
    private String client_id;

    @Value("${GITHUB_AUTH_URL}")
    private String githubAuthUrl;


    @Value("${GITHUB_API_URL}")
    private String githubApiUrl;

    @Override
    public Boolean isValidLink(String repolink, String acessToken) {
        try {
            String[] ans = extractOwnerAndRepo(repolink);
            if (ans[0] == null || ans[1] == null) {
                return false;
            }
            String owner = ans[0];
            String repo = ans[1];

            String url = githubApiUrl + "/repos/" + owner + "/" + repo;

            WebClient.Builder builder = WebClient.builder();

            Repository response = builder.build().get().uri(url).header("Accept", "application/json")
                    .header("Authorization", "Bearer " + acessToken).header("X-GitHub-Api-Version", "2022-11-28")
                    .retrieve().bodyToMono(Repository.class).block();

            if (response != null && repo.equals(response.getName())) {
                return true;
            }

            return false;

        } catch (Exception e) {
            log.error("Github fetch request error", e);
            return false;
        }

    }

    @Override
    public PullRequest[] totalPullRequests(String repoLink, String accessToken, Boolean month) {
        String[] ownerAndRepo = extractOwnerAndRepo(repoLink);
        PullRequest[] total_pull = {};
        WebClient.Builder builder = WebClient.builder();
        int pageNo = 1;

        String url = githubApiUrl + "/repos/" + ownerAndRepo[0] + "/" + ownerAndRepo[1] + "/"
                + "pulls?per_page=100&page=";

        while (true) {
            String tempUrl = url + String.valueOf(pageNo);

            PullRequest[] pulls = builder.build().get().uri(tempUrl).header("Accept", "Application/json")
                    .header("Authorization", "Bearer " + accessToken).header("X-GitHub-Api-Version", "2022-11-28")
                    .retrieve().bodyToMono(PullRequest[].class).block();
            if (pulls.length == 0) {
                break;
            }

            int total_pullLength = total_pull.length;
            int pullsLength = pulls.length;
            PullRequest[] result = new PullRequest[total_pullLength + pullsLength];
            System.arraycopy(total_pull, 0, result, 0, total_pullLength);
            System.arraycopy(pulls, 0, result, total_pullLength, pullsLength);
            total_pull = result;
            pageNo++;

        }

        if (month) {
            List<PullRequest> res = new ArrayList<>();
            LocalDateTime dateToComp = getFirstDayOfMonthAtMidnight();
            for (PullRequest pullRequest : total_pull) {
                if (dateToComp.isBefore(pullRequest.getCreated_at()) || dateToComp.isEqual(pullRequest.getCreated_at())
                        || dateToComp.isEqual(pullRequest.getUpdated_at())
                        || dateToComp.isBefore(pullRequest.getUpdated_at())) {
                    res.add(pullRequest);
                }
            }
            total_pull = res.toArray(new PullRequest[0]);
        } else {
            List<PullRequest> res = new ArrayList<>();
            LocalDateTime dateToComp = getPastMonday();
            for (PullRequest pullRequest : total_pull) {
                if (dateToComp.isBefore(pullRequest.getCreated_at()) || dateToComp.isEqual(pullRequest.getCreated_at())
                        || dateToComp.isEqual(pullRequest.getUpdated_at())
                        || dateToComp.isBefore(pullRequest.getUpdated_at())) {
                    res.add(pullRequest);
                }
            }
            total_pull = res.toArray(new PullRequest[0]);
        }

        return total_pull;

    }

    @Override
    public Issue[] totalIssues(String link, String accessToken, Boolean month) {
        String[] ownerAndRepo = extractOwnerAndRepo(link);
        WebClient.Builder builder = WebClient.builder();
        String url = "";
        if (month) {
            LocalDateTime since = getFirstDayOfMonthAtMidnight();
            System.out.println(since);
            url = githubApiUrl + "/repos/" + ownerAndRepo[0] + "/" + ownerAndRepo[1] + "/issues?since="+since+"&per_page=100&page=";
                    
        } else {
            LocalDateTime since = getPastMonday();
            url = githubApiUrl + "/repos/" + ownerAndRepo[0] + "/" + ownerAndRepo[1] + "/issues?since="+since+"&per_page=100&page=";
                     
        }

        Issue[] totalIssues = {};

        int pageNo = 1;
        while (true) {
            String tempUrl = url + String.valueOf(pageNo);

            Issue[] issues = builder.build().get().uri(tempUrl).header("Accept", "Application/json")
                    .header("Authorization", "Bearer " + accessToken).header("X-GitHub-Api-Version", "2022-11-28")
                    .retrieve().bodyToMono(Issue[].class).block();
            if (issues.length == 0) {
                break;
            }
            int total_issuesLength = totalIssues.length;
            int issuesLength = issues.length;
            Issue[] result = new Issue[total_issuesLength + issuesLength];
            System.arraycopy(totalIssues, 0, result, 0, total_issuesLength);
            System.arraycopy(issues, 0, result, total_issuesLength, issuesLength);
            totalIssues = result;
            
            pageNo++;
            break;
        }

        return totalIssues;

    }

    @Override
    public Commit[] totalCommits(String link, String accessToken, Boolean month) {
        String[] ownerAndRepo = extractOwnerAndRepo(link);
        Commit[] total_commits = {};
        String url = "";
        if (month) {
            LocalDateTime since = getFirstDayOfMonthAtMidnight();
            url = githubApiUrl + "/repos/" + ownerAndRepo[0] + "/" + ownerAndRepo[1] + "/commits?since=" + since
                    + "&per_page=100&page=";
        } else {
            LocalDateTime since = getPastMonday();
            url = githubApiUrl + "/repos/" + ownerAndRepo[0] + "/" + ownerAndRepo[1] + "/commits?since=" + since
                    + "&per_page=100&page=";
        }
       
        WebClient.Builder builder = WebClient.builder();
       
        int pageNo=1;
        while (true) {
           String tempUrl=url+String.valueOf(pageNo);
            Commit[] commits = builder.build().get().uri(tempUrl).header("Accept", "Application/json")
                    .header("Authorization", "Bearer " + accessToken).header("X-GitHub-Api-Version", "2022-11-28")
                    .retrieve().bodyToMono(Commit[].class).block();
            if (commits.length == 0) {
                break;
            }

            int total_commitsLength = total_commits.length;
            int commitsLength = commits.length;
            Commit[] result = new Commit[total_commitsLength + commitsLength];
            System.arraycopy(total_commits, 0, result, 0, total_commitsLength);
            System.arraycopy(commits, 0, result, total_commitsLength, commitsLength);
            total_commits = result;

            pageNo++;

        }
        

        return total_commits;
    }

    @Override
    public Optional<String> getAccesstoken(String code){

        String params = "?client_id=" + client_id + "&client_secret=" + client_secret + "&code=" + code;
        String auth_url = githubAuthUrl + params;
        WebClient.Builder builder = WebClient.builder();
        Accesstoken accesstokenResponse = builder.build().get().uri(auth_url).header("Accept", "application/json")
        .retrieve().bodyToMono(Accesstoken.class).block();

        String acess_token= accesstokenResponse.getAccesstoken();

        if(acess_token==null){
            return Optional.empty();
        }

        return Optional.of(acess_token);
        
    }

    @Override
    public Optional<String> getGithubUserName(String access_token){

          // Github_api endpoint to get userdetails
        String user_url = githubApiUrl + "/user";
        WebClient.Builder builder = WebClient.builder();
        GithubUser userResponse = builder.build().get().uri(user_url).header("Accept", "application/json")
        .header("Authorization", "Bearer " + access_token).header("X-GitHub-Api-Version", "2022-11-28")
        .retrieve().bodyToMono(GithubUser.class).block();
        String username= userResponse.getUsername();
        if(username==null){
            return Optional.empty();
        }
        return Optional.of(username);
    }

    private static String[] extractOwnerAndRepo(String repoLink) {
        String[] ans = { null, null };
        if (!repoLink.endsWith(".git")) {
            return ans;
        }
        repoLink = repoLink.substring(0, repoLink.length() - 4);
        Pattern pattern = Pattern.compile("https://github.com/(\\w+)/([^/]+)");
        Matcher matcher = pattern.matcher(repoLink);

        if (matcher.matches()) {
            String owner = matcher.group(1);
            String repository = matcher.group(2);
            ans[0] = owner;
            ans[1] = repository;
        }
        return ans;
    }

    public static LocalDateTime getPastMonday() {
        LocalDateTime now = LocalDateTime.now();

        // Calculate the past Monday using TemporalAdjusters
        LocalDateTime pastMonday = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                .withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);

        return pastMonday;
    }

    public static LocalDateTime getFirstDayOfMonthAtMidnight() {
        // Get the current date
        LocalDate currentDate = LocalDate.now();

        // Set the day of the month to 1
        LocalDate firstDayOfMonth = LocalDate.of(currentDate.getYear(), currentDate.getMonth(), 1);

        // Combine with midnight time
        LocalDateTime firstDayOfMonthMidnight = LocalDateTime.of(firstDayOfMonth, LocalTime.MIDNIGHT);

        return firstDayOfMonthMidnight;
    }

}
