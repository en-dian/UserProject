package com.example.hedadmin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.hedadmin.mapper")

public class HedadminApplication {

    public static void main(String[] args) {
        SpringApplication.run(HedadminApplication.class, args);
    }

}
