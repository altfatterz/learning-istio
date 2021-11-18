package com.example.springbootadministio;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAdminServer
public class SpringBootAdminIstioApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootAdminIstioApplication.class, args);
    }

}
