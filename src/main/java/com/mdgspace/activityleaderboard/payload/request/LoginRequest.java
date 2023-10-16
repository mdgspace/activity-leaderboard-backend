package com.mdgspace.activityleaderboard.payload.request;

import java.util.Set;
import jakarta.validation.constraints.*;

public class LoginRequest {
    
    @NotBlank
    @Size(min=3, max=20)
    private String code;

    public String getCode(){
        return code;
    }

    public void setCode(){
        this.code=code;
    }
}
