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
public class BittrexApiClient extends MarketApiClient {
    private static final String URL = "https://bittrex.com/api/v1.1/public/getmarketsummaries";

    private ApiCallResult apiCallResult;
    private JSONObject response;

    public CoinResponse getCoin(Coin coin) {
        if (!apiCallResult.isSuccess()) {
            return CoinResponse.error(apiCallResult.getErrorMessage());
        }

        String parameterValue = this.getParameterValue(coin);

        if (!response.getBoolean("success")) {
            return CoinResponse.error(response.getString("message"));
        }

        JSONArray responseData = response.getJSONArray("result");
        for (int i = 0; i < responseData.length(); i++) {
            JSONObject coinValue = responseData.getJSONObject(i);
            if (coinValue.getString("MarketName").equals(parameterValue)) {
                double last = coinValue.getDouble("Last");
                double high = coinValue.getDouble("High");
                double low = coinValue.getDouble("Low");
                double volume = coinValue.getDouble("Volume");

                return CoinResponse.success()
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
        response = new JSONObject(apiCallResult.getResponseBody());
    }
}
