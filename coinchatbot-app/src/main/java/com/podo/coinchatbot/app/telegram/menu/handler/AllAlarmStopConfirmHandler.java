package com.podo.coinchatbot.app.telegram.menu.handler;

import com.podo.coinchatbot.core.Coin;
import com.podo.coinchatbot.core.Language;
import com.podo.coinchatbot.app.telegram.keyboard.Keyboard;
import com.podo.coinchatbot.app.telegram.TelegramMessageSender;
import com.podo.coinchatbot.app.model.CoinMeta;
import com.podo.coinchatbot.app.telegram.command.StopAllAlarmConfirmCommand;
import com.podo.coinchatbot.app.domain.service.UserTargetAlarmService;
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
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class AllAlarmStopConfirmHandler extends AbstractMenuHandler {

    private final UserService userService;
    private final UserTargetAlarmService userTargetAlarmService;

    @Override
    public Menu getHandleMenu() {
        return Menu.STOP_ALL_ALARM;
    }

    @Override
    @Transactional
    public void handle(MessageVo messageVo, Coin coin, CoinMeta coinMeta, UserDto user, String messageText, TelegramMessageSender sender) {
        Long userId = user.getId();
        Language language = user.getLanguage();

        StringBuilder message = new StringBuilder();

        StopAllAlarmConfirmCommand command = StopAllAlarmConfirmCommand.from(language, messageText);
        switch (command) {
            case YES:
                userService.stopAllAlarm(userId);
                userTargetAlarmService.deleteByUserId(userId);
                message.append(messageAllAlarmStopped(language));
                break;
            case NO:
            default:
                message.append("\n");
                break;
        }

        message.append(CommonMessage.toMain(language));

        sender.sendMessage(SendMessageVo.create(messageVo, message.toString(), Keyboard.mainKeyboard(language)));
        userService.updateMenuStatus(userId, Menu.MAIN);
    }


    public String messageAllAlarmStopped(Language language) {
        switch (language) {
            case KR:
                return "모든알림(시간알림, 일일알림, 목표가격알림)이 중지되었습니다.\n";
            case EN:
                return "All notifications (daily, hourly , target price notifications ) be stopped.\n";
            default:
                throw new InvalidUserLanguageException();
        }
    }


}
