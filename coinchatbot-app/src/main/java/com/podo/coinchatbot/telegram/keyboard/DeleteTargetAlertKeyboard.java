package com.podo.coinchatbot.telegram.keyboard;


import com.podo.coinchatbot.telegram.command.TargetAlarmDeleteCommand;
import com.podo.coinchatbot.core.Language;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class DeleteTargetAlertKeyboard extends ReplyKeyboardMarkup {

    public DeleteTargetAlertKeyboard(List<String> targetsCmd, Language language) {
        super();

        this.setSelective(true);
        this.setResizeKeyboard(true);
        this.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow keyboardRow = new KeyboardRow();

        for (int i = 0; i < targetsCmd.size(); i++) {
            keyboardRow = new KeyboardRow();
            keyboardRow.add(targetsCmd.get(i));
            keyboard.add(keyboardRow);
        }

        keyboardRow = new KeyboardRow();
        keyboardRow.add(TargetAlarmDeleteCommand.OUT.getCommand(language));
        keyboard.add(keyboardRow);

        this.setKeyboard(keyboard);

    }

}
