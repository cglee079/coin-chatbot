package com.podo.coinchatbot.app.external.market;

import com.podo.coinchatbot.app.external.ApiCaller;
import com.podo.coinchatbot.app.external.exception.UnknownParameterValueException;
import com.podo.coinchatbot.app.external.model.ApiCallResult;
import com.podo.coinchatbot.app.external.model.CoinResponse;
import com.podo.coinchatbot.core.Coin;
import com.podo.coinchatbot.core.Market;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class GopaxApiClient extends MarketApiClient {
    public static final String URL = "https://api.gopax.co.kr/trading-pairs/stats";

    private ApiCallResult apiCallResult;
    private JSONArray response;

    @Override
    public Market getMarket() {
        return Market.GOPAX;
    }

    @Override
    public CoinResponse getCoin(Coin coin) {
        if (!apiCallResult.isSuccess()) {
            return CoinResponse.error(apiCallResult.getErrorMessage());
        }

        String parameterValue = getParameterValue(coin);
        for (int i = 0; i < response.length(); i++) {
            JSONObject coinValue = response.getJSONObject(i);
            if (coinValue.getString("name").equals(parameterValue)) {
                BigDecimal last = coinValue.getBigDecimal("close");
                BigDecimal first = coinValue.getBigDecimal("open");
                BigDecimal high = coinValue.getBigDecimal("high");
                BigDecimal low = coinValue.getBigDecimal("low");
                BigDecimal volume = coinValue.getBigDecimal("volume");
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
