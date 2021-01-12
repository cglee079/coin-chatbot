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
public class OkexApiClient extends MarketApiClient {

    private static final String URL = "https://www.okex.com/api/spot/v3/instruments/ticker";

    private ApiCallResult apiCallResult;
    private JSONArray response;

    @Override
    public CoinResponse getCoin(Coin coin) {
        if (!apiCallResult.isSuccess()) {
            return CoinResponse.error(apiCallResult.getErrorMessage());
        }

        String parameterValue = this.getParameterValue(coin);

        for (int i = 0; i < response.length(); i++) {
            JSONObject coinObj = response.getJSONObject(i);
            if (coinObj.getString("product_id").equals(parameterValue)) {
                double first = coinObj.getDouble("open_24h");
                double last = coinObj.getDouble("last");
                double high = coinObj.getDouble("high_24h");
                double low = coinObj.getDouble("low_24h");
                double volume = coinObj.getDouble("quote_volume_24h");

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
