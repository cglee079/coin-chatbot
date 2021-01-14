package com.podo.coinchatbot.telegram.keyboard;


import com.podo.coinchatbot.core.Lang;
import com.podo.coinchatbot.telegram.command.MainCommand;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class MainEnKeyboard extends ReplyKeyboardMarkup {
	public MainEnKeyboard(Lang lang) {
		super();

	    this.setSelective(true);
	    this.setResizeKeyboard(true);
	    this.setOneTimeKeyboard(false);

	    List<KeyboardRow> keyboard = new ArrayList<>();
	    KeyboardRow keyboardFirstRow = new KeyboardRow();
	    keyboardFirstRow.add(MainCommand.CURRENT_PRICE.getCmd(lang));
	    keyboardFirstRow.add(MainCommand.MARKETS_PRICE.getCmd(lang));
	    keyboardFirstRow.add(MainCommand.COMPARED_TO_BTC.getCmd(lang));
	    keyboardFirstRow.add(MainCommand.CALCULATE.getCmd(lang));

	    KeyboardRow keyboardSecondRow = new KeyboardRow();
	    keyboardSecondRow.add(MainCommand.SET_INVEST.getCmd(lang));
	    keyboardSecondRow.add(MainCommand.SET_COINCNT.getCmd(lang));
	    keyboardSecondRow.add(MainCommand.SET_MARKET.getCmd(lang));

	    KeyboardRow keyboardThirdRow = new KeyboardRow();
	    keyboardSecondRow.add(MainCommand.SET_TARGET.getCmd(lang));
	    keyboardThirdRow.add(MainCommand.SET_DAYLOOP.getCmd(lang));
	    keyboardThirdRow.add(MainCommand.SET_TIMELOOP.getCmd(lang));

	    KeyboardRow keyboardForthRow = new KeyboardRow();
	    keyboardThirdRow.add(MainCommand.HAPPY_LINE.getCmd(lang));
	    keyboardThirdRow.add(MainCommand.SHOW_SETTING.getCmd(lang));
	    keyboardThirdRow.add(MainCommand.STOP_ALERTS.getCmd(lang));

	    KeyboardRow keyboardFifthRow = new KeyboardRow();
	    keyboardForthRow.add(MainCommand.COIN_LIST.getCmd(lang));
	    keyboardForthRow.add(MainCommand.SEND_MESSAGE.getCmd(lang));
	    keyboardForthRow.add(MainCommand.SPONSORING.getCmd(lang));
	    keyboardForthRow.add(MainCommand.HELP.getCmd(lang));
	    keyboardForthRow.add(MainCommand.PREFERENCE.getCmd(lang));

	    keyboard.add(keyboardFirstRow);
	    keyboard.add(keyboardSecondRow);
	    keyboard.add(keyboardThirdRow);
	    keyboard.add(keyboardForthRow);
	    keyboard.add(keyboardFifthRow);
	    this.setKeyboard(keyboard);
	}


}
