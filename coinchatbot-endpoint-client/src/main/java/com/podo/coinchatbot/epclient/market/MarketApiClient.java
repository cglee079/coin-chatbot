package com.podo.coinchatbot.epclient.market;

import com.podo.coinchatbot.core.Coin;
import com.podo.coinchatbot.epclient.model.CoinResponse;

import java.util.HashMap;
import java.util.Map;

public abstract class MarketApiClient {

	private Map<Coin, String> coinToParameterValue;

	private void init(Map<Coin, String> coinToParameterValue) {
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

	protected abstract CoinResponse getCoin(Coin coin);
	public abstract void refresh();

}
