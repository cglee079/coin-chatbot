package com.podo.coinchatbot.telegram.menu.handler;

import com.podo.coinchatbot.app.domain.dto.UserDto;
import com.podo.coinchatbot.app.domain.service.UserService;
import com.podo.coinchatbot.app.model.CoinMeta;
import com.podo.coinchatbot.app.model.Menu;
import com.podo.coinchatbot.telegram.TelegramMessageSender;
import com.podo.coinchatbot.telegram.command.DayloopAlarmCommand;
import com.podo.coinchatbot.telegram.exception.InvalidUserLanguageException;
import com.podo.coinchatbot.telegram.keyboard.Keyboard;
import com.podo.coinchatbot.telegram.menu.AbstractMenuHandler;
import com.podo.coinchatbot.telegram.message.CommonMessage;
import com.podo.coinchatbot.telegram.model.MessageVo;
import com.podo.coinchatbot.telegram.model.SendMessageVo;
import com.podo.coinchatbot.core.Coin;
import com.podo.coinchatbot.core.Language;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DayloopAlarmConfigHandler extends AbstractMenuHandler {

    private final UserService userService;

    @Override
    public Menu getHandleMenu() {
        return Menu.DAYLOOP_ALARM_CONFIG;
    }

    @Override
    public void handle(MessageVo messageVo, Coin coin, CoinMeta coinMeta, UserDto user, String messageText, TelegramMessageSender sender) {
        Long userId = user.getId();
        Language language = user.getLanguage();
        StringBuilder message = new StringBuilder();

        DayloopAlarmCommand command = DayloopAlarmCommand.from(language, messageText);
        Integer commandValue = command.getValue();

        if (command.equals(DayloopAlarmCommand.OUT)) {
            message.append(messageDayloopOut(language));
        } else if (command.equals(DayloopAlarmCommand.OFF)) {
            userService.updateDayLoop(userId, commandValue);
            message.append(messageDayloopStop(language));
        } else {
            userService.updateDayLoop(userId, commandValue);
            message.append(messageDayloopConfig(language, commandValue));
        }


        message.append(CommonMessage.toMain(language));

        sender.sendMessage(SendMessageVo.create(messageVo, message.toString(), Keyboard.mainKeyboard(language)));

        userService.updateMenuStatus(userId, Menu.MAIN);
    }

    public String messageDayloopStop(Language language) {
        switch (language) {
            case KR:
                return "일일 알림이 전송되지 않습니다.\n";
            case EN:
                return "Daily notifications are not sent.\n";
            default:
                throw new InvalidUserLanguageException();
        }
    }

    public String messageDayloopOut(Language language) {
        switch (language) {
            case KR:
                return "일일 알림 주기가 설정 되지 않았습니다.\n";
            case EN:
                return "Daily notifications cycle is not set.\n";
            default:
                throw new InvalidUserLanguageException();
        }
    }

    public String messageDayloopConfig(Language language, Integer dayloop) {
        switch (language) {
            case KR:
                return "일일 알림이 매 " + dayloop + " 일주기로 전송됩니다.\n";
            case EN:
                return "Daily notifications are sent every " + dayloop + " days.\n";
            default:
                throw new InvalidUserLanguageException();
        }
    }
}
