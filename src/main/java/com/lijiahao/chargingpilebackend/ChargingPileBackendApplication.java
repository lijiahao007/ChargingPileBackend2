package com.lijiahao.chargingpilebackend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.oas.annotations.EnableOpenApi;

@SpringBootApplication
@EnableOpenApi
@MapperScan("com.lijiahao.chargingpilebackend.mapper")
@EnableTransactionManagement
public class ChargingPileBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChargingPileBackendApplication.class, args);
    }

}
