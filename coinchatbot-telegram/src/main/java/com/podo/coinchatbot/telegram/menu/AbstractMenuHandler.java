package com.podo.coinchatbot.telegram.menu;


import com.podo.coinchatbot.telegram.app.TelegramMessageSender;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public abstract class AbstractMenuHandler implements MenuHandler {

    @Autowired
    private TelegramMessageSender telegramMessageSender;

    @Override
    public TelegramMessageSender sender() {
        return telegramMessageSender;
    }

}
