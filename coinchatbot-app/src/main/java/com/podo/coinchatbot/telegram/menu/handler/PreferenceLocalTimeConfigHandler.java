package com.podo.coinchatbot.telegram.menu.handler;

import com.podo.coinchatbot.app.domain.dto.UserDto;
import com.podo.coinchatbot.app.domain.service.UserService;
import com.podo.coinchatbot.app.model.CoinMeta;
import com.podo.coinchatbot.app.model.Menu;
import com.podo.coinchatbot.telegram.TelegramMessageSender;
import com.podo.coinchatbot.telegram.keyboard.Keyboard;
import com.podo.coinchatbot.telegram.menu.AbstractMenuHandler;
import com.podo.coinchatbot.telegram.message.CommonMessage;
import com.podo.coinchatbot.telegram.model.MessageVo;
import com.podo.coinchatbot.telegram.model.SendMessageVo;
import com.podo.coinchatbot.util.DateTimeUtil;
import com.podo.coinchatbot.core.Coin;
import com.podo.coinchatbot.core.Language;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class PreferenceLocalTimeConfigHandler extends AbstractMenuHandler {

    private final UserService userService;

    @Override
    public Menu getHandleMenu() {
        return Menu.PREFERENCE_LOCALTIME;
    }

    @Override
    @Transactional
    public void handle(MessageVo messageVo, Coin coin, CoinMeta coinMeta, UserDto user, String messageText, TelegramMessageSender sender) {
        Menu menuStatus = Menu.MAIN;
        Long userId = user.getId();
        Language language = user.getLanguage();

        StringBuilder message = new StringBuilder();
        LocalDateTime now = LocalDateTime.now();


        if (messageText.equals("0")) {
            userService.updateTimeDifference(userId, 0L);
            message.append(messageLocalTimeConfigSuccess(now));
        } else {
            LocalDateTime enteredDate = extractDateTime(messageText);
            Long timeDifference = DateTimeUtil.dateTimeToLong(enteredDate) - DateTimeUtil.dateTimeToLong(now);

            userService.updateTimeDifference(userId, timeDifference);

            message.append(messageLocalTimeConfigSuccess(enteredDate));
        }


        message.append(CommonMessage.toMain(language));

        sender.sendMessage(SendMessageVo.create(messageVo, message.toString(), Keyboard.mainKeyboard(language)));

        userService.updateMenuStatus(userId, menuStatus);
    }

    private LocalDateTime extractDateTime(String enteredDateStr) {
        return LocalDateTime.parse(enteredDateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    public String messageLocalTimeConfigSuccess(LocalDateTime localDateTime) {
        StringBuilder message = new StringBuilder();
        message.append("Time adjustment succeeded.\n");
        message.append("Current Time : " + DateTimeUtil.toDateTimeString(localDateTime) + "\n");
        return message.toString();
    }

}
