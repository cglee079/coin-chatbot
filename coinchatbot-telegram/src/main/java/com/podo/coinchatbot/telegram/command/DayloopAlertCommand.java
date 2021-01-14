package com.podo.coinchatbot.telegram.command;


import com.podo.coinchatbot.core.Lang;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public enum DayloopAlertCommand implements CommandEnum {
	NULL(-1, "", ""),
	D1(1, "매 1일", "1 Day"),
	D2(2, "매 2일", "2 Day"),
	D3(3, "매 3일", "3 Day"),
	D4(4, "매 4일", "4 Day"),
	D5(5, "매 5일", "5 Day"),
	D6(6, "매 6일", "6 Day"),
	D7(7, "매 7일", "7 Day"),
	OFF(0, "끄기", "Stop"),
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

	public static DayloopAlertCommand from(Lang lang, String str) {
		return CommandEnum.from(values(), lang, str, DayloopAlertCommand.NULL);
	}
}
