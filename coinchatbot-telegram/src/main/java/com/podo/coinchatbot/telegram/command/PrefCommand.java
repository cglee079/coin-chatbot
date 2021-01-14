package com.podo.coinchatbot.telegram.command;


import com.podo.coinchatbot.core.Lang;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PrefCommand implements CommandEnum {
	NULL("", ""),
	SET_LANG("Language", "Language"),
	TIME_ADJUST("Time adjust(시차조절)", "Time adjust"),
	OUT("OUT", "OUT");

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

	public static PrefCommand from(Lang lang, String str) {
		return CommandEnum.from(values(), lang, str, PrefCommand.NULL);
	}

}
