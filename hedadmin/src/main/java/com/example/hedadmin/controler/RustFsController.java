//package com.example.hedadmin.controler;
//
//
//
//import com.example.hedadmin.response.Result;
//import com.example.hedadmin.service.RustFsService;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.InputStream;
//
//@RestController
//@RequestMapping("/api/rustfs")
//@RequiredArgsConstructor
//@Tag( name = "RustFs分布式存储接口")
////@Tag(name=)
//public class RustFsController {
//    private final RustFsService rustFsService;
//
//        @Operation(summary = "Test")
//        @GetMapping("/hello")
//        public Result<String> hello(){
//            return Result.success("ok");
//        }
//
//    // 1.创建桶
//    @Operation(summary = "创建桶")
//    @PostMapping("/bucket/create")
//    public Result<String> createBucket(@RequestParam String bucketName) {
//        String res = rustFsService.createBucket(bucketName);
//        return Result.success(res);
//    }
//
//    // 2.设置桶最大容量，单位字节
//    @Operation(summary = "设置桶最大容量，单位字节")
//    @PostMapping("/bucket/quota")
//    public Result<String> setQuota(@RequestParam String bucketName, @RequestParam long maxBytes) {
//        String res = rustFsService.setBucketQuota(bucketName, maxBytes);
//        return Result.success(res);
//    }
//
//    // 3.创建存储用户
//    @PostMapping("/user/create")
//    public Result<String> createUser(@RequestParam String username) {
//        String res = rustFsService.createUser(username);
//        return Result.success(res);
//    }
//
//    // 4.创建并绑定权限策略
//    @PostMapping("/policy/bind")
//    public Result<String> bindPolicy(@RequestParam String username,
//                                     @RequestParam String bucket,
//                                     @RequestBody String policyJson) {
//        String policyId = rustFsService.createAndBindPolicy(username, bucket, policyJson);
//        return Result.success(policyId);
//    }
//
//    // 5.文件上传
//    @PostMapping("/file/upload")
//    public Result<String> upload(@RequestParam String bucket,
//                                 @RequestParam String objectKey,
//                                 @RequestParam MultipartFile file) throws Exception {
//        try (InputStream is = file.getInputStream()) {
//            rustFsService.uploadFile(bucket, objectKey, is, file.getSize());
//        }
//        return Result.success("上传成功");
//    }
//
//    // 6.文件下载
//    @GetMapping("/file/download")
//    public org.springframework.core.io.Resource download(@RequestParam String bucket, @RequestParam String objectKey) {
//        InputStream inputStream = rustFsService.downloadFile(bucket, objectKey);
//        return new org.springframework.core.io.InputStreamResource(inputStream);
//    }
//}