package com.podo.coinchatbot.telegram.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.updateshandlers.SentCallback;

@EqualsAndHashCode
@Getter
public class SendMessageVo {

    private String telegramId;
    private Integer messageId;
    private String message;
    private ReplyKeyboard keyboard;

    private SendMessageVo(MessageVo messageVo, String message, ReplyKeyboard keyboard) {
        this.telegramId = messageVo.getTelegramId();
        this.messageId = messageVo.getMessageId();
        this.message = message;
        this.keyboard = keyboard;
    }

    public static SendMessageVo create(MessageVo messageVo, String message, ReplyKeyboard keyboard) {
        return new SendMessageVo(messageVo, message, keyboard);
    }

}

