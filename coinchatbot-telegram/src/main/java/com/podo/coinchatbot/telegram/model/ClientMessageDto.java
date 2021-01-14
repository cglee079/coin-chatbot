package com.podo.coinchatbot.telegram.model;


import com.podo.coinchatbot.core.Coin;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ClientMessageDto {
	private int row;
	private int seq;
	private Coin coinId;
	private String userId;
	private String username;
	private String contents;
	private String date;
}
