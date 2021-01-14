package com.podo.coinchatbot.telegram.command;


import com.podo.coinchatbot.core.Lang;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PrefLangCommand implements CommandEnum {
	NULL(null, "", ""),
	SET_KR(Lang.KR, "Korea", "Korea"),
	SET_US(Lang.EN, "English", "English"),
	OUT(null, "OUT", "OUT");

	private final Lang id;
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

	public static PrefLangCommand from(Lang lang, String str) {
		return CommandEnum.from(values(), lang, str, PrefLangCommand.NULL);
	}

}
