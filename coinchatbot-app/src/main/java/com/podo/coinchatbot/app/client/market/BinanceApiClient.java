package com.podo.coinchatbot.app.client.market;

import com.podo.coinchatbot.core.Coin;
import com.podo.coinchatbot.core.Market;
import com.podo.coinchatbot.app.client.exception.UnknownParameterValueException;
import com.podo.coinchatbot.app.client.model.ApiCallResult;
import com.podo.coinchatbot.app.client.model.CoinResponse;
import com.podo.coinchatbot.app.client.ApiCaller;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class BinanceApiClient extends MarketApiClient {
    private static final String URL = "https://api.binance.com/api/v1/ticker/24hr";

    private ApiCallResult apiCallResult;
    private JSONArray response;

    @Override
    public Market getMarket() {
        return Market.BINANCE;
    }

    @Override
    public CoinResponse getCoin(Coin coin) {

        if (!apiCallResult.isSuccess()) {
            return CoinResponse.error(apiCallResult.getErrorMessage());
        }

        JSONObject coinObj = getCoinInResponse(getParameterValue(coin));

        BigDecimal quoteVolume = coinObj.getBigDecimal("quoteVolume");
        BigDecimal openPrice = coinObj.getBigDecimal("openPrice");
        BigDecimal lastPrice = coinObj.getBigDecimal("lastPrice");
        BigDecimal lowPrice = coinObj.getBigDecimal("lowPrice");
        BigDecimal highPrice = coinObj.getBigDecimal("highPrice");

        return CoinResponse.success()
                .volume(quoteVolume)
                .openPrice(openPrice)
                .lastPrice(lastPrice)
                .lowPrice(lowPrice)
                .highPrice(highPrice)
                .build();

    }

    private JSONObject getCoinInResponse(String param) {

        for (int i = 0; i < response.length(); i++) {
            JSONObject coinObj = response.getJSONObject(i);
            if (coinObj.getString("symbol").equals(param)) {
                return coinObj;
            }
        }

        throw new UnknownParameterValueException(param);
    }


    @Override
    public void refresh() {
        apiCallResult = ApiCaller.callApi(URL);
        response = new JSONArray(apiCallResult.getResponseBody());
    }

}
