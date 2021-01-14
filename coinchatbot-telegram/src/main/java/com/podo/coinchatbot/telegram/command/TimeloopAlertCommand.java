package com.podo.coinchatbot.telegram.command;


import com.podo.coinchatbot.core.Lang;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TimeloopAlertCommand implements CommandEnum {
	NULL(-1, "", ""),
	H1(1, "매 1시간", "1 Hour"),
	H2(2, "매 2시간", "2 Hour"),
	H3(3, "매 3시간", "3 Hour"),
	H4(4, "매 4시간", "4 Hour"),
	H5(5, "매 5시간", "5 Hour"),
	H6(6, "매 6시간", "6 Hour"),
	H7(7, "매 7시간", "7 Hour"),
	H8(8, "매 8시간", "8 Hour"),
	H9(9, "매 9시간", "9 Hour"),
	H10(10, "매 10시간", "10 Hour"),
	H11(11, "매 11시간", "11 Hour"),
	H12(12, "매 12시간", "12 Hour"),
	OFF(0, "끄기", "Stop"),
	OUT(-1, "나가기", "Out");

	private final Integer value;
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

	public static TimeloopAlertCommand from(Lang lang, String str) {
		return CommandEnum.from(values(), lang, str, TimeloopAlertCommand.NULL);
	}

}
