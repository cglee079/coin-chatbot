package com.podo.coinchatbot.telegram.keyboard;


import com.podo.coinchatbot.core.Lang;
import com.podo.coinchatbot.telegram.command.DayloopAlertCommand;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class SetDayloopAlertKeyboard extends ReplyKeyboardMarkup {
	public SetDayloopAlertKeyboard(Lang lang) {
		super();

	    this.setSelective(true);
	    this.setResizeKeyboard(true);
	    this.setOneTimeKeyboard(false);

	    List<KeyboardRow> keyboard = new ArrayList<>();
	    KeyboardRow keyboardFirstRow = new KeyboardRow();
	    keyboardFirstRow.add(DayloopAlertCommand.D1.getCmd(lang));
	    keyboardFirstRow.add(DayloopAlertCommand.D2.getCmd(lang));
	    keyboardFirstRow.add(DayloopAlertCommand.D3.getCmd(lang));

	    KeyboardRow keyboardSecondRow = new KeyboardRow();
	    keyboardSecondRow.add(DayloopAlertCommand.D4.getCmd(lang));
	    keyboardSecondRow.add(DayloopAlertCommand.D5.getCmd(lang));
	    keyboardSecondRow.add(DayloopAlertCommand.D6.getCmd(lang));

	    KeyboardRow keyboardThirdRow = new KeyboardRow();
	    keyboardThirdRow.add(DayloopAlertCommand.D7.getCmd(lang));
	    keyboardThirdRow.add(DayloopAlertCommand.OFF.getCmd(lang));
	    keyboardThirdRow.add(DayloopAlertCommand.OUT.getCmd(lang));

	    keyboard.add(keyboardFirstRow);
	    keyboard.add(keyboardSecondRow);
	    keyboard.add(keyboardThirdRow);

	    this.setKeyboard(keyboard);
	}


}
