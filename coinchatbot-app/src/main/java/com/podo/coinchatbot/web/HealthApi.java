package com.podo.coinchatbot.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthApi {

    @GetMapping("/")
    public String home(){
        return "OK";
    }

}
