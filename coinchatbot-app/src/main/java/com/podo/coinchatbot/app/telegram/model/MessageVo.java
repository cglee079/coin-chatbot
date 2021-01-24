package com.podo.coinchatbot.app.telegram.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
@Getter
public class MessageVo {

    private Integer telegramId;
    private Long chatId;
    private Integer messageId;

    public MessageVo(Integer telegramId, Long chatId) {
        this.telegramId = telegramId;
        this.chatId = chatId;
    }

    public MessageVo(Integer telegramId, Long chatId, Integer messageId) {
        this.telegramId = telegramId;
        this.chatId = chatId;
        this.messageId = messageId;
    }
}

