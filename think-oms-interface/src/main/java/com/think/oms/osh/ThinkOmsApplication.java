package com.think.oms.osh;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@ComponentScan(basePackages = "com.think.oms.*")
@EnableTransactionManagement
@MapperScan("com.think.oms.infrastructure.core.mybatis.mapper")
public class ThinkOmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ThinkOmsApplication.class, args);
    }
}