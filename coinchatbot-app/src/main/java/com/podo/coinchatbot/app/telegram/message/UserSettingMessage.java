package com.podo.coinchatbot.app.telegram.message;

import com.podo.coinchatbot.core.Language;
import com.podo.coinchatbot.app.telegram.exception.InvalidUserLanguageException;
import com.podo.coinchatbot.app.domain.dto.UserDto;
import com.podo.coinchatbot.app.util.MessageUtil;
import com.podo.coinchatbot.app.telegram.CoinFormatter;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserSettingMessage {

    public String get(UserDto user, CoinFormatter coinFormatter) {
        switch (user.getLanguage()) {
            case KR:
                return kr(user, coinFormatter);
            case EN:
                return en(user, coinFormatter);
            default:
                throw new InvalidUserLanguageException();
        }
    }

    private String kr(UserDto user, CoinFormatter coinFormatter) {
        StringBuilder message = new StringBuilder();

        message.append("현재 설정은 다음과 같습니다.\n");
        message.append("-----------------\n");

        message.append("거래소     = ");
        message.append(MessageUtil.toMarketStr(user.getMarket(), Language.KR) + "\n");


        if (user.getDayloopAlarm() != 0) {
            message.append("일일알림 = 매 " + user.getDayloopAlarm() + " 일 주기 알림\n");
        } else {
            message.append("일일알림 = 알람 없음\n");
        }

        if (user.getTimeloopAlarm() != 0) {
            message.append("시간알림 = 매 " + user.getTimeloopAlarm() + " 시간 주기 알림\n");
        } else {
            message.append("시간알림 = 알람 없음\n");
        }

        if (user.getInvest() != null) {
            message.append("투자금액 = " + coinFormatter.toInvestAmountStr(user.getInvest(), user.getMarket()) + "\n");
        } else {
            message.append("투자금액 = 입력되어있지 않음.\n");
        }

        if (user.getCoinCount() != null) {
            message.append("코인개수 = " + coinFormatter.toCoinCntStr(user.getCoinCount(), Language.KR) + "\n");
        } else {
            message.append("코인개수 = 입력되어있지 않음.\n");
        }

        return message.toString();
    }

    private String en(UserDto user, CoinFormatter coinFormatter) {
        StringBuilder message = new StringBuilder();
        message.append("The current setting is as follows.\n");
        message.append("-----------------\n");

        message.append("Market = ");
        message.append(MessageUtil.toMarketStr(user.getMarket(), Language.EN) + "\n");

        if (user.getDayloopAlarm() != 0) {
            message.append("Daily Notification = every " + user.getDayloopAlarm() + " days\n");
        } else {
            message.append("Daily Notification = No notifications.\n");
        }

        if (user.getTimeloopAlarm() != 0) {
            message.append("Hourly Notification = every " + user.getTimeloopAlarm() + " hours\n");
        } else {
            message.append("Hourly Notification = No notifications.\n");
        }

        if (user.getInvest() != null) {
            message.append("Investment amount = " + coinFormatter.toInvestAmountStr(user.getInvest(), user.getMarket()) + "\n");
        } else {
            message.append("Investment amount = Not entered.\n");
        }

        if (user.getCoinCount() != null) {
            message.append("The number of coins = " + coinFormatter.toCoinCntStr(user.getCoinCount(), Language.EN) + "\n");
        } else {
            message.append("The number of coins = Not entered.\n");
        }

        return message.toString();
    }
}
