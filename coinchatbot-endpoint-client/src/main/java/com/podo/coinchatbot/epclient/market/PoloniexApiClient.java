package com.podo.coinchatbot.epclient.market;

import com.podo.coinchatbot.core.Coin;
import com.podo.coinchatbot.epclient.model.ApiCallResult;
import com.podo.coinchatbot.epclient.model.CoinResponse;
import com.podo.coinchatbot.epclient.util.ApiCaller;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public class PoloniexApiClient extends MarketApiClient {

    public static final String URL = "https://poloniex.com/public?command=returnTicker";

    private ApiCallResult apiCallResult;
    private JSONObject response;

    @Override
    public CoinResponse getCoin(Coin coin) {
        if (!apiCallResult.isSuccess()) {
            return CoinResponse.error(apiCallResult.getErrorMessage());
        }

        String parameterValue = getParameterValue(coin);

        JSONObject coinObj = response.getJSONObject(parameterValue);
        double last = coinObj.getDouble("last");
        double first = last / (1 + coinObj.getDouble("percentChange"));
        double high = coinObj.getDouble("high24hr");
        double low = coinObj.getDouble("low24hr");
        double volume = coinObj.getDouble("quoteVolume");

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
        apiCallResult = ApiCaller.callApi(URL);
        response = new JSONObject(apiCallResult.getResponseBody());
    }
}
