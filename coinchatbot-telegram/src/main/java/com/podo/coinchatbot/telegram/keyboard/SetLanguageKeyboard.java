package com.podo.coinchatbot.telegram.keyboard;


import com.podo.coinchatbot.core.Lang;
import com.podo.coinchatbot.telegram.command.PrefLangCommand;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class SetLanguageKeyboard extends ReplyKeyboardMarkup {
	public SetLanguageKeyboard(Lang lang) {
		super();

	    this.setSelective(true);
	    this.setResizeKeyboard(true);
	    this.setOneTimeKeyboard(false);

	    List<KeyboardRow> keyboard = new ArrayList<>();
	    KeyboardRow keyboardFirstRow = new KeyboardRow();
	    keyboardFirstRow.add(PrefLangCommand.SET_KR.getCmd(lang));
	    keyboardFirstRow.add(PrefLangCommand.SET_US.getCmd(lang));

	    KeyboardRow keyboardSecondRow = new KeyboardRow();
	    keyboardSecondRow.add(PrefLangCommand.OUT.getCmd(lang));

	    keyboard.add(keyboardFirstRow);
	    keyboard.add(keyboardSecondRow);

	    this.setKeyboard(keyboard);
	}


}
