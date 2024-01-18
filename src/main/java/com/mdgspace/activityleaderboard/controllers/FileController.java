package com.mdgspace.activityleaderboard.controllers;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpHeaders;

import com.mdgspace.activityleaderboard.models.Organization;
import com.mdgspace.activityleaderboard.models.User;
import com.mdgspace.activityleaderboard.models.enums.EOrgRole;
import com.mdgspace.activityleaderboard.models.roles.OrgRole;
import com.mdgspace.activityleaderboard.payload.response.FileResponse;
import com.mdgspace.activityleaderboard.payload.response.MessageResponse;
import com.mdgspace.activityleaderboard.repository.OrgRepository;
import com.mdgspace.activityleaderboard.repository.OrgRoleRepository;
import com.mdgspace.activityleaderboard.repository.UserRepository;
import com.mdgspace.activityleaderboard.services.aws.S3.exceptions.FileDownloadException;
import com.mdgspace.activityleaderboard.services.aws.S3.exceptions.FileEmptyException;
import com.mdgspace.activityleaderboard.services.aws.S3.exceptions.FileUploadException;
import com.mdgspace.activityleaderboard.services.aws.S3.service.FileService;

import io.netty.handler.codec.MessageAggregationException;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;


@CrossOrigin(origins = "*",maxAge=3600)
@RestController
@Slf4j
@RequestMapping("/api/protected/file")
@Validated
public class FileController {
    private final FileService fileService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    OrgRoleRepository orgRoleRepository;

    @Autowired
    OrgRepository orgRepository;


    public FileController(FileService fileUploadService){
        this.fileService=fileUploadService;
    }

    @PostMapping("/upload/{orgName}")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile multipartFile,@PathVariable String orgName ,Principal principal) throws FileEmptyException, FileUploadException, IOException{

        if(multipartFile.isEmpty()){
            throw new FileEmptyException("File is empty. Cannot save an empty file");

        }
       
         Organization org= orgRepository.findByName(orgName).orElse(null);
         String username=principal.getName();
         if(org==null){
             return ResponseEntity.badRequest().body(new MessageResponse("This organisation doesnot exists"));
           }
           
           User user= userRepository.findByUsername(username).orElse(null);

           OrgRole orgRole=orgRoleRepository.findByOrganizationAndUser(org, user).orElse(null);
           if(orgRole==null){
            return ResponseEntity.badRequest().body(new MessageAggregationException("User doesnot belongs to Organization"));
           }
           if(orgRole.getRole()!=EOrgRole.ADMIN){
            return ResponseEntity.badRequest().body(new MessageResponse("User is not the admin of the Organization"));
           }
        
        boolean isValidFile=isValidFile(multipartFile);
        List<String> allowedFileExtensions= new ArrayList<>(Arrays.asList("png","jpg","jpeg"));
        if(isValidFile && allowedFileExtensions.contains(FilenameUtils.getExtension(multipartFile.getOriginalFilename()))){
            String fileName= fileService.uploadFile(multipartFile);
            FileResponse fileResponse=FileResponse.builder().message("File uploaded successfully. File name =>"+fileName).isSuccessful(true).statusCode(200).build();
            org.setIcon(fileName);
            orgRepository.save(org);
            return new ResponseEntity<>(fileResponse,HttpStatus.OK);
        }else{
            FileResponse fileResponse= FileResponse.builder().message("Invalid File. File extension or File name is not supported").isSuccessful(false).statusCode(400).build();
            return new ResponseEntity<>(fileResponse, HttpStatus.BAD_REQUEST);
        }

       

    }

    @GetMapping("/getIcon/{orgName}")
    public ResponseEntity<?> getIcon(@PathVariable String orgName) throws FileDownloadException, IOException{

          Organization org= orgRepository.findByName(orgName).orElse(null);
          if(org==null){
            return ResponseEntity.badRequest().body(new MessageResponse("This organisation doesnot exists"));
          }

          String fileName= org.getIcon();
          if(fileName==null){
            return ResponseEntity.badRequest().body(new MessageResponse("This organisation doesnot have icon image"));
          }
      
          Object response= fileService.downloadFile(fileName);

        if(response!=null){

            HttpHeaders headers= new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+fileName+"\"");
            return ResponseEntity.ok().headers(headers).body(response);
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
