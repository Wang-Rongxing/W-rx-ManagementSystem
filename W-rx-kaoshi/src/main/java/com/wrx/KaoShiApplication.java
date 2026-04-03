package com.wrx;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.wrx.mapper")
@EnableKnife4j
public class KaoShiApplication {
    public static void main(String[] args) {
        SpringApplication.run(KaoShiApplication.class,args);
    }
}
