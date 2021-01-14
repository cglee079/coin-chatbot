package com.podo.coinchatbot.telegram.menu.main;

import com.podo.coinchatbot.core.Lang;
import com.podo.coinchatbot.core.Market;
import com.podo.coinchatbot.telegram.exception.InvalidLanguageException;
import com.podo.coinchatbot.telegram.model.UserDto;
import com.podo.coinchatbot.telegram.util.NumberFormatter;
import com.podo.coinchatbot.telegram.util.TimeStamper;
import lombok.experimental.UtilityClass;
import org.json.JSONObject;

@UtilityClass
public class CurrentPriceMessage {

    public static String getInMoney(UserDto user, Double currentPrice, NumberFormatter numberFormatter){
        Lang lang = user.getLang();
        Market market = user.getMarket();
        String now = TimeStamper.getDateTime(user.getLocaltime());

        switch (lang){
            case KR:
                return "현재시각 : " + now +
                        "\n" + "현재가격 : "
                        + numberFormatter.toMoneyStr(currentPrice, market);
            case EN:
                return "Current Time  : " + now +
                        "\n" + "Current Price : " +
                        numberFormatter.toMoneyStr(currentPrice, market);
        }

        throw new InvalidLanguageException();
    }
    public static String getInBtc(UserDto user, Double currentPrice, Double currentBTC, NumberFormatter numberFormatter){
        Lang lang = user.getLang();
        Market market = user.getMarket();
        String now = TimeStamper.getDateTime(user.getLocaltime());
        StringBuilder message = new StringBuilder();

        switch (lang) {
            case KR:
                message.append("현재시각 : " + now + "\n");
                message.append("현재가격 : " + numberFormatter.toMoneyStr(currentPrice, market) + " [" + numberFormatter.toBTCStr(currentBTC) + "]\n");
                return message.toString();

            case EN:
                message.append("Current Time  : " + now + "\n");
                message.append("Current Price : " + numberFormatter.toMoneyStr(currentPrice, market) + " [" + numberFormatter.toBTCStr(currentBTC) + "]\n");
                return message.toString();
        }

        throw new InvalidLanguageException();
    }
}
