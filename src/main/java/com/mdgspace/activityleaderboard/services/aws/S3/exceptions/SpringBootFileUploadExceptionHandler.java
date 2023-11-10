package com.mdgspace.activityleaderboard.services.aws.S3.exceptions;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.server.ServerWebExchange;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@ControllerAdvice
@Slf4j
public class SpringBootFileUploadExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = { FileEmptyException.class })
    protected Mono<ResponseEntity<Object>> handleFileEmptyException(FileEmptyException ex, ServerWebExchange request) {
        String bodyOfResponse = ex.getMessage();
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.NO_CONTENT, request);
    }

    @ExceptionHandler(value = { FileDownloadException.class })
    protected Mono<ResponseEntity<Object>> handleFileDownloadException(FileDownloadException ex,
            ServerWebExchange request) {
        String bodyOfResponse = ex.getMessage();
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

     @ExceptionHandler(value = { SpringBootFileUploadException.class })
    protected Mono<ResponseEntity<Object>> handleConflict(SpringBootFileUploadException ex,
            ServerWebExchange request) {
        String bodyOfResponse = ex.getMessage();
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    // Handle exceptions that occur when the call was transmitted successfully, but Amazon S3 couldn't process 
    // It, so it returned an error response

    @ExceptionHandler(value = {AmazonServiceException.class})
    protected Mono<ResponseEntity<Object>> handleAmazonServiceException(
        RuntimeException ex, ServerWebExchange request
    ){
      String bodyOfResponse= ex.getMessage();
      return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.CONFLICT, request);  
    }

    // Handle exceptions that occur when Amazon S3 couldn't be contacted for a response, or the client
    //  couldnt parse the response from Amazon s3

     @ExceptionHandler(value = {SdkClientException.class})
    protected Mono<ResponseEntity<Object>> handleClientException(
        RuntimeException ex, ServerWebExchange request
    ){
      String bodyOfResponse= ex.getMessage();
      return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.SERVICE_UNAVAILABLE, request);  
    }

     @ExceptionHandler(value = {IOException.class, FileNotFoundException.class, MultipartException.class})
    protected Mono<ResponseEntity<Object>> handleIOException(
        RuntimeException ex, ServerWebExchange request
    ){
      String bodyOfResponse= ex.getMessage();
      return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.SERVICE_UNAVAILABLE, request);  
    }

      @ExceptionHandler(value = {Exception.class})
    protected Mono<ResponseEntity<Object>> handleUnExpectedException(
        RuntimeException ex, ServerWebExchange request
    ){
      String bodyOfResponse= ex.getMessage();
      log.info("Exception ==> ", ex);
       log.info("Fatal exception ===> {}", bodyOfResponse);

      return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);  
    }



   

}
