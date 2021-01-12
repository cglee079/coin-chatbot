package com.podo.coinchatbot.epclient.market;

import com.podo.coinchatbot.core.Coin;
import com.podo.coinchatbot.epclient.exception.UnknownParameterValueException;
import com.podo.coinchatbot.epclient.model.ApiCallResult;
import com.podo.coinchatbot.epclient.model.CoinResponse;
import com.podo.coinchatbot.epclient.util.ApiCaller;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public class GopaxApiClient extends MarketApiClient {
    public static final String URL = "https://api.gopax.co.kr/trading-pairs/stats";

    private ApiCallResult apiCallResult;
    private JSONArray response;

    @Override
    public CoinResponse getCoin(Coin coin) {
        if (!apiCallResult.isSuccess()) {
            return CoinResponse.error(apiCallResult.getErrorMessage());
        }

        String parameterValue = getParameterValue(coin);
        for (int i = 0; i < response.length(); i++) {
            JSONObject coinValue = response.getJSONObject(i);
            if (coinValue.getString("name").equals(parameterValue)) {
                double last = coinValue.getDouble("close");
                double first = coinValue.getDouble("open");
                double high = coinValue.getDouble("high");
                double low = coinValue.getDouble("low");
                double volume = coinValue.getDouble("volume");
                return CoinResponse.success()
                        .openPrice(first)
                        .lastPrice(last)
                        .highPrice(high)
                        .lowPrice(low)
                        .volume(volume)
                        .build();
            }
        }

        throw new UnknownParameterValueException(parameterValue);
    }

    @Override
    public void refresh() {
        apiCallResult = ApiCaller.callApi(URL);
        response = new JSONArray(apiCallResult.getResponseBody());
    }

}
