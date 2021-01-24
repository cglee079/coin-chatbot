package com.podo.coinchatbot.app.client.market;

import com.podo.coinchatbot.core.Coin;
import com.podo.coinchatbot.core.Market;
import com.podo.coinchatbot.app.client.model.ApiCallResult;
import com.podo.coinchatbot.app.client.model.CoinResponse;
import com.podo.coinchatbot.app.client.ApiCaller;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class HadaxApiClient extends MarketApiClient{
	private static final String URL = "https://api.hadax.com/market/detail/merged?symbol=";

	@Override
	public Market getMarket() {
		return Market.HADAX;
	}

	@Override
	public CoinResponse getCoin(Coin coin) {
		ApiCallResult apiCallResult = ApiCaller.callApi(URL + getParameterValue(coin));
		if (!apiCallResult.isSuccess()) {
			return CoinResponse.error(apiCallResult.getErrorMessage());
		}

		JSONObject response = new JSONObject(apiCallResult.getResponseBody());

		if (!response.getString("status").equals("ok")) {
			return CoinResponse.error("err-msg");
		}

		JSONObject coinValue = response.getJSONObject("tick");
		BigDecimal volume = coinValue.getBigDecimal("vol");
		BigDecimal first = coinValue.getBigDecimal("open");
		BigDecimal last = coinValue.getBigDecimal("close");
		BigDecimal high = coinValue.getBigDecimal("high");
		BigDecimal low = coinValue.getBigDecimal("low");

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
		//NO REFRESH
	}
}
