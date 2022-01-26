package com.podo.coinchatbot.admin.dto;


import com.podo.coinchatbot.core.Coin;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSuggestDto {
	private Long id;
	private Long userId;
	private String suggestText;
	private LocalDateTime createAt;
}
