package com.podo.coinchatbot.telegram.command;


import com.podo.coinchatbot.core.Lang;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TargetSetCommand implements CommandEnum {
	NULL(-1, "", ""),
	ADD(1, "추가", "Add"),
	DEL(0, "삭제", "Delete"),
	OUT(-1, "나가기", "Out");

	private final int value;
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

	public static TargetSetCommand from(Lang lang, String str) {
		return CommandEnum.from(values(), lang, str, TargetSetCommand.NULL);
	}


}
