package com.mdgspace.activityleaderboard.services.github.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;

import com.mdgspace.activityleaderboard.payload.github.Repository;

import lombok.extern.slf4j.Slf4j;


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
}
