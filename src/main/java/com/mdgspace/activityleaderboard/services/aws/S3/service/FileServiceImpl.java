package com.mdgspace.activityleaderboard.services.aws.S3.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.mdgspace.activityleaderboard.services.aws.S3.exceptions.FileDownloadException;

// import com.ehizman.springboot_file_upload.exceptions.FileDownloadException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;



import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;



@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService{

    @Value("${aws.bucket.name}")
    private String bucketName;

    private final AmazonS3 s3Client;

    @Override
    public String uploadFile(MultipartFile multipartFile) throws IOException{
    //    Convert multipart file to a file

    File file= new File(multipartFile.getOriginalFilename());
    try(FileOutputStream fileOutputStream=new FileOutputStream(file)){
        fileOutputStream.write(multipartFile.getBytes());
    }
     
    //  generate file name

    String fileName= generateFileName(multipartFile);

    // upload file
    PutObjectRequest request= new PutObjectRequest(bucketName, fileName, file);
    ObjectMetadata metadata= new ObjectMetadata();
    metadata.setContentType("plain/"+ FilenameUtils.getExtension(multipartFile.getOriginalFilename()));
    metadata.addUserMetadata("Title", "File Uplaod - "+fileName);
    metadata.setContentLength(file.length());
    request.setMetadata(metadata);
    s3Client.putObject(request);

    // Delete file

    return fileName;



    }

    @Override
    public Object downloadFile(String fileName) throws FileDownloadException, IOException{
        if(bucketIsEmpty()){
            throw new FileDownloadException("Requested bucket does not exist or is empty");
        }

        S3Object object= s3Client.getObject(bucketName, fileName);

        try(S3ObjectInputStream s3is=object.getObjectContent()){
            try(FileOutputStream fileOutputStream= new FileOutputStream(fileName)){
                byte[] read_buf= new byte[1024];
                int read_len=0;
                while((read_len=s3is.read(read_buf))>0){
                    fileOutputStream.write(read_buf, 0, read_len);

                }
            }

            Path pathObject=Paths.get(fileName);
            

            Resource resource= new UrlResource(pathObject.toUri());
  
            if(resource.exists()||resource.isReadable()){
                return resource;
                
            }else{
                throw new FileDownloadException("Could not find the file!");

            }
        }

    }
    @Override
    public boolean delete(String fileName) {
        File file = Paths.get(fileName).toFile();
        if (file.exists()) {
            file.delete();
            return true;
        }
        return false;
    }
 
    private boolean bucketIsEmpty(){
        ListObjectsV2Result result = s3Client.listObjectsV2(this.bucketName);
        if (result == null){
            return false;
        }
        List<S3ObjectSummary> objects = result.getObjectSummaries();
        return objects.isEmpty();


    }

    private String generateFileName(MultipartFile multiPart) {
        return new Date().getTime() + "-" + multiPart.getOriginalFilename().replace(" ", "_");
    }


}
