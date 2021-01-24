package com.podo.coinchatbot.app.telegram.menu.handler;

import com.podo.coinchatbot.core.Coin;
import com.podo.coinchatbot.core.Language;
import com.podo.coinchatbot.core.Market;
import com.podo.coinchatbot.app.client.CoinEndpointerUtil;
import com.podo.coinchatbot.app.client.model.CoinResponse;
import com.podo.coinchatbot.app.telegram.keyboard.Keyboard;
import com.podo.coinchatbot.app.telegram.TelegramMessageSender;
import com.podo.coinchatbot.app.client.CoinEndpointer;
import com.podo.coinchatbot.app.model.CoinMeta;
import com.podo.coinchatbot.app.domain.service.UserTargetAlarmService;
import com.podo.coinchatbot.app.domain.service.UserService;
import com.podo.coinchatbot.app.telegram.exception.InvalidTargetAlarmAddFormatException;
import com.podo.coinchatbot.app.telegram.menu.AbstractMenuHandler;
import com.podo.coinchatbot.app.telegram.message.CommonMessage;
import com.podo.coinchatbot.app.model.Menu;
import com.podo.coinchatbot.app.telegram.model.MessageVo;
import com.podo.coinchatbot.app.telegram.model.SendMessageVo;
import com.podo.coinchatbot.app.model.TargetFocus;
import com.podo.coinchatbot.app.domain.dto.UserDto;
import com.podo.coinchatbot.app.domain.dto.UserTargetAlarmInsertDto;
import com.podo.coinchatbot.app.telegram.CoinFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
@RequiredArgsConstructor
public class TargetAlarmAddHandler extends AbstractMenuHandler {

    private final UserService userService;
    private final UserTargetAlarmService userTargetAlarmService;
    private final CoinEndpointer coinEndpointer;

    @Override
    public Menu getHandleMenu() {
        return Menu.TARGET_ALARM_ADD;
    }

    @Override
    @Transactional
    public void handle(MessageVo messageVo, Coin coin, CoinMeta coinMeta, UserDto user, String messageText, TelegramMessageSender sender) {
        StringBuilder message = new StringBuilder();
        Long userId = user.getId();
        Language language = user.getLanguage();
        Market market = user.getMarket();
        CoinFormatter coinFormatter = coinMeta.getCoinFormatter();

        CoinResponse coinResponse = CoinEndpointerUtil.getCoin(coinEndpointer, coin, market);

        BigDecimal currentPrice = coinResponse.getCurrentPrice();
        if (coinMeta.isBTCMarket(market)) {
            currentPrice = CoinEndpointerUtil.btcToMoney(coinEndpointer, market, currentPrice);
        }

        BigDecimal targetAlarmPrice = validateMessageText(messageText.trim(), currentPrice, language);

        message.append(messageTargetAlarmAddResult(targetAlarmPrice, currentPrice, market, language, coinFormatter));

        UserTargetAlarmInsertDto targetAlarm = UserTargetAlarmInsertDto.builder()
                .userId(userId)
                .targetPrice(coinFormatter.formatPrice(targetAlarmPrice, market))
                .targetFocus(targetAlarmPrice.compareTo(currentPrice) >= 0 ? TargetFocus.UP : TargetFocus.DOWN)
                .build();

        userTargetAlarmService.insertNew(targetAlarm);

        message.append(CommonMessage.toMain(language));

        sender.sendMessage(SendMessageVo.create(messageVo, message.toString(), Keyboard.mainKeyboard(language)));

        userService.updateMenuStatus(userId, Menu.MAIN);
    }

    private BigDecimal validateMessageText(String messageText, BigDecimal currentPrice, Language language) {
        // 사용자 입력이 숫자 일때
        if (messageText.matches("^\\d*(\\.?\\d*)$")) {
            return BigDecimal.valueOf(Double.parseDouble(messageText));
        }

        // 사용자 입력이 퍼센트 일때
        if (messageText.matches("^[+-]?\\d*(\\.?\\d*)%$")) {

            if (messageText.equals("%")) {
                messageText = "0%";
            }

            messageText = messageText.replace("%", "");
            BigDecimal percent = BigDecimal.valueOf(Double.parseDouble(messageText)).divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP);

            if (percent.compareTo(BigDecimal.ZERO) >= 0) {
                return currentPrice.add(currentPrice.multiply(percent));
            }

            if (percent.compareTo(BigDecimal.ZERO) < 0 && percent.compareTo(BigDecimal.valueOf(-100)) >= 0) {
                return currentPrice.subtract(currentPrice.multiply(percent).multiply(BigDecimal.valueOf(-1)));
            }

        }

        //그 외의 경우
        StringBuilder message = new StringBuilder();
        switch (language) {
            case KR:
                message.append("목표가격 백분율을 -100% 또는 0 이하로 설정 할 수 없습니다.");
                break;
            case EN:
                message.append("You can not set the target price percentage below -100%.\n");
                break;
        }

        throw new InvalidTargetAlarmAddFormatException(message.toString());
    }

    public String messageTargetAlarmAddResult(BigDecimal targetPrice, BigDecimal currentValue, Market market, Language language, CoinFormatter coinFormatter) {
        StringBuilder message = new StringBuilder();
        switch (language) {
            case KR:
                message.append("목표가격 " + coinFormatter.toMoneyStr(targetPrice, market) + "으로 설정되었습니다.\n");
                message.append("------------------------\n");
                message.append("목표가격 : " + coinFormatter.toMoneyStr(targetPrice, market) + "\n");
                message.append("현재가격 : " + coinFormatter.toMoneyStr(currentValue, market) + "\n");
                message.append("가격차이 : " + coinFormatter.toSignMoneyStr(targetPrice.subtract(currentValue), market) + "(" + coinFormatter.toSignPercentStr(targetPrice, currentValue) + " )\n");
                break;
            case EN:
                message.append("The target price is set at " + coinFormatter.toMoneyStr(targetPrice, market) + ".\n");
                message.append("------------------------\n");
                message.append("Target Price       : " + coinFormatter.toMoneyStr(targetPrice, market) + "\n");
                message.append("Current Price      : " + coinFormatter.toMoneyStr(currentValue, market) + "\n");
                message.append("Price difference : " + coinFormatter.toSignMoneyStr(targetPrice.subtract(currentValue), market) + " (" + coinFormatter.toSignPercentStr(targetPrice, currentValue) + " )\n");
                break;
        }
        return message.toString();
    }


}
