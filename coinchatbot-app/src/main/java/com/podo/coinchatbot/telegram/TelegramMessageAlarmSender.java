package com.podo.coinchatbot.telegram;

import com.podo.coinchatbot.telegram.model.MessageVo;
import com.podo.coinchatbot.telegram.model.SendMessageVo;

public class TelegramMessageAlarmSender extends TelegramMessageSender {

    private final String coupangUrl;

    public TelegramMessageAlarmSender(String botToken, String coupangUrl) {
        super(botToken);
        this.coupangUrl = coupangUrl;
    }

    public void sendCoupangRecommend(MessageVo sv) {
        super.sendMessage(SendMessageVo.create(sv, "쇼핑은 쿠팡 \uD83D\uDE80 !! : " + coupangUrl, null));
    }


}
