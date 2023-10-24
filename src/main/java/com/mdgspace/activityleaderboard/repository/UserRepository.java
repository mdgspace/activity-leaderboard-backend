package com.mdgspace.activityleaderboard.repository;


import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mdgspace.activityleaderboard.models.User;

@Repository
public interface UserRepository extends JpaRepository<User,Long>{
    
    Optional<User> findByUsername(String Username);
    
    Boolean existsByUsername(String username);
}
