package com.podo.coinchatbot.telegram;

import com.podo.coinchatbot.telegram.model.SendMessageVo;

public class TelegramMessageSender extends TelegramMessageAbstractSender {

    public TelegramMessageSender(String botToken) {
        super(botToken);
    }

    @Override
    public void sendMessage(SendMessageVo sendmessageVo) {
        super.sendMessage(sendmessageVo);
    }

}
