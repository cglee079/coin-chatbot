package com.podo.coinchatbot.telegram.keyboard;


import com.podo.coinchatbot.core.Lang;
import com.podo.coinchatbot.telegram.command.StopConfirmCommand;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class ConfirmStopKeyboard extends ReplyKeyboardMarkup {
	public ConfirmStopKeyboard(Lang lang) {
		super();

		this.setSelective(true);
		this.setResizeKeyboard(true);
		this.setOneTimeKeyboard(false);

		List<KeyboardRow> keyboard = new ArrayList<>();

		KeyboardRow keyboardFirstRow = new KeyboardRow();
		keyboardFirstRow.add(StopConfirmCommand.YES.getCmd(lang));
		keyboardFirstRow.add(StopConfirmCommand.NO.getCmd(lang));
		keyboard.add(keyboardFirstRow);

		this.setKeyboard(keyboard);
	}

}
