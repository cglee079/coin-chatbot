package com.podo.coinchatbot.telegram.keyboard;


import com.podo.coinchatbot.telegram.command.StopAllAlarmConfirmCommand;
import com.podo.coinchatbot.core.Language;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class ConfirmStopKeyboard extends ReplyKeyboardMarkup {
    public ConfirmStopKeyboard(Language language) {
        super();

        this.setSelective(true);
        this.setResizeKeyboard(true);
        this.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add(StopAllAlarmConfirmCommand.YES.getCommand(language));
        keyboardFirstRow.add(StopAllAlarmConfirmCommand.NO.getCommand(language));
        keyboard.add(keyboardFirstRow);

        this.setKeyboard(keyboard);
    }

}
