package com.podo.coinchatbot.telegram.keyboard;


import com.podo.coinchatbot.telegram.command.PreferencCommand;
import com.podo.coinchatbot.core.Language;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class PreferenceKeyboard extends ReplyKeyboardMarkup {
    public PreferenceKeyboard(Language language) {
        super();

        this.setSelective(true);
        this.setResizeKeyboard(true);
        this.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add(PreferencCommand.LANGUAGE_CONFIG.getCommand(language));

        KeyboardRow keyboardSecondRow = new KeyboardRow();
        keyboardFirstRow.add(PreferencCommand.LOCALTIME_CONFIG.getCommand(language));

        KeyboardRow keyboardThirdRow = new KeyboardRow();
        keyboardThirdRow.add(PreferencCommand.OUT.getCommand(language));

        keyboard.add(keyboardFirstRow);
        keyboard.add(keyboardSecondRow);
        keyboard.add(keyboardThirdRow);

        this.setKeyboard(keyboard);
    }


}
