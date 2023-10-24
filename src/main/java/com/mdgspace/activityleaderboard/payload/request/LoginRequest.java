package com.mdgspace.activityleaderboard.payload.request;

import jakarta.validation.constraints.*;

// This class is used by spring boot to deserialize user login request
public class LoginRequest {
    
    @NotBlank
    @Size(min=3, max=20)
    private String code;

    // getter and setter function atre defined
    public String getCode(){
        return code;
    }

    public void setCode(String code){
        this.code=code;
    }
}
