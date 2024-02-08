package com.mdgspace.activityleaderboard.services.aws.S3;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Configuration

public class S3ClientConfig {

    @Value("${aws.accessKey}")

    private String accessKey;

    @Value("${aws.secretKey}")

    private String secretKey;

    @Value("${aws.url}")

    private String url;

    @Value("${aws.region}")

    private String region;

    @Bean

    public AmazonS3 initS3Client() {

        return AmazonS3ClientBuilder.standard().withCredentials(getCredentialsProvider())
                .withEndpointConfiguration(getEndPointConfiguration(url)).build();

    }

    private EndpointConfiguration getEndPointConfiguration(String url) {

        return new EndpointConfiguration(url, region);

    }

    private AWSStaticCredentialsProvider getCredentialsProvider() {

        return new AWSStaticCredentialsProvider(getBasicAWSCredentials());

    }

    private BasicAWSCredentials getBasicAWSCredentials() {

        return new BasicAWSCredentials(accessKey, secretKey);

    }

}