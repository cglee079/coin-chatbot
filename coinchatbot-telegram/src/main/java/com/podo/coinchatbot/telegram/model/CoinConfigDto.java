package com.podo.coinchatbot.telegram.model;


import com.podo.coinchatbot.core.Coin;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class CoinConfigDto {
	private Coin coinId;
	private String exInvestKRW;
	private String exInvestUSD;
	private String exTargetKRW;
	private String exTargetUSD;
	private String exTargetRate;
	private String exCoinCnt;
	private Integer digitKRW;
	private Integer digitUSD;
	private Integer digitBTC;
	private String version;
}
