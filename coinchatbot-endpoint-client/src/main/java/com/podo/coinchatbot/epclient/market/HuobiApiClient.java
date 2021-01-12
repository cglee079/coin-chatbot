package com.podo.coinchatbot.epclient.market;


import com.podo.coinchatbot.core.Coin;
import com.podo.coinchatbot.epclient.model.ApiCallResult;
import com.podo.coinchatbot.epclient.model.CoinResponse;
import com.podo.coinchatbot.epclient.util.ApiCaller;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public class HuobiApiClient extends MarketApiClient {

    public static final String URL = "https://api.huobi.pro/market/detail/merged?symbol=";

    @Override
    public CoinResponse getCoin(Coin coin) {
        String parameterValue = getParameterValue(coin);

        ApiCallResult apiCallResult = ApiCaller.callApi(URL + parameterValue);
        if (!apiCallResult.isSuccess()) {
            return CoinResponse.error(apiCallResult.getErrorMessage());
        }

        JSONObject response = new JSONObject(apiCallResult.getResponseBody());

        if (!response.getString("status").equals("ok")) {
            return CoinResponse.error("err-msg");
        }

        JSONObject coinValue = response.getJSONObject("tick");
        double volume = coinValue.getDouble("vol");
        double first = coinValue.getDouble("open");
        double last = coinValue.getDouble("close");
        double high = coinValue.getDouble("high");
        double low = coinValue.getDouble("low");

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
        //NO REFRESH
    }
}
