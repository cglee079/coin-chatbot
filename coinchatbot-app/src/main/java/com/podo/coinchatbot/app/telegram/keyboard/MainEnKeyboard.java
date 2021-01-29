package com.podo.coinchatbot.app.telegram.keyboard;


import com.podo.coinchatbot.app.telegram.command.MainCommand;
import com.podo.coinchatbot.core.Language;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class MainEnKeyboard extends ReplyKeyboardMarkup {
    public MainEnKeyboard(Language language) {
        super();

        this.setSelective(true);
        this.setResizeKeyboard(true);
        this.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add(MainCommand.CURRENT_PRICE.getCommand(language));
        keyboardFirstRow.add(MainCommand.MARKETS_PRICE.getCommand(language));
        keyboardFirstRow.add(MainCommand.COMPARED_TO_BTC.getCommand(language));
        keyboardFirstRow.add(MainCommand.CALCULATE.getCommand(language));

        KeyboardRow keyboardSecondRow = new KeyboardRow();
        keyboardSecondRow.add(MainCommand.INVESET_CONFIG.getCommand(language));
        keyboardSecondRow.add(MainCommand.COINCOUNT_CONFIG.getCommand(language));
        keyboardSecondRow.add(MainCommand.MARKET_CONFIG.getCommand(language));

        KeyboardRow keyboardThirdRow = new KeyboardRow();
        keyboardSecondRow.add(MainCommand.TARGET_ALARM_CONFIG.getCommand(language));
        keyboardThirdRow.add(MainCommand.DAYLOOP_ALARM_CONFIG.getCommand(language));
        keyboardThirdRow.add(MainCommand.TIMELOOP_ALARM_CONFIG.getCommand(language));

        KeyboardRow keyboardForthRow = new KeyboardRow();
        keyboardThirdRow.add(MainCommand.HAPPY_LINE.getCommand(language));
        keyboardThirdRow.add(MainCommand.SHOW_SETTING.getCommand(language));
        keyboardThirdRow.add(MainCommand.STOP_ALL_ALARAM.getCommand(language));

        KeyboardRow keyboardFifthRow = new KeyboardRow();
        keyboardForthRow.add(MainCommand.COIN_LIST.getCommand(language));
        keyboardForthRow.add(MainCommand.SEND_MESSAGE.getCommand(language));
        keyboardForthRow.add(MainCommand.SPONSORING.getCommand(language));
        keyboardForthRow.add(MainCommand.HELP.getCommand(language));
        keyboardForthRow.add(MainCommand.PREFERENCE.getCommand(language));

        keyboard.add(keyboardFirstRow);
        keyboard.add(keyboardSecondRow);
        keyboard.add(keyboardThirdRow);
        keyboard.add(keyboardForthRow);
        keyboard.add(keyboardFifthRow);
        this.setKeyboard(keyboard);
    }


}
