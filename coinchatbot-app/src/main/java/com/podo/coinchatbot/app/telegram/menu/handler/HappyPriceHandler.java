package com.podo.coinchatbot.app.telegram.menu.handler;

import com.podo.coinchatbot.app.domain.dto.UserDto;
import com.podo.coinchatbot.app.domain.service.UserService;
import com.podo.coinchatbot.app.model.CoinMeta;
import com.podo.coinchatbot.app.model.Menu;
import com.podo.coinchatbot.app.telegram.CoinFormatter;
import com.podo.coinchatbot.app.telegram.TelegramMessageSender;
import com.podo.coinchatbot.app.telegram.exception.InvalidUserLanguageException;
import com.podo.coinchatbot.app.telegram.exception.UserInvalidInputException;
import com.podo.coinchatbot.app.telegram.keyboard.Keyboard;
import com.podo.coinchatbot.app.telegram.menu.AbstractMenuHandler;
import com.podo.coinchatbot.app.telegram.message.CommonMessage;
import com.podo.coinchatbot.app.telegram.model.MessageVo;
import com.podo.coinchatbot.app.telegram.model.SendMessageVo;
import com.podo.coinchatbot.core.Coin;
import com.podo.coinchatbot.core.Language;
import com.podo.coinchatbot.core.Market;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
@RequiredArgsConstructor
public class HappyPriceHandler extends AbstractMenuHandler {

    private final UserService userService;

    @Override
    public Menu getHandleMenu() {
        return Menu.HAPPY_COIN_PRICE;
    }

    @Override
    @Transactional
    public void handle(MessageVo messageVo, Coin coin, CoinMeta coinMeta, UserDto user, String messageText, TelegramMessageSender sender) {
        StringBuilder message = new StringBuilder();
        Long userId = user.getId();
        Language language = user.getLanguage();

        BigDecimal happyPrice = extractHappyCoinPrice(messageText);
        BigDecimal invest = user.getInvest();
        BigDecimal coinCnt = user.getCoinCount();
        Market market = user.getMarket();

        message.append(msgHappyLineResult(invest, coinCnt, happyPrice, market, language, coinMeta.getCoinFormatter()));
        message.append(CommonMessage.toMain(language));

        sender.sendMessage(SendMessageVo.create(messageVo, message.toString(), Keyboard.mainKeyboard(language)));

        userService.updateMenuStatus(userId, Menu.MAIN);
    }

    private BigDecimal extractHappyCoinPrice(String messageText) {
        try {
            return BigDecimal.valueOf(Double.parseDouble(messageText));
        } catch (NumberFormatException e) {
            throw new UserInvalidInputException("희망코인가격이 올바르지 않습니다.");
        }
    }

    public String msgHappyLineResult(BigDecimal invest, BigDecimal coinCount, BigDecimal happyCoinPrice, Market market, Language language, CoinFormatter coinFormatter) {
        StringBuilder message = new StringBuilder();
        BigDecimal averageCoinPrice = invest.divide(coinCount, 10, RoundingMode.HALF_UP);

        switch (language) {
            case KR:
                message.append("코인개수 : " + coinFormatter.toCoinCntStr(coinCount, language) + "\n");
                message.append("평균단가 : " + coinFormatter.toMoneyStr(averageCoinPrice, market) + "\n");
                message.append("희망단가 : " + coinFormatter.toMoneyStr(happyCoinPrice, market) + "\n");
                message.append("---------------------\n");
                message.append("투자금액 : " + coinFormatter.toInvestAmountStr(invest, market) + "\n");
                message.append("희망금액 : " + coinFormatter.toInvestAmountStr(happyCoinPrice.multiply(coinCount), market) + "\n");
                message.append("손익금액 : " + coinFormatter.toSignInvestAmountStr(happyCoinPrice.multiply(coinCount).subtract(invest), market) + " (" + coinFormatter.toSignPercentStr(happyCoinPrice.multiply(coinCount), invest) + ")\n");
                break;
            case EN:
                message.append("The number of coins : " + coinFormatter.toCoinCntStr(coinCount, language) + "\n");
                message.append("Average Coin Price  : " + coinFormatter.toMoneyStr(averageCoinPrice, market) + "\n");
                message.append("Desired Coin Price  : " + coinFormatter.toMoneyStr(happyCoinPrice, market) + "\n");
                message.append("---------------------\n");
                message.append("Investment Amount : " + coinFormatter.toInvestAmountStr(invest, market) + "\n");
                message.append("Desired Amount : " + coinFormatter.toInvestAmountStr(happyCoinPrice.multiply(coinCount), market) + "\n");
                message.append("Profit and loss : " + coinFormatter.toSignInvestAmountStr(happyCoinPrice.multiply(coinCount).subtract(invest), market) + "(" + coinFormatter.toSignPercentStr(happyCoinPrice.multiply(coinCount), invest) + ")\n");
                break;
            default:
                throw new InvalidUserLanguageException();
        }

        return message.toString();
    }

}
