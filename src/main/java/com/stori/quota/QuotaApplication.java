package com.stori.quota;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@MapperScan("com.stori.quota")
@MapperScan("com.stori.quota.infras.dao.mapper")

public class QuotaApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuotaApplication.class, args);
    }

}
