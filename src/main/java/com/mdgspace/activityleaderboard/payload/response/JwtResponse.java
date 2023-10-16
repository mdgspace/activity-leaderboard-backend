package com.mdgspace.activityleaderboard.payload.response;

public class JwtResponse {
    
    private String token;
    private String type="Bearer";
    private Long id;
    private String username;

    public JwtResponse(String token, Long id, String username, String email){
        this.token=token;
        this.id= id;
        this.username=username;
    }

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
