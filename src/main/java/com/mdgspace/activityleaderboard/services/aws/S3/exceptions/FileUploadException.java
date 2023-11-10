package com.mdgspace.activityleaderboard.services.aws.S3.exceptions;

public class FileUploadException extends SpringBootFileUploadException{
 
    public FileUploadException(String message){
        super(message);
    }
}
