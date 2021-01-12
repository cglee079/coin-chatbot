package com.podo.coinchatbot.epclient.util;

import com.podo.coinchatbot.epclient.model.ApiCallResult;
import lombok.experimental.UtilityClass;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;

@UtilityClass
public class ApiCaller {

    public static ApiCallResult callApi(String url){
        HttpClient httpClient = new HttpClient();
        GetMethod getMethod = new GetMethod(url);
        try {
            int responseCode = httpClient.executeMethod(getMethod);
            return new ApiCallResult(true, "", getMethod.getResponseBodyAsString());
        } catch (Exception e) {
            return new ApiCallResult(false, getMethod.getStatusText());
        }
    }

}
