package com.podo.coinchatbot.epclient.market;

import com.podo.coinchatbot.core.Coin;
import com.podo.coinchatbot.epclient.model.ApiCallResult;
import com.podo.coinchatbot.epclient.model.CoinResponse;
import com.podo.coinchatbot.epclient.util.ApiCaller;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public class KorbitApiClient extends MarketApiClient {

    public static final String URL = "https://api.korbit.co.kr/v1/ticker/detailed?currency_pair=";

    @Override
    public CoinResponse getCoin(Coin coin) {
        String param = getParameterValue(coin);

        ApiCallResult apiCallResult = ApiCaller.callApi(URL + param);

        if (!apiCallResult.isSuccess()) {
            return CoinResponse.error(apiCallResult.getErrorMessage());
        }

        JSONObject response = new JSONObject(apiCallResult.getResponseBody());

        double volume = response.getDouble("volume");
        double last = response.getDouble("last");
        double high = response.getDouble("high");
        double low = response.getDouble("low");

        return CoinResponse.success()
                .lastPrice(last)
                .highPrice(high)
                .lowPrice(low)
                .volume(volume)
                .build();
    }

    @Override
    public void refresh() {
        // NO REFRESH
    }
}
