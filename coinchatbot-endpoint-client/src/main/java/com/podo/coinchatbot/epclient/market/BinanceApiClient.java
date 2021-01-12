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
public class BinanceApiClient extends MarketApiClient {
    private static final String URL = "https://api.binance.com/api/v1/ticker/24hr";

    private ApiCallResult apiCallResult;
    private JSONArray response;

    @Override
    public CoinResponse getCoin(Coin coin) {

        if (!apiCallResult.isSuccess()) {
            return CoinResponse.error(apiCallResult.getErrorMessage());
        }

        JSONObject coinObj = getCoinInResponse(getParameterValue(coin));

        double quoteVolume = coinObj.getDouble("quoteVolume");
        double openPrice = coinObj.getDouble("openPrice");
        double lastPrice = coinObj.getDouble("lastPrice");
        double lowPrice = coinObj.getDouble("lowPrice");
        double highPrice = coinObj.getDouble("highPrice");

        return CoinResponse.success()
                .volume(quoteVolume)
                .openPrice(openPrice)
                .lastPrice(lastPrice)
                .lowPrice(lowPrice)
                .highPrice(highPrice)
                .build();

    }

    private JSONObject getCoinInResponse(String param) {

        for (int i = 0; i < response.length(); i++) {
            JSONObject coinObj = response.getJSONObject(i);
            if (coinObj.getString("symbol").equals(param)) {
                return coinObj;
            }
        }

        throw new UnknownParameterValueException(param);
    }


    @Override
    public void refresh() {
        apiCallResult = ApiCaller.callApi(URL);
        response = new JSONArray(apiCallResult.getResponseBody());
    }
}
