package com.podo.coinchatbot.app.telegram.command;


import com.podo.coinchatbot.core.Language;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MainCommand implements CommandEnum {
	NULL("", ""),
	CURRENT_PRICE("현재가", "Current Price"),
	MARKETS_PRICE("거래소별 현재가", "Each market price"),
	COMPARED_TO_BTC("비트대비 변화량", "Change rate"),
	CALCULATE("손익금 계산", "Calculate"),
	INVESET_CONFIG("투자금액 설정", "Set Investment amount"),
	COINCOUNT_CONFIG("코인개수 설정", "Set The number of coins"),
	TARGET_ALARM_CONFIG("목표가 알림", "Set Target price"),
	MARKET_CONFIG("거래소 설정", "Set Market"),
	TIMELOOP_ALARM_CONFIG("시간알림 설정", "Set Daily notifications"),
	DAYLOOP_ALARM_CONFIG("일일알림 설정", "Set Hourly notifications"),
	SEND_MESSAGE("문의/건의", "Suggest"),
	HAPPY_LINE("행복회로", "Happy Price"),
	SHOW_SETTING("설정정보", "Show Setting"),
	STOP_ALL_ALARAM("모든알림중지", "Stop Notifications"),
	COIN_LIST("타 코인 알리미", "Other COIN"),
	HELP("Help", "Help"),
	SPONSORING("후원하기", "Sponsoring"),
	PREFERENCE("Preference", "Preference");

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

	public static MainCommand from(Language language, String str) {
		return CommandEnum.from(values(), language, str, MainCommand.NULL);
	}

}
