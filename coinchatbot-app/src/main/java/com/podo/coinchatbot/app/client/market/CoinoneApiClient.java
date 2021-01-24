package com.podo.coinchatbot.app.client.market;

import com.podo.coinchatbot.core.Coin;
import com.podo.coinchatbot.core.Market;
import com.podo.coinchatbot.app.client.model.ApiCallResult;
import com.podo.coinchatbot.app.client.model.CoinResponse;
import com.podo.coinchatbot.app.client.ApiCaller;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public class CoinoneApiClient extends MarketApiClient {

    public static final String URL = "https://api.coinone.co.kr/ticker/?format=json&currency=";

    @Override
    public Market getMarket() {
        return Market.COINONE;
    }

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
                .volume(jsonObj.getBigDecimal("volume"))
                .openPrice(jsonObj.getBigDecimal("first"))
                .lastPrice(jsonObj.getBigDecimal("last"))
                .highPrice(jsonObj.getBigDecimal("high"))
                .lowPrice(jsonObj.getBigDecimal("low"))
                .build();
    }

    @Override
    public void refresh() {
        // NO refresh
    }

}
