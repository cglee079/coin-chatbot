package com.podo.coinchatbot.telegram.keyboard;


import com.podo.coinchatbot.core.Lang;
import com.podo.coinchatbot.telegram.command.TimeloopAlertCommand;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class SetTimeloopAlertKeyboard extends ReplyKeyboardMarkup {
	public SetTimeloopAlertKeyboard(Lang lang) {
		super();
	    this.setSelective(true);
	    this.setResizeKeyboard(true);
	    this.setOneTimeKeyboard(false);

	    List<KeyboardRow> keyboard = new ArrayList<>();
	    KeyboardRow keyboardFirstRow = new KeyboardRow();
	    keyboardFirstRow.add(TimeloopAlertCommand.H1.getCmd(lang));
	    keyboardFirstRow.add(TimeloopAlertCommand.H2.getCmd(lang));
	    keyboardFirstRow.add(TimeloopAlertCommand.H3.getCmd(lang));
	    keyboardFirstRow.add(TimeloopAlertCommand.H4.getCmd(lang));

	    KeyboardRow keyboardSecondRow = new KeyboardRow();
	    keyboardSecondRow.add(TimeloopAlertCommand.H5.getCmd(lang));
	    keyboardSecondRow.add(TimeloopAlertCommand.H6.getCmd(lang));
	    keyboardSecondRow.add(TimeloopAlertCommand.H7.getCmd(lang));
	    keyboardSecondRow.add(TimeloopAlertCommand.H8.getCmd(lang));

	    KeyboardRow keyboardThirdRow = new KeyboardRow();
	    keyboardThirdRow.add(TimeloopAlertCommand.H9.getCmd(lang));
	    keyboardThirdRow.add(TimeloopAlertCommand.H10.getCmd(lang));
	    keyboardThirdRow.add(TimeloopAlertCommand.H11.getCmd(lang));
	    keyboardThirdRow.add(TimeloopAlertCommand.H12.getCmd(lang));

	    KeyboardRow keyboardForthRow = new KeyboardRow();
	    keyboardForthRow.add(TimeloopAlertCommand.OFF.getCmd(lang));
	    keyboardForthRow.add(TimeloopAlertCommand.OUT.getCmd(lang));

	    keyboard.add(keyboardFirstRow);
	    keyboard.add(keyboardSecondRow);
	    keyboard.add(keyboardThirdRow);
	    keyboard.add(keyboardForthRow);

	    this.setKeyboard(keyboard);
	}


}
