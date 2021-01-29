package com.podo.coinchatbot.app.telegram.keyboard;


import com.podo.coinchatbot.app.telegram.command.TimeloopAlarmCommand;
import com.podo.coinchatbot.core.Language;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class SetTimeloopAlertKeyboard extends ReplyKeyboardMarkup {
    public SetTimeloopAlertKeyboard(Language language) {
        super();
        this.setSelective(true);
        this.setResizeKeyboard(true);
        this.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add(TimeloopAlarmCommand.H1.getCommand(language));
        keyboardFirstRow.add(TimeloopAlarmCommand.H2.getCommand(language));
        keyboardFirstRow.add(TimeloopAlarmCommand.H3.getCommand(language));
        keyboardFirstRow.add(TimeloopAlarmCommand.H4.getCommand(language));

        KeyboardRow keyboardSecondRow = new KeyboardRow();
        keyboardSecondRow.add(TimeloopAlarmCommand.H5.getCommand(language));
        keyboardSecondRow.add(TimeloopAlarmCommand.H6.getCommand(language));
        keyboardSecondRow.add(TimeloopAlarmCommand.H7.getCommand(language));
        keyboardSecondRow.add(TimeloopAlarmCommand.H8.getCommand(language));

        KeyboardRow keyboardThirdRow = new KeyboardRow();
        keyboardThirdRow.add(TimeloopAlarmCommand.H9.getCommand(language));
        keyboardThirdRow.add(TimeloopAlarmCommand.H10.getCommand(language));
        keyboardThirdRow.add(TimeloopAlarmCommand.H11.getCommand(language));
        keyboardThirdRow.add(TimeloopAlarmCommand.H12.getCommand(language));

        KeyboardRow keyboardForthRow = new KeyboardRow();
        keyboardForthRow.add(TimeloopAlarmCommand.OFF.getCommand(language));
        keyboardForthRow.add(TimeloopAlarmCommand.OUT.getCommand(language));

        keyboard.add(keyboardFirstRow);
        keyboard.add(keyboardSecondRow);
        keyboard.add(keyboardThirdRow);
        keyboard.add(keyboardForthRow);

        this.setKeyboard(keyboard);
    }


}
