package com.podo.coinchatbot.app.telegram.command;


import com.podo.coinchatbot.core.Language;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PrefLangCommand implements CommandEnum {
	NULL(null, "", ""),
	SET_KR(Language.KR, "Korea", "Korea"),
	SET_EN(Language.EN, "English", "English"),
	OUT(null, "OUT", "OUT");

	private final Language value;
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

	public static PrefLangCommand from(Language language, String str) {
		return CommandEnum.from(values(), language, str, PrefLangCommand.NULL);
	}

}
