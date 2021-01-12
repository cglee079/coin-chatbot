package com.podo.coinchatbot.epclient.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiCallResult {

    public Boolean result;
    public String errorMessage;
    public String responseBody;

    public ApiCallResult(Boolean result, String errorMessage) {
        this.result = result;
        this.errorMessage = errorMessage;
    }

    public Boolean isSuccess(){
        return result;
    }

}
