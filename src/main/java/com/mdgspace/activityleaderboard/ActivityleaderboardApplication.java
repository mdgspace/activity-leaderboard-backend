package com.mdgspace.activityleaderboard;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;



@ServletComponentScan
@SpringBootApplication
public class ActivityleaderboardApplication {


	public static void main(String[] args) {

		SpringApplication.run(ActivityleaderboardApplication.class, args);
	}

}
