package com.podo.coinchatbot.telegram.menu;


import com.podo.coinchatbot.app.domain.dto.UserDto;
import com.podo.coinchatbot.app.model.CoinMeta;
import com.podo.coinchatbot.app.model.Menu;
import com.podo.coinchatbot.telegram.TelegramMessageSender;
import com.podo.coinchatbot.telegram.model.MessageVo;
import com.podo.coinchatbot.core.Coin;

public interface MenuHandler {

    Menu getHandleMenu();

    void handle(MessageVo messageVo, Coin coin, CoinMeta coinMeta, UserDto user, String messageText, TelegramMessageSender sender);

}
