package com.podo.coinchatbot.epclient;

import com.podo.coinchatbot.epclient.exception.ExchangeApiCallException;
import com.podo.coinchatbot.epclient.model.ApiCallResult;
import com.podo.coinchatbot.epclient.util.ApiCaller;
import org.json.JSONObject;

public class ExchangeApiClient {

    public static final String URL = "http://data.fixer.io/api/latest?access_key=f8e8de4219acbfc156083b7a039acf28";

    public double usdToKrw() {
        ApiCallResult apiCallResult = ApiCaller.callApi(URL);

        if (!apiCallResult.isSuccess()) {
            throw new ExchangeApiCallException(apiCallResult.getErrorMessage());
        }

        JSONObject responseObj = new JSONObject(apiCallResult.getResponseBody());
        JSONObject rateObj = responseObj.getJSONObject("rates");
        return rateObj.getDouble("KRW") / rateObj.getDouble("USD");
    }

}
