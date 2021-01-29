package com.podo.coinchatbot.app.telegram.menu.handler;

import com.podo.coinchatbot.app.domain.dto.UserDto;
import com.podo.coinchatbot.app.domain.service.UserService;
import com.podo.coinchatbot.app.model.CoinMeta;
import com.podo.coinchatbot.app.model.Menu;
import com.podo.coinchatbot.app.telegram.TelegramMessageSender;
import com.podo.coinchatbot.app.telegram.command.TimeloopAlarmCommand;
import com.podo.coinchatbot.app.telegram.exception.InvalidUserLanguageException;
import com.podo.coinchatbot.app.telegram.keyboard.Keyboard;
import com.podo.coinchatbot.app.telegram.menu.AbstractMenuHandler;
import com.podo.coinchatbot.app.telegram.message.CommonMessage;
import com.podo.coinchatbot.app.telegram.model.MessageVo;
import com.podo.coinchatbot.app.telegram.model.SendMessageVo;
import com.podo.coinchatbot.core.Coin;
import com.podo.coinchatbot.core.Language;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TimeloopAlarmConfigHandler extends AbstractMenuHandler {

    private final UserService userService;

    @Override
    public Menu getHandleMenu() {
        return Menu.TIMELOOP_ALARM_CONFIG;
    }

    @Override
    public void handle(MessageVo messageVo, Coin coin, CoinMeta coinMeta, UserDto user, String messageText, TelegramMessageSender sender) {
        Language language = user.getLanguage();
        Long userId = user.getId();
        StringBuilder message = new StringBuilder();

        TimeloopAlarmCommand command = TimeloopAlarmCommand.from(language, messageText);
        Integer commandValue = command.getValue();

        if (command.equals(TimeloopAlarmCommand.OUT)) {
            message.append(messageTimeloopOut(language));
        } else if (command.equals(TimeloopAlarmCommand.OFF)) {
            userService.updateTimeLoop(userId, commandValue);
            message.append(messageTimeloopAlarmStop(language));
        } else {
            userService.updateTimeLoop(userId, commandValue);
            message.append(messageTimeloopAlarmConfigSuccess(language, commandValue));
        }


        message.append(CommonMessage.toMain(language));

        sender.sendMessage(SendMessageVo.create(messageVo, message.toString(), Keyboard.mainKeyboard(language)));

        userService.updateMenuStatus(userId, Menu.MAIN);
    }

    public String messageTimeloopAlarmConfigSuccess(Language language, Integer timeloop) {
        switch (language) {
            case KR:
                return "시간 알림이 " + timeloop + " 시간 주기로 전송됩니다.\n";
            case EN:
                return "Hourly notifications are sent every " + timeloop + " hours.\n";
            default:
                throw new InvalidUserLanguageException();
        }
    }

    public String messageTimeloopOut(Language language) {
        switch (language) {
            case KR:
                return "시간알림 주기가 설정 되지 않았습니다.\n";
            case EN:
                return "Hourly notifications cycle is not set.\n";
            default:
                throw new InvalidUserLanguageException();
        }
    }


    public String messageTimeloopAlarmStop(Language language) {
        switch (language) {
            case KR:
                return "시간 알림이 전송되지 않습니다.\n";
            case EN:
                return "Hourly notifications are not sent.\n";
            default:
                throw new InvalidUserLanguageException();
        }
    }
}
