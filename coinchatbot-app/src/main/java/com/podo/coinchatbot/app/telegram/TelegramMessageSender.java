package com.podo.coinchatbot.app.telegram;

import com.podo.coinchatbot.log.ThreadLocalContext;
import com.podo.coinchatbot.core.Coin;
import com.podo.coinchatbot.app.telegram.exception.TelegramApiRuntimeException;
import com.podo.coinchatbot.app.telegram.model.SendMessageVo;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.ApiContext;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class TelegramMessageSender extends DefaultAbsSender {

    private final String botToken;

    public TelegramMessageSender(String botToken) {
        super(ApiContext.getInstance(DefaultBotOptions.class));
        this.botToken = botToken;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    public void sendMessage(SendMessageVo sendmessageVo) {
        ThreadLocalContext.putSendMessage(sendmessageVo.getMessage());
        send(sendmessageVo);
    }

    public void sendAlarm(SendMessageVo sendMessageVo){
        ThreadLocalContext.putSendMessage(sendMessageVo.getMessage());
        send(sendMessageVo);
    }

    private void send(SendMessageVo sendmessageVo) {
        final SendMessage sendMessage = new SendMessage(sendmessageVo.getChatId(), sendmessageVo.getMessage());

        sendMessage.setReplyMarkup(sendmessageVo.getKeyboard());
        sendMessage.setReplyToMessageId(sendmessageVo.getMessageId());
        sendMessage.enableHtml(true);
        sendMessage.enableMarkdown(false);

        try {
            this.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new TelegramApiRuntimeException(e, sendmessageVo.getChatId(), sendmessageVo.getMessage());
        }
    }

}
