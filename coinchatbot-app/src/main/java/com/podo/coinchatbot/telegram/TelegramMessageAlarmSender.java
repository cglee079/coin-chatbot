package com.podo.coinchatbot.telegram;

import com.podo.coinchatbot.telegram.model.SendMessageVo;

public class TelegramMessageAlarmSender extends TelegramMessageSender {

    private final String coupangUrl;

    public TelegramMessageAlarmSender(String botToken, String coupangUrl) {
        super(botToken);
        this.coupangUrl = coupangUrl;
    }

    public void sendAlarm(SendMessageVo sv) {
        super.sendMessage(sv);
        SendMessageVo coupangRecommendSv = new SendMessageVo(sv.getTelegramId(), sv.getChatId(), sv.getMessageId(), "쇼핑은 쿠팡 \uD83D\uDE80 !! : " + coupangUrl, null);
        super.sendMessage(coupangRecommendSv);
    }


}
