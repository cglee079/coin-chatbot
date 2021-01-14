package com.podo.coinchatbot.telegram;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

public class TelegramClient {

    public TelegramClient(List<TelegramBot> telegramBots) throws TelegramApiException {
        TelegramBotsApi api = new TelegramBotsApi();
        for (TelegramBot telegramBot : telegramBots) {
            api.registerBot(telegramBot);
        }
    }

}
