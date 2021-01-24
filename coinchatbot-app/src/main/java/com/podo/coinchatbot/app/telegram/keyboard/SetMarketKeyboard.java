package com.podo.coinchatbot.app.telegram.keyboard;


import com.podo.coinchatbot.core.Language;
import com.podo.coinchatbot.app.telegram.command.MarketCommand;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class SetMarketKeyboard extends ReplyKeyboardMarkup {
	public SetMarketKeyboard(List<MarketCommand> enabledMarketIds, Language language) {
		super();

		this.setSelective(true);
		this.setResizeKeyboard(true);
		this.setOneTimeKeyboard(false);

		List<KeyboardRow> keyboard = new ArrayList<>();

		KeyboardRow keyboardRow = null;
		int marketCnt = 0;

		for(int i = 0; i < enabledMarketIds.size(); i++) {
			if(marketCnt%2 == 0) { keyboardRow = new KeyboardRow();}
			marketCnt ++;

			keyboardRow.add(enabledMarketIds.get(i).getCommand(language));
			if(marketCnt%2 == 0) { keyboard.add(keyboardRow);}
		}


		if(marketCnt%2 == 0) { keyboardRow = new KeyboardRow(); }
		marketCnt ++;
		keyboardRow.add(MarketCommand.OUT.getCommand(language));
		keyboard.add(keyboardRow);

		this.setKeyboard(keyboard);
	}

}
