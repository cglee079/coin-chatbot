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
public class BittrexApiClient extends MarketApiClient {
    private static final String URL = "https://bittrex.com/api/v1.1/public/getmarketsummaries";

    private ApiCallResult apiCallResult;
    private JSONObject response;

    @Override
    public Market getMarket() {
        return Market.BITTREX;
    }

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
                BigDecimal last = coinValue.getBigDecimal("Last");
                BigDecimal high = coinValue.getBigDecimal("High");
                BigDecimal low = coinValue.getBigDecimal("Low");
                BigDecimal volume = coinValue.getBigDecimal("Volume");

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
        apiCallResult = ApiCaller.callGetApi(URL);
        if (apiCallResult.isSuccess()) {
            response = new JSONObject(apiCallResult.getResponseBody());
        }
    }
}
