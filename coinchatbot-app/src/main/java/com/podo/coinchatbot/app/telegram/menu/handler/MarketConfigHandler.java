package com.podo.coinchatbot.app.telegram.menu.handler;

import com.podo.coinchatbot.app.client.ExchangeHolder;
import com.podo.coinchatbot.app.domain.dto.UserDto;
import com.podo.coinchatbot.app.domain.dto.UserTargetAlarmDto;
import com.podo.coinchatbot.app.domain.service.UserService;
import com.podo.coinchatbot.app.domain.service.UserTargetAlarmService;
import com.podo.coinchatbot.app.model.CoinMeta;
import com.podo.coinchatbot.app.model.Menu;
import com.podo.coinchatbot.app.telegram.CoinFormatter;
import com.podo.coinchatbot.app.telegram.TelegramMessageSender;
import com.podo.coinchatbot.app.telegram.command.MarketCommand;
import com.podo.coinchatbot.app.telegram.exception.InvalidUserLanguageException;
import com.podo.coinchatbot.app.telegram.keyboard.Keyboard;
import com.podo.coinchatbot.app.telegram.menu.AbstractMenuHandler;
import com.podo.coinchatbot.app.telegram.message.CommonMessage;
import com.podo.coinchatbot.app.telegram.model.MessageVo;
import com.podo.coinchatbot.app.telegram.model.SendMessageVo;
import com.podo.coinchatbot.app.util.MessageUtil;
import com.podo.coinchatbot.app.util.NumberFormatter;
import com.podo.coinchatbot.core.Coin;
import com.podo.coinchatbot.core.Language;
import com.podo.coinchatbot.core.Market;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MarketConfigHandler extends AbstractMenuHandler {

    private final UserService userService;
    private final ExchangeHolder exchangeHolder;
    private final UserTargetAlarmService userTargetAlarmService;

    @Override
    public Menu getHandleMenu() {
        return Menu.MARKET_CONFIG;
    }

    @Override
    @Transactional
    public void handle(MessageVo messageVo, Coin coin, CoinMeta coinMeta, UserDto user, String messageText, TelegramMessageSender sender) {
        Long userId = user.getId();
        Language language = user.getLanguage();
        Market userMarket = user.getMarket();
        BigDecimal userInvest = user.getInvest();
        CoinFormatter coinFormatter = coinMeta.getCoinFormatter();

        StringBuilder message = new StringBuilder();

        MarketCommand command = MarketCommand.from(language, messageText);

        if (command.equals(MarketCommand.OUT)) {
            message.append(messageMarketNoSet(language));
            message.append(CommonMessage.toMain(language));

            sender.sendMessage(SendMessageVo.create(messageVo, message.toString(), Keyboard.mainKeyboard(language)));
            userService.updateMenuStatus(userId, Menu.MAIN);
            return;
        }

        Market changedMarket = command.getValue();

        BigDecimal changedInvest = userInvest;
        BigDecimal exchangeRate = exchangeHolder.getCurrentExchangeRate();

        // 한화에서 달러 거래소로 변경시 환율에 따른 투자금액, 목표가격 변경
        if (userMarket.isKRW() && changedMarket.isUSD()) {
            changedInvest = userInvest != null ? userInvest.divide(exchangeRate, 10, RoundingMode.HALF_UP) : null;

            List<UserTargetAlarmDto> userTargetAlarms = userTargetAlarmService.findByUserId(userId);

            List<BigDecimal> currentTargetPrices = new ArrayList<>();
            List<BigDecimal> changedTargetPrices = new ArrayList<>();

            for (UserTargetAlarmDto userTargetAlarm : userTargetAlarms) {
                currentTargetPrices.add(userTargetAlarm.getTargetPrice());
                changedTargetPrices.add(userTargetAlarm.getTargetPrice().divide(exchangeRate, 10, RoundingMode.HALF_UP));
                userTargetAlarmService.changeTargetPrice(userTargetAlarm.getId(), coinFormatter.formatPrice(userTargetAlarm.getTargetPrice().divide(exchangeRate, 10, RoundingMode.HALF_UP), changedMarket));
            }

            message.append(messageMarketConfigChangeCurrency(user, coinMeta, changedInvest, currentTargetPrices, changedTargetPrices, changedMarket));

        }

        // 달러에서 한화 거래소로 변경시 환율에 따른 투자금액, 목표가격 변경
        if (userMarket.isUSD() && changedMarket.isKRW()) {
            changedInvest = userInvest != null ? userInvest.multiply(exchangeRate) : null;
            List<UserTargetAlarmDto> userTargetAlarms = userTargetAlarmService.findByUserId(userId);

            List<BigDecimal> currentTargetPrices = new ArrayList<>();
            List<BigDecimal> changedTargetPrices = new ArrayList<>();

            for (UserTargetAlarmDto userTargetAlarm : userTargetAlarms) {
                currentTargetPrices.add(userTargetAlarm.getTargetPrice());
                changedTargetPrices.add(userTargetAlarm.getTargetPrice().multiply(exchangeRate));
                userTargetAlarmService.changeTargetPrice(userTargetAlarm.getId(), coinFormatter.formatPrice(userTargetAlarm.getTargetPrice().multiply(exchangeRate), changedMarket));
            }

            message.append(messageMarketConfigChangeCurrency(user, coinMeta, changedInvest, currentTargetPrices, changedTargetPrices, changedMarket));
        }

        userService.updateInvest(userId, changedInvest);
        userService.updateMarket(userId, changedMarket);

        message.append(CommonMessage.toMain(language));

        sender.sendMessage(SendMessageVo.create(messageVo, message.toString(), Keyboard.mainKeyboard(language)));
        userService.updateMenuStatus(userId, Menu.MAIN);
    }


    public String messageMarketConfigChangeCurrency(UserDto user, CoinMeta coinMeta, BigDecimal changedInvest, List<BigDecimal> currentTargetPrices, List<BigDecimal> changedTargetAlarmPrices, Market changeMarket) {
        StringBuilder message = new StringBuilder();
        Market marketId = user.getMarket();
        Language language = user.getLanguage();
        BigDecimal currentPrice = user.getInvest();
        CoinFormatter coinFormatter = coinMeta.getCoinFormatter();

        switch (language) {
            case KR:
                message.append("변경하신 거래소의 화폐단위가 변경되어,\n");
                message.append("설정하신 투자금액/목표가를 환율에 맞추어 변동하였습니다.\n");
                if (currentPrice != null) {
                    message.append("투자금액 : " + coinFormatter.toInvestAmountStr(currentPrice, marketId) + " -> " + coinFormatter.toInvestAmountStr(changedInvest, changeMarket) + "\n");
                }
                for (int i = 0; i < currentTargetPrices.size(); i++) {
                    message.append("목표가격 #" + NumberFormatter.toNumberStr(i + 1, 2) + ": " + coinFormatter.toMoneyStr(currentTargetPrices.get(i), marketId) + " -> " + coinFormatter.toMoneyStr(changedTargetAlarmPrices.get(i), changeMarket) + "\n");
                }
                break;
            case EN:
                message.append("* The currency unit of the exchange has been changed,\n");
                message.append("the investment amount / target price you set has been changed to match the exchange rate.\n");
                message.append("\n");
                if (currentPrice != null) {
                    message.append("Investment amount : " + coinFormatter.toInvestAmountStr(currentPrice, marketId) + " -> " + coinFormatter.toInvestAmountStr(changedInvest, changeMarket) + "\n");
                }
                for (int i = 0; i < currentTargetPrices.size(); i++) {
                    message.append("Target Price #" + NumberFormatter.toNumberStr(i + 1, 2) + ": " + coinFormatter.toMoneyStr(currentTargetPrices.get(i), marketId) + " -> " + coinFormatter.toMoneyStr(changedTargetAlarmPrices.get(i), changeMarket) + "\n");
                }
                break;
            default:
                throw new InvalidUserLanguageException();
        }

        return message.toString();
    }


    public String msgMarketSet(Market marketId, Language language) {
        StringBuilder msg = new StringBuilder("");
        String marketStr = MessageUtil.toMarketStr(marketId, language);

        switch (language) {
            case KR:
                msg.append("거래소가 " + marketStr + "(으)로 설정되었습니다.\n");
                break;
            case EN:
                msg.append("The exchange has been set up as " + marketStr + ".\n");
                break;
        }
        return msg.toString();
    }

    public String messageMarketNoSet(Language language) {
        switch (language) {
            case KR:
                return "거래소가 설정되지 않았습니다.\n";
            case EN:
                return "The market has not been set up.\n";
            default:
                throw new InvalidUserLanguageException();
        }
    }
}
