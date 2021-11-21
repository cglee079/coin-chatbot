package com.podo.coinchatbot.telegram.menu.handler.main.message;

import com.podo.coinchatbot.telegram.CoinFormatter;
import com.podo.coinchatbot.telegram.exception.InvalidUserLanguageException;
import com.podo.coinchatbot.util.MessageUtil;
import com.podo.coinchatbot.core.Coin;
import com.podo.coinchatbot.core.Language;
import com.podo.coinchatbot.core.Market;

import java.math.BigDecimal;

public class ComparedToBTCMessage {

    public static String currentTime(String now, Language language) {
        StringBuilder message = new StringBuilder();
        switch (language) {
            case KR:
                message.append("현재시각 : " + now + "\n");
                message.append("----------------------\n");
                message.append("\n");
                return message.toString();
            case EN:
                message.append("Current Time : " + now + "\n");
                message.append("----------------------\n");
                message.append("\n");
                return message.toString();
        }

        throw new InvalidUserLanguageException();
    }

    public static String notSupportMarket(Market market, Language language) {
        String marketStr = MessageUtil.toMarketStr(market, language);

        switch (language) {
            case KR:
                return marketStr + " API는 해당 정보를 제공하지 않습니다.\n";
            case EN:
                return marketStr + " market API does not provide this information.\n";
        }

        throw new InvalidUserLanguageException();
    }

    public static String replaceAnotherMarket(Market market, Language language) {
        String marketStr = MessageUtil.toMarketStr(market, language);
        switch (language) {
            case KR:
                return marketStr + " 기준 정보로 대체합니다.\n";
            case EN:
                return "Replace with " + marketStr + " market information.\n";
        }

        throw new InvalidUserLanguageException();
    }

    public static String result(Language language, Coin coin, Market market, BigDecimal btcBV, BigDecimal btcCV, BigDecimal coinBV, BigDecimal coinCV, CoinFormatter coinFormatter) {
        StringBuilder msg = new StringBuilder("");
        switch (language) {
            case KR:
                msg.append("BTC 현재 시각 가격 : " + coinFormatter.toOnlyBTCMoneyStr(btcCV, market) + "\n");
                msg.append("BTC 24시간전 가격 : " + coinFormatter.toOnlyBTCMoneyStr(btcBV, market) + "\n");
                msg.append("\n");
                msg.append(coin + " 현재 시각 가격 : " + coinFormatter.toMoneyStr(coinCV, market) + "\n");
                msg.append(coin + " 24시간전 가격 : " + coinFormatter.toMoneyStr(coinBV, market) + "\n");
                msg.append("\n");
                msg.append("BTC 24시간 변화량 : " + coinFormatter.toSignPercentStr(btcCV, btcBV) + "\n");
                msg.append(coin + " 24시간 변화량 : " + coinFormatter.toSignPercentStr(coinCV, coinBV) + "\n");
                return msg.toString();

            case EN:
                msg.append("BTC Price at current time : " + coinFormatter.toMoneyStr(btcCV, market) + "\n");
                msg.append("BTC Price before 24 hours : " + coinFormatter.toMoneyStr(btcBV, market) + "\n");
                msg.append("\n");
                msg.append(coin + " Price at current time : " + coinFormatter.toMoneyStr(coinCV, market) + "\n");
                msg.append(coin + " Price before 24 hours : " + coinFormatter.toMoneyStr(coinBV, market) + "\n");
                msg.append("\n");
                msg.append("BTC 24 hour rate of change : " + coinFormatter.toSignPercentStr(btcCV, btcBV) + "\n");
                msg.append(coin + " 24 hour rate of change : " + coinFormatter.toSignPercentStr(coinCV, coinBV) + "\n");
                return msg.toString();
        }

        throw new InvalidUserLanguageException();
    }

}
