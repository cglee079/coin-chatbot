package com.podo.coinchatbot.telegram.keyboard;


import com.podo.coinchatbot.telegram.command.PrefLangCommand;
import com.podo.coinchatbot.core.Language;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class LanguageConfigKeyboard extends ReplyKeyboardMarkup {
    public LanguageConfigKeyboard(Language language) {
        super();

        this.setSelective(true);
        this.setResizeKeyboard(true);
        this.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add(PrefLangCommand.SET_KR.getCommand(language));
        keyboardFirstRow.add(PrefLangCommand.SET_EN.getCommand(language));

        KeyboardRow keyboardSecondRow = new KeyboardRow();
        keyboardSecondRow.add(PrefLangCommand.OUT.getCommand(language));

        keyboard.add(keyboardFirstRow);
        keyboard.add(keyboardSecondRow);

        this.setKeyboard(keyboard);
    }


}
