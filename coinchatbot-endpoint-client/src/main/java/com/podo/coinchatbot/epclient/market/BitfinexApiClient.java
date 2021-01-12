package com.podo.coinchatbot.epclient.market;

import com.podo.coinchatbot.core.Coin;
import com.podo.coinchatbot.epclient.exception.UnknownParameterValueException;
import com.podo.coinchatbot.epclient.model.ApiCallResult;
import com.podo.coinchatbot.epclient.model.CoinResponse;
import com.podo.coinchatbot.epclient.util.ApiCaller;
import org.json.JSONArray;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

@Component
public class BitfinexApiClient extends MarketApiClient {
    private static final String URL = "https://api.bitfinex.com/v2/tickers?symbols=";

    private ApiCallResult apiCallResult;
    private JSONArray response;

    @Override
    public CoinResponse getCoin(Coin coin) {
        if (!apiCallResult.isSuccess()) {
            return CoinResponse.error(apiCallResult.getErrorMessage());
        }

        String parameterValue = this.getParameterValue(coin);
        for (int i = 0; i < response.length(); i++) {
            JSONArray coinArr = response.getJSONArray(i);
            if (parameterValue.equals(coinArr.getString(0))) {
                double openPrice = coinArr.getDouble(7) / (1 + coinArr.getDouble(6));
                double lastPrice = coinArr.getDouble(7);
                double volume = coinArr.getDouble(8);
                double highPrice = coinArr.getDouble(9);
                double lowPrice = coinArr.getDouble(10);

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
