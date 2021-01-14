package com.podo.coinchatbot.telegram.command;


import com.podo.coinchatbot.core.Lang;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SendMessageCommand implements CommandEnum {
	NULL("", ""),
	OUT("0", "0");

	private final String kr;
	private final String en;

	@Override
	public String kr() {
		return kr;
	}

	@Override
	public String en() {
		return en;
	}

	public static SendMessageCommand from(Lang lang, String str) {
		return CommandEnum.from(values(), lang, str, SendMessageCommand.NULL);
	}

}
