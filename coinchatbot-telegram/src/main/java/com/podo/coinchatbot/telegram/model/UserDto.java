package com.podo.coinchatbot.telegram.model;

import com.podo.coinchatbot.core.Coin;
import com.podo.coinchatbot.core.Lang;
import com.podo.coinchatbot.core.Market;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class UserDto {
	private Long id;
	private Coin coin;

	private String telegramId;
	private String username;
	private Long localtime;
	private Lang lang;
	private Menu menuStatus;
	private Market market;
	private Integer timeloopAlert;
	private Integer dayloopAlert;
	private Double invest;
	private Double coinCount;
	private String openDate;
	private String reopenDate;
	private String closeDate;
	private String lastSendMessageAt;
	private Integer errorCount;
	private boolean enabled;
}
