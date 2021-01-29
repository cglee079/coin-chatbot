package com.podo.coinchatbot.app.telegram.menu.handler.main.message;

import com.podo.coinchatbot.app.domain.dto.UserDto;
import com.podo.coinchatbot.app.telegram.CoinFormatter;
import com.podo.coinchatbot.app.telegram.exception.InvalidUserLanguageException;
import com.podo.coinchatbot.app.util.DateTimeUtil;
import com.podo.coinchatbot.core.Language;
import com.podo.coinchatbot.core.Market;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@UtilityClass
public class CurrentPriceMessage {

    public static String getInMoney(UserDto user, BigDecimal currentPrice, CoinFormatter coinFormatter, LocalDateTime now) {
        Language language = user.getLanguage();
        Market market = user.getMarket();
        String userLocaleNow = DateTimeUtil.toDateTimeString(now, user.getTimeDifference());

        switch (language) {
            case KR:
                return "현재시각 : " + userLocaleNow +
                        "\n" + "현재가격 : "
                        + coinFormatter.toMoneyStr(currentPrice, market);
            case EN:
                return "Current Time  : " + userLocaleNow +
                        "\n" + "Current Price : " +
                        coinFormatter.toMoneyStr(currentPrice, market);
        }

        throw new InvalidUserLanguageException();
    }

    public static String getInBtc(UserDto user, BigDecimal currentPrice, BigDecimal currentBTC, CoinFormatter coinFormatter, LocalDateTime now) {
        Language language = user.getLanguage();
        Market market = user.getMarket();
        String userLocalNow = DateTimeUtil.toDateTimeString(now, user.getTimeDifference());
        StringBuilder message = new StringBuilder();

        switch (language) {
            case KR:
                message.append("현재시각 : " + userLocalNow + "\n");
                message.append("현재가격 : " + coinFormatter.toMoneyStr(currentPrice, market) + " [" + coinFormatter.toBTCStr(currentBTC) + "]\n");
                return message.toString();

            case EN:
                message.append("Current Time  : " + userLocalNow + "\n");
                message.append("Current Price : " + coinFormatter.toMoneyStr(currentPrice, market) + " [" + coinFormatter.toBTCStr(currentBTC) + "]\n");
                return message.toString();
        }

        throw new InvalidUserLanguageException();
    }
}
