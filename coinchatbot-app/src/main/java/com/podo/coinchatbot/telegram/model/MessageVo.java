package com.podo.coinchatbot.telegram.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
@Getter
public class MessageVo {

    private Long telegramId;
    private Long chatId;
    private Integer messageId;

    public MessageVo(Long telegramId, Long chatId) {
        this.telegramId = telegramId;
        this.chatId = chatId;
    }

    public MessageVo(Long telegramId, Long chatId, Integer messageId) {
        this.telegramId = telegramId;
        this.chatId = chatId;
        this.messageId = messageId;
    }
}

