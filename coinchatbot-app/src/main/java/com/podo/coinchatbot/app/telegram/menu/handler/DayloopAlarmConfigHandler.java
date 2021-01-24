package com.podo.coinchatbot.app.telegram.menu.handler;

import com.podo.coinchatbot.core.Coin;
import com.podo.coinchatbot.core.Language;
import com.podo.coinchatbot.app.telegram.keyboard.Keyboard;
import com.podo.coinchatbot.app.telegram.TelegramMessageSender;
import com.podo.coinchatbot.app.model.CoinMeta;
import com.podo.coinchatbot.app.telegram.command.DayloopAlarmCommand;
import com.podo.coinchatbot.app.domain.service.UserService;
import com.podo.coinchatbot.app.telegram.exception.InvalidUserLanguageException;
import com.podo.coinchatbot.app.telegram.menu.AbstractMenuHandler;
import com.podo.coinchatbot.app.telegram.message.CommonMessage;
import com.podo.coinchatbot.app.model.Menu;
import com.podo.coinchatbot.app.telegram.model.MessageVo;
import com.podo.coinchatbot.app.telegram.model.SendMessageVo;
import com.podo.coinchatbot.app.domain.dto.UserDto;
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

        if (command.equals(DayloopAlarmCommand.OUT)) {
            message.append(messageDayloopOut(language));
        }

        userService.updateDayLoop(userId, command.getValue());

        if (command.equals(DayloopAlarmCommand.OFF)) {
            message.append(messageDayloopStop(language));
        } else {
            message.append(messageDayloopConfig(language,  command.getValue()));
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
