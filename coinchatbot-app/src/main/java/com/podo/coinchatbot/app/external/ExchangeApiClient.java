package com.podo.coinchatbot.app.external;

import com.podo.coinchatbot.app.external.exception.ExchangeApiCallException;
import com.podo.coinchatbot.app.external.model.ApiCallResult;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class ExchangeApiClient {

    public static final String URL = "http://data.fixer.io/api/latest?access_key=f8e8de4219acbfc156083b7a039acf28";

    public BigDecimal usdToKrw() {
        ApiCallResult apiCallResult = ApiCaller.callGetApi(URL);

        if (!apiCallResult.isSuccess()) {
            throw new ExchangeApiCallException(apiCallResult.getErrorMessage());
        }

        final JSONObject response = new JSONObject(apiCallResult.getResponseBody());
        final JSONObject responseData = response.getJSONObject("rates");

        return responseData.getBigDecimal("KRW").divide(responseData.getBigDecimal("USD"), 2, RoundingMode.HALF_UP);
    }

}
