package com.mdgspace.activityleaderboard.payload.response;

import java.io.Serializable;

// Class to serialize message
public class MessageResponse implements Serializable{
    
    private String message;

    public MessageResponse(String message){
        this.message=message;
    }

    public String getMessage(){
        return message;
    }

    public void setMessage(String message){
        this.message=message;
    }

}
