//package com.example.hedadmin.config;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.reactive.function.client.WebClient;
//import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
//import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
//import software.amazon.awssdk.regions.Region;
//import software.amazon.awssdk.services.s3.S3Client;
//import software.amazon.awssdk.services.s3.S3Configuration;
//
//import java.net.URI;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
//import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
//import software.amazon.awssdk.regions.Region;
//import software.amazon.awssdk.services.s3.S3Client;
//import software.amazon.awssdk.services.s3.S3Configuration;
//
//import java.net.URI;
//import java.nio.charset.StandardCharsets;
//import java.util.Base64;
//
//@Configuration
//public class RustFsConfig {
//
//    private final String endpoint = "http://192.168.29.50:32045/";; // 请使用实际地址
//    private final String accessKey = "admin";
//    private final String secretKey = "123456";
//
//    @Bean
//    public S3Client s3Client() {
//        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);
//
//        return S3Client.builder()
//                .endpointOverride(URI.create(endpoint))
//                .region(Region.US_EAST_1) // 自定义 endpoint 时 region 可任意
//                .credentialsProvider(StaticCredentialsProvider.create(credentials))
//                .serviceConfiguration(S3Configuration.builder()
//                        .pathStyleAccessEnabled(true)   // 强制路径风格（关键！）
//                        .build())
//                .build();
//    }
//    @Bean
//    public WebClient rustFsWebClient(WebClient.Builder builder) {
//        // 构建 Basic Auth 头
//        String auth = accessKey + ":" + secretKey;
//        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
//        String authHeader = "Basic " + encodedAuth;
//
//        return builder
//                .baseUrl(endpoint)
//                .defaultHeader("Authorization", authHeader)
//                .build();
//    }
//}