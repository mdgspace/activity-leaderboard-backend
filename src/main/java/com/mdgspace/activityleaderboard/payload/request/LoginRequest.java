package com.mdgspace.activityleaderboard.payload.request;

import java.io.Serializable;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

// This class is used by spring boot to deserialize user login request
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest implements Serializable{
    
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
