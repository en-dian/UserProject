package com.example.hedadmin;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.junit.jupiter.api.Test;
//import org.junit.Test;

//import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RustFSConnectionTest {

    @Test
    public void testConnectToRustFS() {
        String endpoint = "http://192.168.29.50:32045/";
        String accessKey = "admin";      // 用户名
        String secretKey = "123456";        // 密码

        BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);

        AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(
                        new AwsClientBuilder.EndpointConfiguration(endpoint, "us-east-1"))
                .withPathStyleAccessEnabled(true)   // 非 AWS 公有云通常需要路径风格
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();

        try {
            // 尝试列出存储桶，如果成功则说明连接和认证均通过
            s3Client.listBuckets();
            System.out.println("✅ 连接成功！RustFS 可正常访问。");
            assertTrue(true);
        } catch (Exception e) {
            System.err.println("❌ 连接失败：" + e.getMessage());
            assertTrue(false);
        }
    }
}