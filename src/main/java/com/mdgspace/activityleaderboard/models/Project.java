
//  WIP
package com.mdgspace.activityleaderboard.models;

import jakarta.persistence.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name="projects",uniqueConstraints = {
    @UniqueConstraint(columnNames = "name"),
    @UniqueConstraint(columnNames = "link")
})
public class Project {
    
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    


}
