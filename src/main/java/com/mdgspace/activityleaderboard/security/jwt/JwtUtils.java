package com.mdgspace.activityleaderboard.security.jwt;

import java.security.Key;
import java.util.Date;

import javax.swing.Spring;

import java.io.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.mdgspace.activityleaderboard.security.services.UserDetailsServiceImpl;
import com.mdgspace.activityleaderboard.security.services.UserDetailsImpl;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

@Component
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    private String jwtSecret="===============================mdgspace==========================";
     private int jwtExpirationMs=156548;


  

   
    public String generateJwtToken(Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
       
        return Jwts.builder().setSubject((userPrincipal.getUsername())).setIssuedAt(new Date()).setExpiration(new Date((new Date()).getTime()+jwtExpirationMs)).signWith(key(), SignatureAlgorithm.HS256).compact();

    }

    public String getUsernameFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build()
        .parseClaimsJws(token).getBody().getSubject();
    }

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public boolean validateJwtToken(String authtoken) {
        try {

            Jwts.parserBuilder().setSigningKey(key()).build().parse(authtoken);
            return true;

        } catch (MalformedJwtException e) {

            logger.error("Invalid JWT token: {}", e.getMessage());

        } catch (ExpiredJwtException e) {

            logger.error("JWT token is expired: {}", e.getMessage());

        } catch (UnsupportedJwtException e) {

            logger.error("JWT token is unsupported: {}", e.getMessage());

        } catch (IllegalArgumentException e) {

            logger.error("JWT claims string is empty: {}", e.getMessage());
            
        }

        return false;
    }

}
