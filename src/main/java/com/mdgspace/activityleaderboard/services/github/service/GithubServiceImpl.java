package com.mdgspace.activityleaderboard.services.github.service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;

import com.mdgspace.activityleaderboard.payload.github.Issue;
import com.mdgspace.activityleaderboard.payload.github.PullRequest;
import com.mdgspace.activityleaderboard.payload.github.Repository;

import lombok.extern.slf4j.Slf4j;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;

@Slf4j
public class GithubServiceImpl implements GithubService {

    
    @Value("${GITHUB_CLIENT_SECRET}")
    private String client_secret;

    @Value("${GITHUB_API_URL}")
    private String githubApiUrl;


    @Override
    public Boolean isValidLink(String repolink, String acessToken){
        try{
            String[] ans = extractOwnerAndRepo(repolink);
            if(ans[0]==null||ans[1]==null){
                return false;
            }
            String owner=ans[0];
            String repo=ans[1];
            
            String url= githubApiUrl+"/repos/"+owner+"/"+repo;
            
            WebClient.Builder builder= WebClient.builder();

           Repository response= builder.build().get().uri(url).header("Accept","application/json").header("Authorization", "Bearer "+acessToken).header("X-GitHub-Api-Version", "2022-11-28").retrieve().bodyToMono(Repository.class).block();
           if(response!=null&&response.getName()==repo){
            return true;
           }
          
            return false;

        }catch(Exception e){
            log.error("Github fetch request error", e);
           return false;
        }

    }


    @Override
    public PullRequest[] totalPullRequests(String repoLink, String accessToken,Boolean week, Boolean month ){
      String[] ownerAndRepo=extractOwnerAndRepo(repoLink);
      PullRequest[] total_pull={};
      WebClient.Builder builder=WebClient.builder();
      int pageNo=1;
      String url="";
      if(week){
         url = githubApiUrl+"repos/"+ownerAndRepo[0]+"/"+ownerAndRepo[1]+"/"+"pulls?state=all?per_page=100?page=";
      }
      else if(month){
        url = githubApiUrl+"repos/"+ownerAndRepo[0]+"/"+ownerAndRepo[1]+"/"+"pulls?state=all?per_page=100?page=";
      }else{
        url = githubApiUrl+"repos/"+ownerAndRepo[0]+"/"+ownerAndRepo[1]+"/"+"pulls?state=all?per_page=100?page=";
      }
      while(true){
        url=url+pageNo;

        PullRequest[] pulls= builder.build().get().uri(url).header("Accept", "Application/json").header("Authorization", "Bearer "+accessToken).header("X-GitHub-Api-Version", "2022-11-28").retrieve().bodyToMono(PullRequest[].class).block();
        if(pulls.length==0){
            break;
        }

        int total_pullLength=total_pull.length;
        int pullsLength=pulls.length;
        PullRequest[] result=new PullRequest[total_pullLength+pullsLength];
        System.arraycopy(total_pull, 0, result, 0, total_pullLength);
        System.arraycopy(pulls,0,result,total_pullLength,pullsLength);
        total_pull=result;
        pageNo++;
        
      }

      if(week){
        List<PullRequest> res= new ArrayList();
        LocalDateTime dateToComp= getPastMonday();
        for(PullRequest pullRequest:total_pull){
           if(dateToComp.isBefore(pullRequest.getCreated_at())|| dateToComp.isEqual(pullRequest.getCreated_at()) || dateToComp.isEqual(pullRequest.getUpdated_at()) || dateToComp.isBefore(pullRequest.getUpdated_at())){
             res.add(pullRequest);
           }
        }
        total_pull=res.toArray(new PullRequest[0]);
      }

      else if(month){
        List<PullRequest> res= new ArrayList();
        LocalDateTime dateToComp= getFirstDayOfMonthAtMidnight();
        for(PullRequest pullRequest:total_pull){
           if(dateToComp.isBefore(pullRequest.getCreated_at())|| dateToComp.isEqual(pullRequest.getCreated_at()) || dateToComp.isEqual(pullRequest.getUpdated_at()) || dateToComp.isBefore(pullRequest.getUpdated_at())){
             res.add(pullRequest);
           }
        }
        total_pull=res.toArray(new PullRequest[0]);
      }

      return total_pull;
      
    }


    

    private static String[] extractOwnerAndRepo(String repoLink) {
        String[] ans = {null,null};
        if(!repoLink.endsWith(".git")){
            return ans;
        }
        repoLink=repoLink.substring(0, repoLink.length() - 4);
        Pattern pattern = Pattern.compile("https://github.com/(\\w+)/([^/]+)");
        Matcher matcher = pattern.matcher(repoLink);

        if (matcher.matches()) {
            String owner = matcher.group(1);
            String repository = matcher.group(2);
            ans[0]=owner;
            ans[1]=repository;
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
