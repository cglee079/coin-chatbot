package com.podo.coinchatbot.telegram.command;


import com.podo.coinchatbot.core.Lang;
import com.podo.coinchatbot.telegram.model.TargetFocus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TargetDelCommand implements CommandEnum {
	NULL(null, "", ""),
	OUT(null, "나가기", "Out");

	private final TargetFocus value;
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

	public static TargetDelCommand from(Lang lang, String str) {
		return CommandEnum.from(values(), lang, str, TargetDelCommand.NULL);
	}

}
