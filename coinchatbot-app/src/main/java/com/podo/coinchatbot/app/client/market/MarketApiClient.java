package com.podo.coinchatbot.app.client.market;

import com.podo.coinchatbot.core.Coin;
import com.podo.coinchatbot.core.Market;
import com.podo.coinchatbot.app.client.model.CoinResponse;

import java.util.HashMap;
import java.util.Map;

public abstract class MarketApiClient {

	private Map<Coin, String> coinToParameterValue;

	public void init(Map<Coin, String> coinToParameterValue) {
		this.initParameterValueOfCoin(coinToParameterValue);
		this.refresh();
	}

	private void initParameterValueOfCoin(Map<Coin, String> coinToParameterValue) {
		this.coinToParameterValue = coinToParameterValue;
	}

	protected String getParameterValue(Coin coin) {
		return coinToParameterValue.get(coin);
	}

	public Map<Coin, String> getCoinToParameterValue() {
		return new HashMap<>(coinToParameterValue);
	}

	public abstract CoinResponse getCoin(Coin coin);

	public abstract void refresh();

	public abstract Market getMarket();

}
