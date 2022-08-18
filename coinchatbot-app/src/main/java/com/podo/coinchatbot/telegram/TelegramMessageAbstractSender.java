package com.podo.coinchatbot.telegram;

import com.podo.coinchatbot.log.ThreadLocalContext;
import com.podo.coinchatbot.telegram.exception.TelegramApiRuntimeException;
import com.podo.coinchatbot.telegram.model.SendMessageVo;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public abstract class TelegramMessageAbstractSender extends DefaultAbsSender {

    private final String botToken;

    public TelegramMessageAbstractSender(String botToken) {
        super(new DefaultBotOptions());
        this.botToken = botToken;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    public void sendMessage(SendMessageVo sendmessageVo) {
        ThreadLocalContext.putTelegramMessageSend(sendmessageVo.getMessage());
        final SendMessage sendMessage = new SendMessage(sendmessageVo.getChatId().toString(), sendmessageVo.getMessage());

        sendMessage.setReplyMarkup(sendmessageVo.getKeyboard());
        sendMessage.setReplyToMessageId(sendmessageVo.getMessageId());
        sendMessage.enableHtml(true);
        sendMessage.enableMarkdown(false);
        sendMessage.

        try {
            this.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new TelegramApiRuntimeException(e, sendmessageVo.getChatId(), sendmessageVo.getMessage());
        }
    }

}
