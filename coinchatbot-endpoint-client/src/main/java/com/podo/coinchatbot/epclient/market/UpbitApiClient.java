package com.podo.coinchatbot.epclient.market;

import com.podo.coinchatbot.core.Coin;
import com.podo.coinchatbot.epclient.model.ApiCallResult;
import com.podo.coinchatbot.epclient.model.CoinResponse;
import com.podo.coinchatbot.epclient.util.ApiCaller;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public class UpbitApiClient extends MarketApiClient {

    private static final String URL = "https://api.upbit.com/v1/candles/days?count=1&market=";

    @Override
    protected CoinResponse getCoin(Coin coin) {
        ApiCallResult apiCallResult = ApiCaller.callApi(URL + getParameterValue(coin));

        if (!apiCallResult.isSuccess()) {
            return CoinResponse.error(apiCallResult.getErrorMessage());
        }

        JSONArray response = new JSONArray(apiCallResult.getResponseBody());
        JSONObject coinValue = response.getJSONObject(0);

        return CoinResponse.success()
                .openPrice(coinValue.getDouble("opening_price"))
                .lastPrice(coinValue.getDouble("trade_price"))
                .highPrice(coinValue.getDouble("high_price"))
                .lowPrice(coinValue.getDouble("low_price"))
                .volume(coinValue.getDouble("candle_acc_trade_volume"))
                .build();
    }

    @Override
    public void refresh() {
        //NO REFRESH
    }
}
