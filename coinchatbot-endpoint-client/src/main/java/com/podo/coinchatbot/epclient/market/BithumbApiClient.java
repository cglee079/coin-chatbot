package com.podo.coinchatbot.epclient.market;

import com.podo.coinchatbot.core.Coin;
import com.podo.coinchatbot.epclient.model.ApiCallResult;
import com.podo.coinchatbot.epclient.model.CoinResponse;
import com.podo.coinchatbot.epclient.util.ApiCaller;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public class BithumbApiClient extends MarketApiClient {

    private static final String URL = "https://api.bithumb.com/public/ticker/ALL";
    private ApiCallResult apiCallResult;
    private JSONObject response;

    @Override
    public CoinResponse getCoin(Coin coin) {
        if (!apiCallResult.isSuccess()) {
            return CoinResponse.error(apiCallResult.getErrorMessage());
        }

        String status = response.getString("status");

        if (!status.equals("0000")) {
            CoinResponse.error(response.getString("message"));
        }

        JSONObject responseData = response.getJSONObject("data");

        JSONObject coinObj = responseData.getJSONObject(getParameterValue(coin));
        double volume = coinObj.getDouble("units_traded_24H");
        double openPrice = coinObj.getDouble("opening_price");
        double lastPrice = coinObj.getDouble("closing_price");
        double highPrice = coinObj.getDouble("max_price");
        double lowPrice = coinObj.getDouble("min_price");

        return CoinResponse.success()
                .volume(volume)
                .openPrice(openPrice)
                .lastPrice(lastPrice)
                .highPrice(highPrice)
                .lowPrice(lowPrice)
                .build();
    }

    @Override
    public void refresh() {
        apiCallResult = ApiCaller.callApi(URL);
        response = new JSONObject(apiCallResult.getResponseBody());
    }
}
