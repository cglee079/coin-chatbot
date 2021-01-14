package com.podo.coinchatbot.telegram.model;


import com.podo.coinchatbot.core.Coin;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class CoinInfoDto {
	private Coin coinId;
	private String chatAddr;
	private boolean enabled;
}
