package com.example.hedadmin.config;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("黄恩典的SpringBoot3+Vue3脚手架")
                        .version("1.0")
                        .description("黄恩典SpringBoot3+Vue3脚手架接口文档"));

    }
}