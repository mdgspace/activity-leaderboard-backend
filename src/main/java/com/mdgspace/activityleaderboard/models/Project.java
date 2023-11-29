
//  WIP
package com.mdgspace.activityleaderboard.models;

import java.util.HashSet;

import java.util.Set;


import com.mdgspace.activityleaderboard.models.roles.ProjectRole;

import jakarta.persistence.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name="projects",uniqueConstraints = {
    @UniqueConstraint(columnNames = "link")
})
public class Project {
    
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="name")
    @NotBlank
    @Size(max=10)
    private String name;


    @NotBlank
    @Size(max=30)
    private String link;

    @Column(name = "description")
    @Size(max=40)
    private String description;

    @Column(name = "bookmarked")
    private Boolean bookmarked = false;

    @Column(name="archeive")
    private Boolean archeive= false;

    @ManyToOne(fetch =FetchType.LAZY, optional = false)
    @JoinColumn(name="organization_id",nullable = false)
    private Organization organization;

    @OneToMany(mappedBy = "project")
    private Set<ProjectRole> projectRoles=new HashSet<>();



    public Project(){

    }

    public Project(String name, String link, String description, Organization organization) {
        this.name = name;
        this.description = description;
        this.link = link;
        this.organization=organization;
    }

    
   public Long getId(){
    return id;
   }

   public String getName(){
    return name;
   }

   public String getLink(){
    return link;
   }

   public String getDescription(){
    return description;
   }

   public boolean getBookmarked(){
    return bookmarked;
   }

   public boolean getArcheive(){
    return archeive;
   }

   public Set<ProjectRole> getProjectRoles(){
    return projectRoles;
   }

   public Organization getOrganization(){
    return organization;
   }

   public void setId(Long id){
     this.id=id;
   }

   public void setName(String name){
    this.name=name;
   }

   public void setDescription(String description){
    this.description=description;
   }

   public void setBookmarked(boolean bookmarked){
    this.bookmarked=bookmarked;
   }

   public void setLink(String link){
    this.link=link;
   }

   public void setArcheive(boolean archeive){
    this.archeive=archeive;
   }

   public void setProjectRoles(Set<ProjectRole> projectRoles){
    this.projectRoles=projectRoles;
   }


   


}
