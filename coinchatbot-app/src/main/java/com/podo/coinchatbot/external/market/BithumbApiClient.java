package com.podo.coinchatbot.external.market;

import com.podo.coinchatbot.external.ApiCaller;
import com.podo.coinchatbot.external.model.ApiCallResult;
import com.podo.coinchatbot.external.model.CoinResponse;
import com.podo.coinchatbot.core.Coin;
import com.podo.coinchatbot.core.Market;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class BithumbApiClient extends MarketApiClient {

    private static final String URL = "https://api.bithumb.com/public/ticker/ALL";
    private ApiCallResult apiCallResult;
    private JSONObject response;

    @Override
    public Market getMarket() {
        return Market.BITHUMB;
    }

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
        BigDecimal volume = coinObj.getBigDecimal("units_traded_24H");
        BigDecimal openPrice = coinObj.getBigDecimal("opening_price");
        BigDecimal lastPrice = coinObj.getBigDecimal("closing_price");
        BigDecimal highPrice = coinObj.getBigDecimal("max_price");
        BigDecimal lowPrice = coinObj.getBigDecimal("min_price");

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
        apiCallResult = ApiCaller.callGetApi(URL);
        if (apiCallResult.isSuccess()) {
            response = new JSONObject(apiCallResult.getResponseBody());
        }
    }
}
