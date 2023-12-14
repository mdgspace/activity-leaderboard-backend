package com.mdgspace.activityleaderboard.services.aws.S3.service;

import com.mdgspace.activityleaderboard.services.aws.S3.exceptions.FileDownloadException;
import com.mdgspace.activityleaderboard.services.aws.S3.exceptions.FileUploadException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {

    String uploadFile(MultipartFile multipartFile) throws FileUploadException, IOException;

    Object downloadFile(String fileName) throws FileDownloadException, IOException;

    boolean delete(String fileName);
    
} 
