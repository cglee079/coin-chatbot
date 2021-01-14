package com.podo.coinchatbot.telegram.model;


import com.podo.coinchatbot.core.Coin;
import com.podo.coinchatbot.core.Market;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class TimelyInfoDto {
	private Coin coinId;
	private String date;
	private Market marketId;
	private Double high;
	private Double low;
	private Double last;
	private long volume;
	private String result;
	private String errorCode;
	private String errorMsg;
}
