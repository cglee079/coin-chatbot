package com.podo.coinchatbot.app.client;

import com.podo.coinchatbot.app.client.model.ApiCallResult;
import lombok.experimental.UtilityClass;
import org.springframework.web.client.RestTemplate;

@UtilityClass
public class ApiCaller {

    public static ApiCallResult callApi(String url){
        RestTemplate restTemplate = new RestTemplate();

        try {
            String responseBody = restTemplate.getForObject(url, String.class);
            return new ApiCallResult(true, "", responseBody);
        } catch (Exception e) {
            return new ApiCallResult(false, e.getMessage());
        }
    }

}
