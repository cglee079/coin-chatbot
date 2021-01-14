package com.podo.coinchatbot.telegram.message;

import com.podo.coinchatbot.core.Lang;
import com.podo.coinchatbot.core.Market;
import com.podo.coinchatbot.telegram.exception.InvalidLanguageException;
import com.podo.coinchatbot.telegram.model.UserDto;
import com.podo.coinchatbot.telegram.util.MessageUtil;
import com.podo.coinchatbot.telegram.util.NumberFormatter;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ClientSettingMessage {

    public String get(UserDto user, Lang lang, NumberFormatter numberFormatter) {
        switch(lang) {
            case KR :
                return kr(user, lang, numberFormatter);
            case EN :
                return en(user, lang, numberFormatter);
        }

        throw new InvalidLanguageException();
    }

    private String kr(UserDto user, Lang lang, NumberFormatter numberFormatter) {
        StringBuilder message = new StringBuilder();

        message.append("현재 설정은 다음과 같습니다.\n");
        message.append("-----------------\n");

        message.append("거래소     = ");
        message.append(MessageUtil.toMarketStr(user.getMarket(), lang) + "\n");


        if(user.getDayloopAlert() != 0){ message.append("일일알림 = 매 " + user.getDayloopAlert() + " 일 주기 알림\n");}
        else{ message.append("일일알림 = 알람 없음\n");}

        if(user.getTimeloopAlert() != 0){ message.append("시간알림 = 매 " + user.getTimeloopAlert() + " 시간 주기 알림\n");}
        else{ message.append("시간알림 = 알람 없음\n");}

        if(user.getInvest() != null){message.append("투자금액 = " + numberFormatter.toInvestAmountStr(user.getInvest(), user.getMarket()) + "\n");}
        else { message.append("투자금액 = 입력되어있지 않음.\n");}

        if(user.getCoinCount() != null){message.append("코인개수 = " + numberFormatter.toCoinCntStr(user.getCoinCount(), lang) + "\n"); }
        else { message.append("코인개수 = 입력되어있지 않음.\n");}

        return message.toString();
    }

    private String en(UserDto user, Lang lang, NumberFormatter numberFormatter) {
        StringBuilder message = new StringBuilder();
        message.append("The current setting is as follows.\n");
        message.append("-----------------\n");

        message.append("Market = ");
        message.append(MessageUtil.toMarketStr(user.getMarket(), lang) + "\n");

        if(user.getDayloopAlert() != 0){ message.append("Daily Notification = every " + user.getDayloopAlert() + " days\n");}
        else{ message.append("Daily Notification = No notifications.\n");}

        if(user.getTimeloopAlert() != 0){ message.append("Hourly Notification = every " + user.getTimeloopAlert() + " hours\n");}
        else{ message.append("Hourly Notification = No notifications.\n");}

        if(user.getInvest() != null){message.append("Investment amount = " + numberFormatter.toInvestAmountStr(user.getInvest(), user.getMarket()) + "\n");}
        else { message.append("Investment amount = Not entered.\n");}

        if(user.getCoinCount() != null){message.append("The number of coins = " + numberFormatter.toCoinCntStr(user.getCoinCount(), lang) + "\n"); }
        else { message.append("The number of coins = Not entered.\n");}
        return;
    }
}
