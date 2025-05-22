package com.carocart.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class CaroCartServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(CaroCartServerApplication.class, args);
    }
}
