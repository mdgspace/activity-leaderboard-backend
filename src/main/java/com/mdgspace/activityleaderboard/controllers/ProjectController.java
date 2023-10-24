package com.mdgspace.activityleaderboard.controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*",maxAge=3600)
@RestController

@RequestMapping("/api/protected/project")
public class ProjectController {
    
}
