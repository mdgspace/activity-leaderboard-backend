package com.mdgspace.activityleaderboard.services.aws.S3.exceptions;

public class FileEmptyException extends SpringBootFileUploadException{
    public FileEmptyException(String message){
        super(message);
    }
    
}
