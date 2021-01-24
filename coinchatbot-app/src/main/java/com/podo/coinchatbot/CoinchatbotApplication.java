package com.podo.coinchatbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;

@ConfigurationPropertiesScan
@SpringBootApplication
public class CoinchatbotApplication {

    public static void main(String[] args) {
        ApiContextInitializer.init();
        SpringApplication.run(CoinchatbotApplication.class, args);
    }
}
