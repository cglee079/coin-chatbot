package com.podo.coinchatbot.app.domain.dto;


import com.podo.coinchatbot.app.domain.model.UserTargetAlarm;
import com.podo.coinchatbot.app.model.TargetFocus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@Builder
public class UserTargetAlarmDto {
	private Long id;
	private Long userId;
	private BigDecimal targetPrice;
	private TargetFocus targetFocus;

	public UserTargetAlarmDto(UserTargetAlarm userTargetAlarm){
		this.id = userTargetAlarm.getId();
		this.userId = userTargetAlarm.getUserId();
		this.targetPrice = userTargetAlarm.getTargetPrice();
		this.targetFocus = userTargetAlarm.getTargetFocus();
	}
}
