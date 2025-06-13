package com.carocart.address;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


@EnableFeignClients

@SpringBootApplication
public class CaroCartAddressApplication {
    public static void main(String[] args) {
        SpringApplication.run(CaroCartAddressApplication.class, args);
    }
}
