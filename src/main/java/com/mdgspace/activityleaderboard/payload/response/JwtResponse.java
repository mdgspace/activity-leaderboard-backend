package com.mdgspace.activityleaderboard.payload.response;


// Class for serializing the jwt_response
public class JwtResponse {
    
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
