package com.podo.coinchatbot.core;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Market{
	COINONE(Money.KRW),
	BITHUMB(Money.KRW),
	UPBIT(Money.KRW),
	COINNEST(Money.KRW),
	KORBIT(Money.KRW),
	GOPAX(Money.KRW),
	CASHIEREST(Money.KRW),

	BITFINEX(Money.USD),
	BITTREX(Money.USD),
	POLONIEX(Money.USD),
	BINANCE(Money.USD),
	HUOBI(Money.USD),
	HADAX(Money.USD),
	OKEX(Money.USD);

	private final Money money;

	public boolean isKRW() {
		return this.money == Money.KRW;
	}

	public boolean isUSD() {
		return this.money == Money.USD;
	}

	private enum Money{
		KRW, USD
	}
}
