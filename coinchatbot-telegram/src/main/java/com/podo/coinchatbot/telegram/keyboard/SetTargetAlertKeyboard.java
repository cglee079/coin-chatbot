package com.podo.coinchatbot.telegram.keyboard;


import com.podo.coinchatbot.core.Lang;
import com.podo.coinchatbot.telegram.command.TargetSetCommand;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class SetTargetAlertKeyboard extends ReplyKeyboardMarkup {
	public SetTargetAlertKeyboard(Lang lang) {
		super();

	    this.setSelective(true);
	    this.setResizeKeyboard(true);
	    this.setOneTimeKeyboard(false);

	    List<KeyboardRow> keyboard = new ArrayList<>();
	    KeyboardRow keyboardFirstRow = new KeyboardRow();
	    keyboardFirstRow.add(TargetSetCommand.ADD.getCmd(lang));
	    keyboardFirstRow.add(TargetSetCommand.DEL.getCmd(lang));

	    KeyboardRow keyboardSecondRow = new KeyboardRow();
	    keyboardSecondRow.add(TargetSetCommand.OUT.getCmd(lang));

	    keyboard.add(keyboardFirstRow);
	    keyboard.add(keyboardSecondRow);

	    this.setKeyboard(keyboard);
	}


}
