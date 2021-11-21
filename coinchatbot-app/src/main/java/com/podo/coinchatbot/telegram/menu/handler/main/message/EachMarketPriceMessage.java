package com.podo.coinchatbot.telegram.menu.handler.main.message;

import com.podo.coinchatbot.app.domain.dto.UserDto;
import com.podo.coinchatbot.telegram.CoinFormatter;
import com.podo.coinchatbot.util.DateTimeUtil;
import com.podo.coinchatbot.util.MessageUtil;
import com.podo.coinchatbot.core.Language;
import com.podo.coinchatbot.core.Market;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Map;

public class EachMarketPriceMessage {

    public static String get(UserDto user, Map<Market, BigDecimal> marketToCurrentPrice, BigDecimal exchangeRate, CoinFormatter coinFormatter, LocalDateTime now) {
        StringBuilder message = new StringBuilder();
        Market userMarket = user.getMarket();
        Language language = user.getLanguage();
        String userLocaleNow = DateTimeUtil.toDateTimeString(now, user.getTimeDifference());
        BigDecimal currentPrice = marketToCurrentPrice.get(userMarket);

        switch (language) {
            case KR:
                message.append("현재 시각  : " + userLocaleNow + "\n");
                message.append("\n");
                message.append("나의 거래소 : " + MessageUtil.toMarketStr(userMarket, language) + "\n");
                message.append("금일의 환율 : $1 = " + coinFormatter.toExchangeRateKRWStr(exchangeRate) + "\n");
                message.append("----------------------------\n");
                break;
            case EN:
                message.append("Current Time  : " + userLocaleNow + "\n");
                message.append("\n");
                message.append("My Market     : " + MessageUtil.toMarketStr(userMarket, language) + "\n");
                message.append("Exchange rate : $1 = " + coinFormatter.toKRWStr(exchangeRate) + "\n");
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

                message.append(MessageUtil.toMarketStr(market, language) + "  : ");

                if (marketToCurrentPrice.get(market) == null) {
                    message.append("Server Error\n");
                    continue;
                }

                if (market.isKRW()) {
                    message.append(coinFormatter.toMoneyStr(marketToCurrentPrice.get(market), marketKRW));
                    message.append("  [" + coinFormatter.toMoneyStr(marketToCurrentPrice.get(market).divide(exchangeRate, 10, RoundingMode.HALF_UP), marketUSD) + "]");
                }

                if (market.isUSD()) {
                    message.append(coinFormatter.toMoneyStr(marketToCurrentPrice.get(market).multiply(exchangeRate), marketKRW));
                    message.append("  [" + coinFormatter.toMoneyStr(marketToCurrentPrice.get(market), marketUSD) + "]");
                    message.append(" ( P. " + coinFormatter.toSignPercentStr(currentPrice, marketToCurrentPrice.get(market).multiply(exchangeRate)) + ") ");
                }

                message.append("\n");
            }
        }

        if (userMarket.isUSD()) { // 설정된 마켓이 달러인 경우,
            for (Market market : marketToCurrentPrice.keySet()) {
                if (market == userMarket) {
                    message.append("★ ");
                }

                message.append(MessageUtil.toMarketStr(market, language) + "  : ");
                if (marketToCurrentPrice.get(market) == null) {
                    message.append("Server Error\n");
                    continue;
                }

                if (market.isKRW()) {
                    message.append(coinFormatter.toMoneyStr(marketToCurrentPrice.get(market).divide(exchangeRate, 10, RoundingMode.HALF_UP), marketUSD));
                    message.append("  [" + coinFormatter.toMoneyStr(marketToCurrentPrice.get(market), marketKRW) + "]");
                }

                if (market.isUSD()) {
                    message.append(coinFormatter.toMoneyStr(marketToCurrentPrice.get(market), marketUSD));
                    message.append("  [" + coinFormatter.toMoneyStr(marketToCurrentPrice.get(market).multiply(exchangeRate), marketKRW) + "]");
                }

                message.append("\n");
            }
        }


        return message.toString();
    }
}
