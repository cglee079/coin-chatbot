package com.podo.coinchatbot.app.telegram;


import com.podo.coinchatbot.app.model.Menu;
import com.podo.coinchatbot.app.telegram.menu.MenuHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

@Configuration
public class TelegramAppConfig {

    @Bean
    public Map<Menu, MenuHandler> menuHandlers(List<MenuHandler> menuHandlers) {
        return menuHandlers.stream()
                .collect(toMap(MenuHandler::getHandleMenu, x -> x));
    }

    @Bean
    public TelegramBotsApi TelegramBotRegisterConfig(List<TelegramMessageReceiver> telegramMessageReceivers) throws TelegramApiRequestException {
        final TelegramBotsApi telegramBotsApi = new TelegramBotsApi();

        for (TelegramMessageReceiver telegramMessageReceiver : telegramMessageReceivers) {
            telegramBotsApi.registerBot(telegramMessageReceiver);
        }

        return telegramBotsApi;
    }


}
