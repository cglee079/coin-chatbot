package com.podo.coinchatbot.app.client.market;

import com.podo.coinchatbot.core.Coin;
import com.podo.coinchatbot.core.Market;
import com.podo.coinchatbot.app.client.exception.UnknownParameterValueException;
import com.podo.coinchatbot.app.client.model.ApiCallResult;
import com.podo.coinchatbot.app.client.model.CoinResponse;
import com.podo.coinchatbot.app.client.ApiCaller;
import org.json.JSONArray;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.Objects;

@Component
public class BitfinexApiClient extends MarketApiClient {
    private static final String URL = "https://api.bitfinex.com/v2/tickers?symbols=";

    private ApiCallResult apiCallResult;
    private JSONArray response;

    @Override
    public Market getMarket() {
        return Market.BITFINEX;
    }

    @Override
    public CoinResponse getCoin(Coin coin) {
        if (!apiCallResult.isSuccess()) {
            return CoinResponse.error(apiCallResult.getErrorMessage());
        }

        String parameterValue = this.getParameterValue(coin);
        for (int i = 0; i < response.length(); i++) {
            JSONArray coinArr = response.getJSONArray(i);
            if (parameterValue.equals(coinArr.getString(0))) {
                BigDecimal openPrice = coinArr.getBigDecimal(7).divide(coinArr.getBigDecimal(6).add(BigDecimal.ONE), 10, RoundingMode.HALF_UP);
                BigDecimal lastPrice = coinArr.getBigDecimal(7);
                BigDecimal volume = coinArr.getBigDecimal(8);
                BigDecimal highPrice = coinArr.getBigDecimal(9);
                BigDecimal lowPrice = coinArr.getBigDecimal(10);

                return CoinResponse.success()
                        .highPrice(highPrice)
                        .lowPrice(lowPrice)
                        .lastPrice(lastPrice)
                        .openPrice(openPrice)
                        .volume(volume)
                        .build();
            }
        }

        throw new UnknownParameterValueException(parameterValue);
    }


    @Override
    public void refresh() {

        Map<Coin, String> coinToParameterValue = getCoinToParameterValue();

        if (Objects.isNull(coinToParameterValue)) {
            return;
        }

        String urlParam = String.join(",", coinToParameterValue.values());

        apiCallResult = ApiCaller.callApi(URL + urlParam);
        response = new JSONArray(apiCallResult.getResponseBody());
    }

}
