package com.mdgspace.activityleaderboard.models;



import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


import java.util.Set;



import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mdgspace.activityleaderboard.models.roles.OrgRole;

import java.io.Serializable;
import java.util.HashSet;


@Entity
@Table(name="organizations", uniqueConstraints = {
    @UniqueConstraint(columnNames = "name")
})
public class Organization implements Serializable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max=40)
    private String name;

    @Size(max = 200)
    private String description;

    @JsonIgnore
    @Size(max= 60)
    private String icon;

    @JsonIgnore
    @OneToMany(mappedBy = "organization")
    private Set<Project> projects=new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "organization")
    private Set<OrgRole> orgRoles=new HashSet<>();
 
    public Organization(){

    }

    public Organization(String name, String description, String icon){
      this.name=name;
      this.description=description;
      this.icon=icon;
    }

    public String getName(){
        return name;
    }

    public Long getId(){
        return id;
    }

    public Set<OrgRole> getOrgRoles(){
        return orgRoles;
    }

    public String getDescription(){
        return description;
    }

    public String getIcon(){
        return icon;
    }

    public Set<Project> getProjects(){
        return projects;
    }


    public void setId(Long id){
        this.id=id;
    }

    public void setOrgRoles(Set<OrgRole> orgRoles){
        this.orgRoles=orgRoles;
    }

    public void setName(String name){
        this.name=name;
    }

    public void setIcon(String icon){
        this.icon=icon;
    }

    public void setDescription(String description){
        this.description=description;
    }

}
