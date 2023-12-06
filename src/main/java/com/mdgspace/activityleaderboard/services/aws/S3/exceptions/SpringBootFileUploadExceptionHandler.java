package com.mdgspace.activityleaderboard.services.aws.S3.exceptions;

import java.io.FileNotFoundException;
import java.io.IOException;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.server.ServerWebExchange;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;

import lombok.extern.slf4j.Slf4j;


@ControllerAdvice
@Slf4j
public class SpringBootFileUploadExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = { FileEmptyException.class })
    protected ResponseEntity<?> handleFileEmptyException(FileEmptyException ex) {
        String bodyOfResponse = ex.getMessage();
        return new ResponseEntity<>(bodyOfResponse,HttpStatus.NO_CONTENT);
    }

    @ExceptionHandler(value = { FileDownloadException.class })
    protected ResponseEntity<?> handleFileDownloadException(FileDownloadException ex) {
        String bodyOfResponse = ex.getMessage();
         return new ResponseEntity<>(bodyOfResponse,HttpStatus.BAD_REQUEST);
    }

     @ExceptionHandler(value = { SpringBootFileUploadException.class })
    protected ResponseEntity<?> handleConflict(SpringBootFileUploadException ex,
            ServerWebExchange request) {
        String bodyOfResponse = ex.getMessage();
        return new ResponseEntity<>(bodyOfResponse,HttpStatus.BAD_REQUEST);
    }

    // Handle exceptions that occur when the call was transmitted successfully, but Amazon S3 couldn't process 
    // It, so it returned an error response

    @ExceptionHandler(value = {AmazonServiceException.class})
    protected ResponseEntity<?> handleAmazonServiceException(
        RuntimeException ex
    ){
      String bodyOfResponse= ex.getMessage();
        return new ResponseEntity<>(bodyOfResponse, HttpStatus.CONFLICT);
    }

    // Handle exceptions that occur when Amazon S3 couldn't be contacted for a response, or the client
    //  couldnt parse the response from Amazon s3

     @ExceptionHandler(value = {SdkClientException.class})
    protected ResponseEntity<?> handleClientException(
        RuntimeException ex
    ){
      String bodyOfResponse= ex.getMessage();
      return new ResponseEntity<>(bodyOfResponse,HttpStatus.SERVICE_UNAVAILABLE);  
    }

     @ExceptionHandler(value = {IOException.class, FileNotFoundException.class, MultipartException.class})
    protected ResponseEntity<?> handleIOException(
        RuntimeException ex
    ){
      String bodyOfResponse= ex.getMessage();
      return new ResponseEntity<>(bodyOfResponse,HttpStatus.SERVICE_UNAVAILABLE); 
    }

      @ExceptionHandler(value = {Exception.class})
    protected ResponseEntity<?> handleUnExpectedException(
        RuntimeException ex
    ){
      String bodyOfResponse= ex.getMessage();
      log.info("Exception ==> ", ex);
       log.info("Fatal exception ===> {}", bodyOfResponse);

      return new ResponseEntity<>(bodyOfResponse,HttpStatus.INTERNAL_SERVER_ERROR);  
    }



   

}
