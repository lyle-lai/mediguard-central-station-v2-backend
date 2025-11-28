package com.mediguard.central;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * MediGuard CMS 启动类
 */
@SpringBootApplication
@EnableScheduling
@MapperScan("com.mediguard.central.mapper")
public class MediGuardApplication {

    public static void main(String[] args) {
        SpringApplication.run(MediGuardApplication.class, args);
    }

}
