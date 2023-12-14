package com.mdgspace.activityleaderboard.services.aws.S3.exceptions;

public class FileDownloadException extends SpringBootFileUploadException{
    
    public FileDownloadException(String message){
        super(message);
    }
}
