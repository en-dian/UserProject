package com.example.hedadmin;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

//import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RustFSHttpTest {

    @org.junit.jupiter.api.Test
    public void testBasicAuthConnection() {
        String url = "http://192.168.29.50:32045";  // 可根据实际管理端点调整
        String username = "admin";
        String password = "123456";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(username, password);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    url, HttpMethod.GET, entity, String.class);
            int status = response.getStatusCodeValue();
            // 只要不是 401（未授权）或 403（禁止），就认为连接认证通过
            if (status != 401 && status != 403) {
                System.out.println("✅ 连接成功，HTTP状态码：" + status);
                assertTrue(true);
            } else {
                System.err.println("❌ 认证失败，HTTP状态码：" + status);
                assertTrue(false);
            }
        } catch (Exception e) {
            System.err.println("❌ 连接异常：" + e.getMessage());
            assertTrue(false);
        }
    }
}
