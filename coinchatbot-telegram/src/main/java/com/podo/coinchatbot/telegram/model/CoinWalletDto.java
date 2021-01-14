package com.podo.coinchatbot.telegram.model;

import com.podo.coinchatbot.core.Coin;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class CoinWalletDto {
	private Coin coinId;
	private String addr1;
	private String addr2;
	private boolean enabled;
}
