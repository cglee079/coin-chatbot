package com.podo.coinchatbot.app.external.market;

import com.podo.coinchatbot.app.external.ApiCaller;
import com.podo.coinchatbot.app.external.model.ApiCallResult;
import com.podo.coinchatbot.app.external.model.CoinResponse;
import com.podo.coinchatbot.core.Coin;
import com.podo.coinchatbot.core.Market;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public class UpbitApiClient extends MarketApiClient {

    private static final String URL = "https://api.upbit.com/v1/candles/days?count=1&market=";

    @Override
    public Market getMarket() {
        return Market.UPBIT;
    }

    @Override
    public CoinResponse getCoin(Coin coin) {
        ApiCallResult apiCallResult = ApiCaller.callGetApi(URL + getParameterValue(coin));

        if (!apiCallResult.isSuccess()) {
            return CoinResponse.error(apiCallResult.getErrorMessage());
        }

        JSONArray response = new JSONArray(apiCallResult.getResponseBody());
        JSONObject coinValue = response.getJSONObject(0);

        return CoinResponse.success()
                .openPrice(coinValue.getBigDecimal("opening_price"))
                .lastPrice(coinValue.getBigDecimal("trade_price"))
                .highPrice(coinValue.getBigDecimal("high_price"))
                .lowPrice(coinValue.getBigDecimal("low_price"))
                .volume(coinValue.getBigDecimal("candle_acc_trade_volume"))
                .build();
    }

    @Override
    public void refresh() {
        //NO REFRESH
    }
}
