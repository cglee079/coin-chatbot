package com.podo.coinchatbot.telegram.menu.main;

import com.podo.coinchatbot.core.Coin;
import com.podo.coinchatbot.core.Lang;
import com.podo.coinchatbot.core.Market;
import com.podo.coinchatbot.telegram.exception.InvalidLanguageException;
import com.podo.coinchatbot.telegram.util.MessageUtil;
import com.podo.coinchatbot.telegram.util.NumberFormatter;

public class ComparedToBTCMessage {

    public static String currentTime(String now, Lang lang) {
        StringBuilder message = new StringBuilder();
        switch (lang) {
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

        throw new InvalidLanguageException();
    }

    public static String notSupportMarket(Market market, Lang lang) {
        String marketStr = MessageUtil.toMarketStr(market, lang);

        switch (lang) {
            case KR:
                return marketStr + " API는 해당 정보를 제공하지 않습니다.\n";
            case EN:
                return marketStr + " market API does not provide this information.\n";
        }

        throw new InvalidLanguageException();
    }

    public static String replaceAnotherMarket(Market market, Lang lang) {
        String marketStr = MessageUtil.toMarketStr(market, lang);
        switch (lang) {
            case KR:
                return marketStr + " 기준 정보로 대체합니다.\n";
            case EN:
                return "Replace with " + marketStr + " market information.\n";
        }

        throw new InvalidLanguageException();
    }

    public static String result(Lang lang, Coin coin, Market market, double btcBV, double btcCV, double coinBV, double coinCV, NumberFormatter numberFormatter) {
        StringBuilder msg = new StringBuilder("");
        switch (lang) {
            case KR:
                msg.append("BTC 현재 시각 가격 : " + numberFormatter.toOnlyBTCMoneyStr(btcCV, market) + "\n");
                msg.append("BTC 24시간전 가격 : " + numberFormatter.toOnlyBTCMoneyStr(btcBV, market) + "\n");
                msg.append("\n");
                msg.append(coin + " 현재 시각 가격 : " + numberFormatter.toMoneyStr(coinCV, market) + "\n");
                msg.append(coin + " 24시간전 가격 : " + numberFormatter.toMoneyStr(coinBV, market) + "\n");
                msg.append("\n");
                msg.append("BTC 24시간 변화량 : " + numberFormatter.toSignPercentStr(btcCV, btcBV) + "\n");
                msg.append(coin + " 24시간 변화량 : " + numberFormatter.toSignPercentStr(coinCV, coinBV) + "\n");
                return msg.toString();

            case EN:
                msg.append("BTC Price at current time : " + numberFormatter.toMoneyStr(btcCV, market) + "\n");
                msg.append("BTC Price before 24 hours : " + numberFormatter.toMoneyStr(btcBV, market) + "\n");
                msg.append("\n");
                msg.append(coin + " Price at current time : " + numberFormatter.toMoneyStr(coinCV, market) + "\n");
                msg.append(coin + " Price before 24 hours : " + numberFormatter.toMoneyStr(coinBV, market) + "\n");
                msg.append("\n");
                msg.append("BTC 24 hour rate of change : " + numberFormatter.toSignPercentStr(btcCV, btcBV) + "\n");
                msg.append(coin + " 24 hour rate of change : " + numberFormatter.toSignPercentStr(coinCV, coinBV) + "\n");
                return msg.toString();
        }

        throw new InvalidLanguageException();
    }

}
