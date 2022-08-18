package com.podo.coinchatbot.telegram.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

@EqualsAndHashCode
@Getter
public class SendMessageVo {

    private Long telegramId;
    private Long chatId;
    private Integer messageId;
    private String message;
    private ReplyKeyboard keyboard;

    private SendMessageVo(MessageVo messageVo, String message, ReplyKeyboard keyboard) {
        this.telegramId = messageVo.getTelegramId();
        this.chatId = messageVo.getChatId();
        this.messageId = messageVo.getMessageId();
        this.message = message;
        this.keyboard = keyboard;
    }

    public SendMessageVo(Long telegramId, Long chatId, Integer messageId, String message, ReplyKeyboard keyboard) {
        this.telegramId = telegramId;
        this.chatId = chatId;
        this.messageId = messageId;
        this.message = message;
        this.keyboard = keyboard;
    }

    public static SendMessageVo create(MessageVo messageVo, String message, ReplyKeyboard keyboard) {
        return new SendMessageVo(messageVo, message, keyboard);
    }

}

