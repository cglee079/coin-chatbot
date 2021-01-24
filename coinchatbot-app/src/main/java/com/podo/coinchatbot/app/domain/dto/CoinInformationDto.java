package com.podo.coinchatbot.app.domain.dto;


import com.podo.coinchatbot.core.Coin;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CoinInformationDto {
	private Coin coin;
	private String chatUrl;
}
