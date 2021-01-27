package com.podo.coinchatbot.app.telegram;

import com.podo.coinchatbot.log.ThreadLocalContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@RequiredArgsConstructor
public class TelegramMessageReceiver extends TelegramLongPollingBot {

    private final String botToken;
    private final String botUsername;
    private final TelegramMessageReceiverHandler telegramMessageReceiverHandler;
    private final ExecutorService executorService = Executors.newFixedThreadPool(1);

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public void onUpdateReceived(Update update) {
       executorService.submit(() ->{
           ThreadLocalContext.init("message-receive");
           telegramMessageReceiverHandler.handle(update);
       });
    }


}
