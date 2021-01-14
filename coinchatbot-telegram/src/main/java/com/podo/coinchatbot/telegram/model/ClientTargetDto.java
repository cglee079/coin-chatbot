package com.podo.coinchatbot.telegram.model;


import com.podo.coinchatbot.core.Coin;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ClientTargetDto {
	private Coin coinId;
	private String userId;
	private Double price;
	private TargetFocus focus;
	private String createDate;
}
