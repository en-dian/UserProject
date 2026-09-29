//package com.example.hedadmin.service;
//
//
//import com.alibaba.fastjson2.JSON;
//import com.example.hedadmin.config.RustFsConfig;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.web.reactive.function.client.WebClient;
//import reactor.core.publisher.Mono;
//import software.amazon.awssdk.core.ResponseInputStream;
//import software.amazon.awssdk.core.sync.RequestBody;
//import software.amazon.awssdk.services.s3.S3Client;
//import software.amazon.awssdk.services.s3.model.*;
//
//import java.io.InputStream;
//import java.util.Map;
//
//@Service
//@RequiredArgsConstructor
//public class RustFsService {
//    private final WebClient rustFsWebClient;
//    private final RustFsConfig rustFsConfig;
//    private final S3Client s3Client;
//
//    //==================== 桶操作 ====================
//    /**
//     * 创建桶
//     */
//    public String createBucket(String bucketName) {
//        Mono<String> respMono = rustFsWebClient.post()
//                .uri("/api/buckets")
//                .bodyValue(Map.of("name", bucketName))
//                .retrieve()
//                .bodyToMono(String.class);
//        return respMono.block();
//    }
//
//    /**
//     * 设置桶容量配额（调整桶大小限制）
//     * quotaUnit: bytes
//     */
//    public String setBucketQuota(String bucketName, long maxSizeBytes) {
//        Mono<String> respMono = rustFsWebClient.put()
//                .uri("/api/buckets/{bucket}/quota", bucketName)
//                .bodyValue(Map.of("max_size", maxSizeBytes))
//                .retrieve()
//                .bodyToMono(String.class);
//        return respMono.block();
//    }
//
//    /**
//     * 删除桶
//     */
//    public String deleteBucket(String bucketName) {
//        Mono<String> respMono = rustFsWebClient.delete()
//                .uri("/api/buckets/{bucket}", bucketName)
//                .retrieve()
//                .bodyToMono(String.class);
//        return respMono.block();
//    }
//
//    //==================== 用户（AK/SK）管理 ====================
//    /**
//     * 创建rustfs存储用户
//     */
//    public String createUser(String userName) {
//        Mono<String> respMono = rustFsWebClient.post()
//                .uri("/api/users")
//                .bodyValue(Map.of("username", userName))
//                .retrieve()
//                .bodyToMono(String.class);
//        return respMono.block();
//    }
//
//    //==================== 权限策略Policy ====================
//    /**
//     * 创建策略并绑定用户/桶
//     * policyJson：标准S3 policy字符串
//     */
//    public String createAndBindPolicy(String userName, String bucket, String policyJson) {
//        // 1.创建策略
//        Mono<String> policyResp = rustFsWebClient.post()
//                .uri("/api/policies")
//                .bodyValue(Map.of("policy", policyJson, "bucket", bucket))
//                .retrieve()
//                .bodyToMono(String.class);
//        String policyId = policyResp.block();
//
//        // 2.绑定策略到用户
//        rustFsWebClient.post()
//                .uri("/api/users/{user}/policies", userName)
//                .bodyValue(Map.of("policy_id", policyId))
//                .retrieve()
//                .bodyToMono(String.class)
//                .block();
//        return policyId;
//    }
//
//    //==================== 文件上传下载（使用S3 SDK） ====================
//    /**
//     * 文件上传
//     */
//    public void uploadFile(String bucket, String objectKey, InputStream inputStream, long contentLength) {
//        PutObjectRequest request = PutObjectRequest.builder()
//                .bucket(bucket)
//                .key(objectKey)
//                .build();
//        s3Client.putObject(request, RequestBody.fromInputStream(inputStream, contentLength));
//    }
//
//    /**
//     * 文件下载
//     */
//    public InputStream downloadFile(String bucket, String objectKey) {
//        GetObjectRequest request = GetObjectRequest.builder()
//                .bucket(bucket)
//                .key(objectKey)
//                .build();
//        ResponseInputStream<GetObjectResponse> response = s3Client.getObject(request);
//        return response;
//    }
//}