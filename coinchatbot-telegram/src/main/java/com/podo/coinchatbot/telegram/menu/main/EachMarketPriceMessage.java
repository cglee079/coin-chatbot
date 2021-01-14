package com.podo.coinchatbot.telegram.menu.main;

import com.podo.coinchatbot.core.Lang;
import com.podo.coinchatbot.core.Market;
import com.podo.coinchatbot.telegram.model.UserDto;
import com.podo.coinchatbot.telegram.util.MessageUtil;
import com.podo.coinchatbot.telegram.util.NumberFormatter;
import com.podo.coinchatbot.telegram.util.TimeStamper;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class EachMarketPriceMessage {

    public static String get(UserDto user, Map<Market, Double> marketToCurrentPrice, Double exchangeRate, NumberFormatter numberFormatter) {
        StringBuilder message = new StringBuilder();
        Market userMarket = user.getMarket();
        Lang lang = user.getLang();
        String date = TimeStamper.getDateTime(user.getLocaltime());
        Double currentPrice = marketToCurrentPrice.get(userMarket);

        switch (lang) {
            case KR:
                message.append("현재 시각  : " + date + "\n");
                message.append("\n");
                message.append("나의 거래소 : " + MessageUtil.toMarketStr(userMarket, lang) + "\n");
                message.append("금일의 환율 : $1 = " + numberFormatter.toExchangeRateKRWStr(exchangeRate) + "\n");
                message.append("----------------------------\n");
                break;
            case EN:
                message.append("Current Time  : " + date + "\n");
                message.append("\n");
                message.append("My Market     : " + MessageUtil.toMarketStr(userMarket, lang) + "\n");
                message.append("Exchange rate : $1 = " + numberFormatter.toKRWStr(exchangeRate) + "\n");
                message.append("----------------------------\n");
                break;
        }

        Market marketKRW = Market.COINONE; // KRW 대표
        Market marketUSD = Market.BITFINEX; // USD 대표

        if (userMarket.isKRW()) { // 설정된 마켓이 한화인 경우,
            for (Market market : marketToCurrentPrice.keySet()) {
                if (market == userMarket) { // 내 마켓
                    message.append("★ ");
                }

                message.append(MessageUtil.toMarketStr(market, lang) + "  : ");
                if (marketToCurrentPrice.get(market) == -1) {
                    message.append("Server Error");
                    continue;
                }

                if (market.isKRW()) {
                    message.append(numberFormatter.toMoneyStr(marketToCurrentPrice.get(market), marketKRW));
                    message.append("  [" + numberFormatter.toMoneyStr(marketToCurrentPrice.get(market) / exchangeRate, marketUSD) + "]");
                }

                if (market.isUSD()) {
                    message.append(numberFormatter.toMoneyStr(marketToCurrentPrice.get(market) * exchangeRate, marketKRW));
                    message.append("  [" + numberFormatter.toMoneyStr(marketToCurrentPrice.get(market), marketUSD) + "]");
                    message.append(" ( P. " + numberFormatter.toSignPercentStr(currentPrice, marketToCurrentPrice.get(market) * exchangeRate) + ") ");
                }

                message.append("\n");
            }
        }

        if (userMarket.isUSD()) { // 설정된 마켓이 달러인 경우,
            for (Market market : marketToCurrentPrice.keySet()) {
                if (market == userMarket) {
                    message.append("★ ");
                }

                message.append(MessageUtil.toMarketStr(market, lang) + "  : ");
                if (marketToCurrentPrice.get(market) == -1) {
                    message.append("Server Error");
                    continue;
                }

                if (market.isKRW()) {
                    message.append(numberFormatter.toMoneyStr(marketToCurrentPrice.get(market) / exchangeRate, marketUSD));
                    message.append("  [" + numberFormatter.toMoneyStr(marketToCurrentPrice.get(market), marketKRW) + "]");
                }

                if (market.isUSD()) {
                    message.append(numberFormatter.toMoneyStr(marketToCurrentPrice.get(market), marketUSD));
                    message.append("  [" + numberFormatter.toMoneyStr(marketToCurrentPrice.get(market) * exchangeRate, marketKRW) + "]");
                }
            }
            message.append("\n");
        }


        return message.toString();
    }
}
