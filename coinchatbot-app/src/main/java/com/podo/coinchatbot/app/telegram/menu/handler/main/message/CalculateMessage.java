package com.podo.coinchatbot.app.telegram.menu.handler.main.message;

import com.podo.coinchatbot.app.domain.dto.UserDto;
import com.podo.coinchatbot.app.telegram.CoinFormatter;
import com.podo.coinchatbot.app.telegram.exception.InvalidUserLanguageException;
import com.podo.coinchatbot.app.util.DateTimeUtil;
import com.podo.coinchatbot.core.Language;
import com.podo.coinchatbot.core.Market;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@UtilityClass
public class CalculateMessage {

    public static String calcResult(BigDecimal invest, BigDecimal coinCount, BigDecimal averageCoinPrice, BigDecimal currentPrice, UserDto user, CoinFormatter coinFormatter) {
        StringBuilder message = new StringBuilder();
        Market marketId = user.getMarket();
        Language language = user.getLanguage();
        String dateTime = DateTimeUtil.toDateTimeString(LocalDateTime.now(), user.getTimeDifference());

        BigDecimal nowInvest = currentPrice.multiply(coinCount);
        switch (language) {
            case KR:
                message.append("현재 시각  : " + dateTime + "\n");
                message.append("\n");
                message.append("코인개수 : " + coinFormatter.toCoinCntStr(coinCount, language) + "\n");
                message.append("평균단가 : " + coinFormatter.toMoneyStr(averageCoinPrice, marketId) + "\n");
                message.append("현재단가 : " + coinFormatter.toMoneyStr(currentPrice, marketId) + "\n");
                message.append("---------------------\n");
                message.append("투자금액 : " + coinFormatter.toInvestAmountStr(invest, marketId) + "\n");
                message.append("현재금액 : " + coinFormatter.toInvestAmountStr(nowInvest, marketId) + "\n");
                message.append("손익금액 : " + coinFormatter.toSignInvestAmountStr(nowInvest.subtract(invest), marketId) + " (" + coinFormatter.toSignPercentStr(nowInvest, invest) + ")\n");
                message.append("\n");
                break;

            case EN:
                message.append("Current Time  : " + dateTime + "\n");
                message.append("\n");
                message.append("The number of coins : " + coinFormatter.toCoinCntStr(coinCount, language) + "\n");
                message.append("Average Coin Price : " + coinFormatter.toMoneyStr(averageCoinPrice, marketId) + "\n");
                message.append("Current Coin Price : " + coinFormatter.toMoneyStr(currentPrice, marketId) + "\n");
                message.append("---------------------\n");
                message.append("Investment Amount : " + coinFormatter.toInvestAmountStr(invest, marketId) + "\n");
                message.append("Curernt Amount : " + coinFormatter.toInvestAmountStr(nowInvest, marketId) + "\n");
                message.append("Profit and loss : " + coinFormatter.toSignInvestAmountStr(nowInvest.subtract(invest), marketId) + " (" + coinFormatter.toSignPercentStr(nowInvest, invest) + ")\n");
                message.append("\n");
                break;
            default:
                throw new InvalidUserLanguageException();
        }

        return message.toString();
    }

    public static String calcResultInBTC(BigDecimal invest, BigDecimal coinCount, BigDecimal averageCoinPrice, BigDecimal currentPrice, BigDecimal currentBTC, UserDto user, CoinFormatter coinFormatter) {
        StringBuilder message = new StringBuilder();
        Market market = user.getMarket();
        Language language = user.getLanguage();
        String date = DateTimeUtil.toDateTimeString(LocalDateTime.now(), user.getTimeDifference());

        BigDecimal coinMoney = currentPrice.multiply(currentBTC);
        BigDecimal nowInvest = coinMoney.multiply(coinCount);

        switch (language) {
            case KR:
                message.append("현재 시각  : " + date + "\n");
                message.append("\n");
                message.append("코인개수 : " + coinFormatter.toCoinCntStr(coinCount, language) + "\n");
                message.append("평균단가 : " + coinFormatter.toMoneyStr(averageCoinPrice, market) + "  [" + coinFormatter.toBTCStr(averageCoinPrice.divide(currentBTC, 10, RoundingMode.HALF_UP)) + "]\n");
                message.append("현재단가 : " + coinFormatter.toMoneyStr(coinMoney, market) + " [" + coinFormatter.toBTCStr(currentPrice) + "]\n");
                message.append("---------------------\n");
                message.append("투자금액 : " + coinFormatter.toInvestAmountStr(invest, market) + "\n");
                message.append("현재금액 : " + coinFormatter.toInvestAmountStr(nowInvest, market) + "\n");
                message.append("손익금액 : " + coinFormatter.toSignInvestAmountStr(nowInvest.subtract(invest), market) + " (" + coinFormatter.toSignPercentStr(nowInvest, invest) + ")\n");
                message.append("\n");
                break;
            case EN:
                message.append("Current Time  : " + date + "\n");
                message.append("\n");
                message.append("The number of coins : " + coinFormatter.toCoinCntStr(coinCount, language) + "\n");
                message.append("Average Coin Price : " + coinFormatter.toMoneyStr(averageCoinPrice, market) + "  [ " + coinFormatter.toBTCStr(averageCoinPrice.divide(currentBTC, 10, RoundingMode.HALF_UP)) + "]\n");
                message.append("Current Coin Price : " + coinFormatter.toMoneyStr(coinMoney, market) + " [ " + coinFormatter.toBTCStr(currentPrice) + "]\n");
                message.append("---------------------\n");
                message.append("Investment Amount : " + coinFormatter.toInvestAmountStr(invest, market) + "\n");
                message.append("Curernt Amount : " + coinFormatter.toInvestAmountStr(nowInvest, market) + "\n");
                message.append("Profit and loss : " + coinFormatter.toSignInvestAmountStr(currentPrice.multiply(currentBTC.multiply(coinCount).subtract(invest)), market) + " (" + coinFormatter.toSignPercentStr(nowInvest, invest) + ")\n");
                message.append("\n");
                break;
            default:
                throw new InvalidUserLanguageException();
        }

        return message.toString();
    }
}
