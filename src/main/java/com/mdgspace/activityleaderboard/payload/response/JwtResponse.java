package com.mdgspace.activityleaderboard.payload.response;

import java.io.Serializable;

// Class for serializing the jwt_response
public class JwtResponse implements Serializable  {
    
    // JWT Access token
    private String token;

    // Type of token
    private String type="Bearer";

    //  ID from usermodel
    private Long id;

    // Username of user
    private String username;

    public JwtResponse(String token, Long id, String username){
        this.token=token;
        this.id= id;
        this.username=username;
    }
    //  Getter and setters

    public String getToken(){
        return token;
    }

    public void setToken(String token){
        this.token=token;
    }

    public String getType(){
        return type;
    }

    public void setType(String type){
       this.type=type;
    }

    public Long getId(){
        return id;
    }

    public void settId(Long id){
        this.id=id;
    }

    public String getUsername() {
        return username;
      }
    
      public void setUsername(String username) {
        this.username = username;
      }

}
