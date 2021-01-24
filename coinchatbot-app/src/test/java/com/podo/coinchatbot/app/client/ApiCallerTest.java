package com.podo.coinchatbot.app.client;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ApiCallerTest {

    @Test
    public void dd(){
        System.out.println(ApiCaller.getQueryString("http://podo-dev.com?abc=123&123=123"));
    }

}
