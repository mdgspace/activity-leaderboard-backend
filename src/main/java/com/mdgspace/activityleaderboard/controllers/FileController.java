package com.mdgspace.activityleaderboard.controllers;

import java.io.IOException;
import java.net.http.HttpHeaders;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.apache.commons.io.FilenameUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mdgspace.activityleaderboard.payload.response.FileResponse;
import com.mdgspace.activityleaderboard.services.aws.S3.exceptions.FileDownloadException;
import com.mdgspace.activityleaderboard.services.aws.S3.exceptions.FileEmptyException;
import com.mdgspace.activityleaderboard.services.aws.S3.exceptions.FileUploadException;
import com.mdgspace.activityleaderboard.services.aws.S3.service.FileService;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/api/auth/file")
@Validated
public class FileController {
    private final FileService fileService;

    public FileController(FileService fileUploadService){
        this.fileService=fileUploadService;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile multipartFile) throws FileEmptyException, FileUploadException, IOException{
        if(multipartFile.isEmpty()){
            throw new FileEmptyException("File is empty. Cannot save an empty file");

        }
        boolean isValidFile=isValidFile(multipartFile);
        List<String> allowedFileExtensions= new ArrayList<>(Arrays.asList("png","jpg","jpeg"));
        if(isValidFile && allowedFileExtensions.contains(FilenameUtils.getExtension(multipartFile.getOriginalFilename()))){
            String fileName= fileService.uploadFile(multipartFile);
            FileResponse fileResponse=FileResponse.builder().message("File uploaded successfully. File name =>"+fileName).isSuccessful(true).statusCode(200).build();
            return new ResponseEntity<>(fileResponse,org.springframework.http.HttpStatus.OK);
        }else{
            FileResponse fileResponse= FileResponse.builder().message("Invalid File. File extension or File name is not supported").isSuccessful(false).statusCode(400).build();
            return new ResponseEntity<>(fileResponse, HttpStatus.BAD_REQUEST);
        }


    }

    @GetMapping("/download")
    public ResponseEntity<?> downloadFile(@RequestParam("file") @NotBlank @NotNull String fileName) throws FileDownloadException, IOException{

        Object response= fileService.downloadFile(fileName);

        if(response!=null){
            return ResponseEntity.ok().header(org.springframework.http.HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\""+fileName+"\"").body(response);
        }
        else {
            FileResponse fileResponse = FileResponse.builder()
                    .message("File could not be downloaded")
                    .isSuccessful(false)
                    .statusCode(400)
                    .build();
            return new ResponseEntity<>(fileResponse, HttpStatus.NOT_FOUND);
        }
 
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(@RequestParam("fileName") @NotBlank @NotNull String fileName){
        boolean isDeleted = fileService.delete(fileName);
        if(isDeleted){
            FileResponse fileResponse=FileResponse.builder().message("file deleted!").statusCode(200).build();
            return new ResponseEntity<>(fileResponse,HttpStatus.OK);
        }
        else{
            FileResponse fileResponse = FileResponse.builder().message("file does not exist")
               .statusCode(404).build();
               return new ResponseEntity<>(fileResponse, HttpStatus.NOT_FOUND);
        }
    }
    private boolean isValidFile(MultipartFile multipartFile){
        log.info("Empty Status ==> {}", multipartFile.isEmpty());
        if (Objects.isNull(multipartFile.getOriginalFilename())){
            return false;
        }
        return !multipartFile.getOriginalFilename().trim().equals("");
    }

}
