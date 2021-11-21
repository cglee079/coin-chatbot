package com.podo.coinchatbot.telegram.keyboard;


import com.podo.coinchatbot.telegram.command.DayloopAlarmCommand;
import com.podo.coinchatbot.core.Language;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class SetDayloopAlertKeyboard extends ReplyKeyboardMarkup {
    public SetDayloopAlertKeyboard(Language language) {
        super();

        this.setSelective(true);
        this.setResizeKeyboard(true);
        this.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add(DayloopAlarmCommand.D1.getCommand(language));
        keyboardFirstRow.add(DayloopAlarmCommand.D2.getCommand(language));
        keyboardFirstRow.add(DayloopAlarmCommand.D3.getCommand(language));

        KeyboardRow keyboardSecondRow = new KeyboardRow();
        keyboardSecondRow.add(DayloopAlarmCommand.D4.getCommand(language));
        keyboardSecondRow.add(DayloopAlarmCommand.D5.getCommand(language));
        keyboardSecondRow.add(DayloopAlarmCommand.D6.getCommand(language));

        KeyboardRow keyboardThirdRow = new KeyboardRow();
        keyboardThirdRow.add(DayloopAlarmCommand.D7.getCommand(language));
        keyboardThirdRow.add(DayloopAlarmCommand.OFF.getCommand(language));
        keyboardThirdRow.add(DayloopAlarmCommand.OUT.getCommand(language));

        keyboard.add(keyboardFirstRow);
        keyboard.add(keyboardSecondRow);
        keyboard.add(keyboardThirdRow);

        this.setKeyboard(keyboard);
    }


}
