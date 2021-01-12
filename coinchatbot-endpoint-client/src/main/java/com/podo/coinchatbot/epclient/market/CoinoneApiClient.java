package com.podo.coinchatbot.epclient.market;

import com.podo.coinchatbot.core.Coin;
import com.podo.coinchatbot.epclient.model.ApiCallResult;
import com.podo.coinchatbot.epclient.model.CoinResponse;
import com.podo.coinchatbot.epclient.util.ApiCaller;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public class CoinoneApiClient extends MarketApiClient {

    public static final String URL = "https://api.coinone.co.kr/ticker/?format=json&currency=";

    @Override
    public CoinResponse getCoin(Coin coin) {
        String parameterValue = getParameterValue(coin);

        ApiCallResult apiCallResult = ApiCaller.callApi(URL + parameterValue);
        if (!apiCallResult.isSuccess()) {
            return CoinResponse.error(apiCallResult.getErrorMessage());
        }

        JSONObject jsonObj = new JSONObject(apiCallResult.getResponseBody());
        if (!jsonObj.getString("result").equals("success")) {
            return CoinResponse.error("Coinone ErrorCode : " + jsonObj.getInt("errorCode"));
        }

        return CoinResponse.success()
                .volume(jsonObj.getDouble("volume"))
                .openPrice(jsonObj.getDouble("first"))
                .lastPrice(jsonObj.getDouble("last"))
                .highPrice(jsonObj.getDouble("high"))
                .lowPrice(jsonObj.getDouble("low"))
                .build();
    }

    @Override
    public void refresh() {
        // NO refresh
    }

}
