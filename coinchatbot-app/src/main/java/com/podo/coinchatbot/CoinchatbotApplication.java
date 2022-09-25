package com.podo.coinchatbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@ConfigurationPropertiesScan
@SpringBootApplication
public class CoinchatbotApplication {

    public static void main(String[] args) {
        SpringApplication.run(CoinchatbotApplication.class, args);
    }
}

