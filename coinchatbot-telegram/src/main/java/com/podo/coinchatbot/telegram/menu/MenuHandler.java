package com.podo.coinchatbot.telegram.menu;


import com.podo.coinchatbot.core.Coin;
import com.podo.coinchatbot.telegram.app.TelegramMessageSender;
import com.podo.coinchatbot.telegram.coin.CoinMeta;
import com.podo.coinchatbot.telegram.model.Menu;
import com.podo.coinchatbot.telegram.model.MessageVo;
import com.podo.coinchatbot.telegram.model.UserDto;

public interface MenuHandler {

    Menu getHandleMenu();

    TelegramMessageSender sender();

    void handle(MessageVo messageVo, Coin coin, CoinMeta coinMeta, UserDto userDto, String messageText);

}
