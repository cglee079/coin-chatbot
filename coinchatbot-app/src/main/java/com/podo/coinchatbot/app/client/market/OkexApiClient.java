package com.podo.coinchatbot.app.client.market;

import com.podo.coinchatbot.app.client.ApiCaller;
import com.podo.coinchatbot.app.client.exception.UnknownParameterValueException;
import com.podo.coinchatbot.app.client.model.ApiCallResult;
import com.podo.coinchatbot.app.client.model.CoinResponse;
import com.podo.coinchatbot.core.Coin;
import com.podo.coinchatbot.core.Market;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class OkexApiClient extends MarketApiClient {

    private static final String URL = "https://www.okex.com/api/spot/v3/instruments/ticker";

    private ApiCallResult apiCallResult;
    private JSONArray response;

    @Override
    public Market getMarket() {
        return Market.OKEX;
    }

    @Override
    public CoinResponse getCoin(Coin coin) {
        if (!apiCallResult.isSuccess()) {
            return CoinResponse.error(apiCallResult.getErrorMessage());
        }

        String parameterValue = this.getParameterValue(coin);

        for (int i = 0; i < response.length(); i++) {
            JSONObject coinObj = response.getJSONObject(i);
            if (coinObj.getString("product_id").equals(parameterValue)) {
                BigDecimal first = coinObj.getBigDecimal("open_24h");
                BigDecimal last = coinObj.getBigDecimal("last");
                BigDecimal high = coinObj.getBigDecimal("high_24h");
                BigDecimal low = coinObj.getBigDecimal("low_24h");
                BigDecimal volume = coinObj.getBigDecimal("quote_volume_24h");

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
        apiCallResult = ApiCaller.callGetApi(URL);
        if (apiCallResult.isSuccess()) {
            response = new JSONArray(apiCallResult.getResponseBody());
        }
    }
}
