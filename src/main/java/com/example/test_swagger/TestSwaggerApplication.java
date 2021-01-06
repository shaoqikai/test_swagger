package com.example.test_swagger;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.test_swagger.mapper")
public class TestSwaggerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestSwaggerApplication.class, args);
    }

}
