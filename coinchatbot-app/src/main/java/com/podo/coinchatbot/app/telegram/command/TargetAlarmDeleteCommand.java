package com.podo.coinchatbot.app.telegram.command;


import com.podo.coinchatbot.core.Language;
import com.podo.coinchatbot.app.model.TargetFocus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TargetAlarmDeleteCommand implements CommandEnum {
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

	public static TargetAlarmDeleteCommand from(Language language, String str) {
		return CommandEnum.from(values(), language, str, TargetAlarmDeleteCommand.NULL);
	}

}
