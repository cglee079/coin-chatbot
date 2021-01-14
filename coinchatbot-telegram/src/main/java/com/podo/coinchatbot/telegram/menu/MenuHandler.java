package com.podo.coinchatbot.telegram.menu;


import com.podo.coinchatbot.telegram.app.TelegramMessageSender;
import com.podo.coinchatbot.telegram.model.Menu;
import com.podo.coinchatbot.telegram.model.MessageVo;

public interface MenuHandler {

    Menu getHandleMenu();

    TelegramMessageSender sender();

    void handle(MessageVo messageVo, String messageContents);

}
