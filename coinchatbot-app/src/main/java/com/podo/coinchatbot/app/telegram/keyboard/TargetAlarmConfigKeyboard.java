package com.podo.coinchatbot.app.telegram.keyboard;


import com.podo.coinchatbot.core.Language;
import com.podo.coinchatbot.app.telegram.command.TargetAlarmConfigCommand;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class TargetAlarmConfigKeyboard extends ReplyKeyboardMarkup {
	public TargetAlarmConfigKeyboard(Language language) {
		super();

	    this.setSelective(true);
	    this.setResizeKeyboard(true);
	    this.setOneTimeKeyboard(false);

	    List<KeyboardRow> keyboard = new ArrayList<>();
	    KeyboardRow keyboardFirstRow = new KeyboardRow();
	    keyboardFirstRow.add(TargetAlarmConfigCommand.ADD.getCommand(language));
	    keyboardFirstRow.add(TargetAlarmConfigCommand.DEL.getCommand(language));

	    KeyboardRow keyboardSecondRow = new KeyboardRow();
	    keyboardSecondRow.add(TargetAlarmConfigCommand.OUT.getCommand(language));

	    keyboard.add(keyboardFirstRow);
	    keyboard.add(keyboardSecondRow);

	    this.setKeyboard(keyboard);
	}


}
