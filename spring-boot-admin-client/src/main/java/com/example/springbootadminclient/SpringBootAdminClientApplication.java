package com.example.springbootadminclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class SpringBootAdminClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootAdminClientApplication.class, args);
    }

    @GetMapping("/")
    public String greet() {
        return "Hello World!";
    }
}
