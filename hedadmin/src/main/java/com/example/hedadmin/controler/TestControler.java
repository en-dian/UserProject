package com.example.hedadmin.controler;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController


public class TestControler {
    @RequestMapping("/sayHello")
    public String sayHello(@RequestParam String name){
        return "Hello "+name;
    }
}
