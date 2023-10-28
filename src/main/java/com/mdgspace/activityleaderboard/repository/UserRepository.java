package com.mdgspace.activityleaderboard.repository;

// Import necessary Java and Spring Framework classes.
import java.util.Optional; // Import the Optional class to handle null values more gracefully.
import org.springframework.data.jpa.repository.JpaRepository; // Import JPA repository interface for CRUD operations.
import org.springframework.stereotype.Repository; // Import Repository annotation to indicate this is a Spring Repository.

import com.mdgspace.activityleaderboard.models.User; // Import the User model to be used with this repository.

// Annotate this interface as a Repository, marking it as a Spring bean.
// An interface is declared, implement it in other component classes. 
@Repository
public interface UserRepository extends JpaRepository<User,Long>{
    
    // Find a user by their username. Returns an Optional to handle nulls.
    Optional<User> findByUsername(String Username);
    
    // Check if a user exists using their username.
    Boolean existsByUsername(String username);
}
