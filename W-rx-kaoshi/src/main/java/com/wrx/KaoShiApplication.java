package com.wrx;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.wrx.mapper")
public class KaoShiApplication {
    public static void main(String[] args) {
        SpringApplication.run(KaoShiApplication.class,args);
    }
}
