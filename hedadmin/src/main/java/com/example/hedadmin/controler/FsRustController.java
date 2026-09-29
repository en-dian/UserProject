package com.example.hedadmin.controler;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.example.hedadmin.DTO.BucketPolicyConfigDto;
import com.example.hedadmin.response.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
//import software.amazon.awssdk.core.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

@Slf4j
@RestController
@Tag(name = "RustFSController", description = "RustFS对象存储管理")
@RequestMapping("/rustfs")
public class FsRustController {

    @Autowired
    private S3Client s3Client;

    @Value("${rustfs.bucketName}")
    private String BUCKET_NAME;

    @Value("${rustfs.endpoint}")
    private String ENDPOINT;

    @Operation(summary = "文件上传")
    @RequestMapping(value = "/upload", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    public CommonResult upload(@RequestPart("file") MultipartFile file) {
        // 判断Bucket是否存在
        if (!bucketExists(BUCKET_NAME)) {
            // 创建Bucket
            s3Client.createBucket(CreateBucketRequest.builder()
                    .bucket(BUCKET_NAME)
                    .build());
            log.info("Bucket created: {}", BUCKET_NAME);

            // 添加Bucket的访问策略
            String policy = JSONUtil.toJsonStr(createBucketPolicyConfigDto(BUCKET_NAME));
            log.info(policy);
            PutBucketPolicyRequest policyReq = PutBucketPolicyRequest.builder()
                    .bucket(BUCKET_NAME)
                    .policy(policy)
                    .build();
            s3Client.putBucketPolicy(policyReq);
        } else {
            log.info("Bucket already exists.");
        }

        /** 上传文件
         try {
         s3Client.putObject(PutObjectRequest.builder()
         .bucket(BUCKET_NAME)
         .key(file.getOriginalFilename())
         .contentType(file.getContentType())
         .build(),
         RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
         // 可自行封装上传结果返回
         return CommonResult.success("上传成功，文件名：" + file.getOriginalFilename());
         } catch (Exception e) {
         log.error("文件上传失败", e);
         return CommonResult.fail("文件上传失败：" + e.getMessage());
         }
         */
        return CommonResult.success("上传成功，文件名：" + file.getOriginalFilename());
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult delete(@RequestParam("objectName") String objectName) {
        // 删除对象
        s3Client.deleteObject(DeleteObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(objectName)
                .build());
        return CommonResult.success(null);
    }

    /**
     * 判断Bucket是否存在
     */
    private boolean bucketExists(String bucketName) {
        try {
            s3Client.headBucket(request -> request.bucket(bucketName));
            return true;
        } catch (NoSuchBucketException exception) {
            return false;
        }
    }

    /**
     * 创建存储桶的访问策略，设置为只读权限
     */
    private BucketPolicyConfigDto createBucketPolicyConfigDto(String bucketName) {
        BucketPolicyConfigDto.Statement statement = BucketPolicyConfigDto.Statement.builder()
                .Effect("Allow")
                .Principal(BucketPolicyConfigDto.Principal.builder().AWS(new String[]{"*"}).build())
                .Action(new String[]{"s3:GetObject"})
                .Resource(new String[]{"arn:aws:s3:::" + bucketName + "/*"})
                .build();

        return BucketPolicyConfigDto.builder()
                .Version("2012-10-17")
                .Statement(CollUtil.toList(statement))
                .build();
    }
}
