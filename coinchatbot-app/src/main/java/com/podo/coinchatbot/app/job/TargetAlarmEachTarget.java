package com.podo.coinchatbot.app.job;


import com.podo.coinchatbot.app.domain.dto.UserDto;
import com.podo.coinchatbot.app.domain.dto.UserTargetAlarmDto;
import com.podo.coinchatbot.app.domain.service.UserService;
import com.podo.coinchatbot.app.domain.service.UserTargetAlarmService;
import com.podo.coinchatbot.app.telegram.CoinFormatter;
import com.podo.coinchatbot.app.telegram.TelegramMessageSender;
import com.podo.coinchatbot.app.telegram.exception.InvalidUserLanguageException;
import com.podo.coinchatbot.app.telegram.model.MessageVo;
import com.podo.coinchatbot.app.telegram.model.SendMessageVo;
import com.podo.coinchatbot.core.Language;
import com.podo.coinchatbot.core.Market;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class TargetAlarmEachTarget {

    private final UserTargetAlarmService userTargetAlarmService;
    private final UserService userService;

    @Transactional
    public void alarmEachTarget(Market market, BigDecimal currentPrice, CoinFormatter coinFormatter, TelegramMessageSender telegramMessageSender, UserTargetAlarmDto target) {
        BigDecimal targetPrice = target.getTargetPrice();
        UserDto user = userService.findById(target.getUserId());
        Integer telegramId = user.getTelegramId();
        Long chatId = user.getChatId();
        Language language = user.getLanguage();

        TargetAlarmEachTargetContext.put("telegramId", telegramId.toString());
        TargetAlarmEachTargetContext.put("chatId", chatId.toString());
        TargetAlarmEachTargetContext.put("targetFocus", target.getTargetFocus());
        TargetAlarmEachTargetContext.put("targetPrice", targetPrice);
        TargetAlarmEachTargetContext.put("currentPrice", currentPrice);

        String message = msgTargetPriceNotify(currentPrice, targetPrice, market, language, coinFormatter);
        telegramMessageSender.sendTargetAlarm(SendMessageVo.create(new MessageVo(telegramId, chatId), message, null));

        userTargetAlarmService.deleteById(target.getId());
        telegramMessageSender.sendTargetAlarm(SendMessageVo.create(new MessageVo(telegramId, chatId), msgTargetPriceDeleted(language), null));
    }

    private String msgTargetPriceNotify(BigDecimal currentValue, BigDecimal targetPrice, Market market, Language language, CoinFormatter coinFormatter) {
        StringBuilder message = new StringBuilder();
        switch (language) {
            case KR:
                message.append("목표가격에 도달하였습니다!\n");
                message.append("목표가격 : " + coinFormatter.toMoneyStr(targetPrice, market) + "\n");
                message.append("현재가격 : " + coinFormatter.toMoneyStr(currentValue, market) + "\n");
                break;
            case EN:
                message.append("Target price reached!\n");
                message.append("Traget Price : " + coinFormatter.toMoneyStr(targetPrice, market) + "\n");
                message.append("Current Price : " + coinFormatter.toMoneyStr(currentValue, market) + "\n");
                break;
            default:
                throw new InvalidUserLanguageException();
        }

        return message.toString();
    }

    private String msgTargetPriceDeleted(Language language) {
        switch (language) {
            case KR:
                return "해당 목표가격이 삭제되었습니다.\n";
            case EN:
                return "The Target been deleted.\n";
            default:
                throw new InvalidUserLanguageException();
        }
    }
}

