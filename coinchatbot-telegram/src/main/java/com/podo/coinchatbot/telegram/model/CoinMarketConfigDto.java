package com.podo.coinchatbot.telegram.model;

import com.podo.coinchatbot.core.Coin;
import com.podo.coinchatbot.core.Market;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class CoinMarketConfigDto {
	private Coin coinId;
	private Market marketId;
	private boolean inBtc;
	private String param;
	private boolean enabled;
}
