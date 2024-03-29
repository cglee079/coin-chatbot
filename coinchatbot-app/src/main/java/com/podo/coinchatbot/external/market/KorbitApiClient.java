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
public class KorbitApiClient extends MarketApiClient {

    public static final String URL = "https://api.korbit.co.kr/v1/ticker/detailed?currency_pair=";

    @Override
    public Market getMarket() {
        return Market.KORBIT;
    }

    @Override
    public CoinResponse getCoin(Coin coin) {
        String param = getParameterValue(coin);

        ApiCallResult apiCallResult = ApiCaller.callGetApi(URL + param);

        if (!apiCallResult.isSuccess()) {
            return CoinResponse.error(apiCallResult.getErrorMessage());
        }

        JSONObject response = new JSONObject(apiCallResult.getResponseBody());

        BigDecimal volume = response.getBigDecimal("volume");
        BigDecimal last = response.getBigDecimal("last");
        BigDecimal high = response.getBigDecimal("high");
        BigDecimal low = response.getBigDecimal("low");

        return CoinResponse.success()
                .lastPrice(last)
                .highPrice(high)
                .lowPrice(low)
                .volume(volume)
                .build();
    }

    @Override
    public void refresh() {
        // NO REFRESH
    }
}
