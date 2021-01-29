package com.podo.coinchatbot.app.client.market;

import com.podo.coinchatbot.app.client.ApiCaller;
import com.podo.coinchatbot.app.client.model.ApiCallResult;
import com.podo.coinchatbot.app.client.model.CoinResponse;
import com.podo.coinchatbot.core.Coin;
import com.podo.coinchatbot.core.Market;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class PoloniexApiClient extends MarketApiClient {

    public static final String URL = "https://poloniex.com/public?command=returnTicker";

    private ApiCallResult apiCallResult;
    private JSONObject response;

    @Override
    public Market getMarket() {
        return Market.POLONIEX;
    }

    @Override
    public CoinResponse getCoin(Coin coin) {
        if (!apiCallResult.isSuccess()) {
            return CoinResponse.error(apiCallResult.getErrorMessage());
        }

        String parameterValue = getParameterValue(coin);

        JSONObject coinObj = response.getJSONObject(parameterValue);
        BigDecimal last = coinObj.getBigDecimal("last");
        BigDecimal first = last.divide(coinObj.getBigDecimal("percentChange").add(BigDecimal.ONE), 10, RoundingMode.HALF_UP);
        BigDecimal high = coinObj.getBigDecimal("high24hr");
        BigDecimal low = coinObj.getBigDecimal("low24hr");
        BigDecimal volume = coinObj.getBigDecimal("quoteVolume");

        return CoinResponse.success()
                .openPrice(first)
                .lastPrice(last)
                .highPrice(high)
                .lowPrice(low)
                .volume(volume)
                .build();
    }

    @Override
    public void refresh() {
        apiCallResult = ApiCaller.callGetApi(URL);
        if (apiCallResult.isSuccess()) {
            response = new JSONObject(apiCallResult.getResponseBody());
        }
    }
}
