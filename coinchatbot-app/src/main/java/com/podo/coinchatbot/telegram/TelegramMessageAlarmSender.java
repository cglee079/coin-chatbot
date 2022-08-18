package com.podo.coinchatbot.telegram;

import com.podo.coinchatbot.telegram.model.MessageVo;
import com.podo.coinchatbot.telegram.model.SendMessageVo;

public class TelegramMessageAlarmSender extends TelegramMessageAbstractSender {

    private final String coupangUrl;

    public TelegramMessageAlarmSender(String botToken, String coupangUrl) {
        super(botToken);
        this.coupangUrl = coupangUrl;
    }

    public void sendCoupangRecommend(MessageVo sv) {
        String message = new StringBuilder()
                .append("쿠팡에 쇼핑하러가기 \uD83D\uDE80!\n")
                .append("\n")
                .append(coupangUrl + "\n")
                .append("\n")
                .append("이 포스팅은 쿠팡 파트너스 활동의 일환으로, 이에 따른 일정액의 수수료를 제공받습니다.")
                .toString();

        super.sendMessage(SendMessageVo.create(sv, message , null));
    }


}
