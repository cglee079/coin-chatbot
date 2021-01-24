package com.podo.coinchatbot.app.telegram.menu.handler;

import com.podo.coinchatbot.app.telegram.exception.UserInvalidInputException;
import com.podo.coinchatbot.core.Coin;
import com.podo.coinchatbot.core.Language;
import com.podo.coinchatbot.core.Market;
import com.podo.coinchatbot.app.telegram.keyboard.Keyboard;
import com.podo.coinchatbot.app.telegram.TelegramMessageSender;
import com.podo.coinchatbot.app.model.CoinMeta;
import com.podo.coinchatbot.app.telegram.command.TargetAlarmDeleteCommand;
import com.podo.coinchatbot.app.domain.service.UserTargetAlarmService;
import com.podo.coinchatbot.app.domain.service.UserService;
import com.podo.coinchatbot.app.telegram.exception.InvalidUserLanguageException;
import com.podo.coinchatbot.app.telegram.menu.AbstractMenuHandler;
import com.podo.coinchatbot.app.telegram.message.CommonMessage;
import com.podo.coinchatbot.app.model.Menu;
import com.podo.coinchatbot.app.telegram.model.MessageVo;
import com.podo.coinchatbot.app.telegram.model.SendMessageVo;
import com.podo.coinchatbot.app.domain.dto.UserDto;
import com.podo.coinchatbot.app.telegram.CoinFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class TargetAlarmDeleteHandler extends AbstractMenuHandler {

    private final UserService userService;
    private final UserTargetAlarmService userTargetAlarmService;

    @Override
    public Menu getHandleMenu() {
        return Menu.TARGET_ALARM_DELETE;
    }

    @Override
    @Transactional
    public void handle(MessageVo messageVo, Coin coin, CoinMeta coinMeta, UserDto user, String messageText, TelegramMessageSender sender) {
        StringBuilder message = new StringBuilder();

        Long userId = user.getId();
        Language language = user.getLanguage();
        Market market = user.getMarket();
        TargetAlarmDeleteCommand command = TargetAlarmDeleteCommand.from(language, messageText);

        switch (command) {
            case OUT:
                message.append(CommonMessage.toMain(language));
                break;
            default:
                BigDecimal targetAlarmPrice = extractTargetPrice(messageText);
                userTargetAlarmService.deleteByUserIdAndPrice(userId, targetAlarmPrice);
                message.append(msgCompleteDelTarget(targetAlarmPrice, market, language, coinMeta.getCoinFormatter()));
                message.append(CommonMessage.toMain(language));
                break;
        }

        sender.send(SendMessageVo.create(messageVo, message.toString(), Keyboard.mainKeyboard(language)));
        userService.updateMenuStatus(userId, Menu.MAIN);
    }

    private BigDecimal extractTargetPrice(String messageText) {
        try {
            return BigDecimal.valueOf(Double.parseDouble(messageText.replaceAll("[^-?0-9-?.]+", "")));
        }catch (NumberFormatException e){
            throw new UserInvalidInputException("목표 알림을 찾지 못하였습니다.");
        }
    }

    public String msgCompleteDelTarget(BigDecimal targetAlarmPrice, Market market, Language language, CoinFormatter coinFormatter) {
        switch (language) {
            case KR:
                return coinFormatter.toMoneyStr(targetAlarmPrice, market) + " 목표가격이 삭제 되었습니다 \n";
            case EN:
                return coinFormatter.toMoneyStr(targetAlarmPrice, market) + " Target Deleted\n";
            default:
                throw new InvalidUserLanguageException();
        }
    }


}
