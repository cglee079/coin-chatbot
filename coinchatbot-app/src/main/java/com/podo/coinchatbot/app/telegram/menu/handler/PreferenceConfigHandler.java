package com.podo.coinchatbot.app.telegram.menu.handler;

import com.podo.coinchatbot.core.Coin;
import com.podo.coinchatbot.core.Language;
import com.podo.coinchatbot.app.telegram.keyboard.Keyboard;
import com.podo.coinchatbot.app.telegram.TelegramMessageSender;
import com.podo.coinchatbot.app.model.CoinMeta;
import com.podo.coinchatbot.app.telegram.command.PreferencCommand;
import com.podo.coinchatbot.app.domain.service.UserService;
import com.podo.coinchatbot.app.telegram.menu.AbstractMenuHandler;
import com.podo.coinchatbot.app.telegram.message.CommonMessage;
import com.podo.coinchatbot.app.model.Menu;
import com.podo.coinchatbot.app.telegram.model.MessageVo;
import com.podo.coinchatbot.app.telegram.model.SendMessageVo;
import com.podo.coinchatbot.app.domain.dto.UserDto;
import com.podo.coinchatbot.app.util.DateTimeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class PreferenceConfigHandler extends AbstractMenuHandler {

    private final UserService userService;

    @Override
    public Menu getHandleMenu() {
        return Menu.PREFERENCE;
    }

    @Override
    @Transactional
    public void handle(MessageVo messageVo, Coin coin, CoinMeta coinMeta, UserDto user, String messageText, TelegramMessageSender sender) {
        Menu menuStatus = Menu.MAIN;
        Long userId = user.getId();
        Language language = user.getLanguage();

        PreferencCommand command = PreferencCommand.from(language, messageText);

        switch (command) {
            case LANGUAGE_CONFIG: //언어 설정
                sender.send(SendMessageVo.create(messageVo, explainLanguageConfig(), Keyboard.languageConfigKeyboard(language)));
                menuStatus = Menu.PREFERENC_LANGUAGE;
                break;
            case LOCALTIME_CONFIG: //시차 조절
                LocalDateTime now = LocalDateTime.now();
                sender.send(SendMessageVo.create(messageVo, explainLocalTimeConfig(now), Keyboard.defaultKeyboard()));
                menuStatus = Menu.PREFERENCE_LOCALTIME;
                break;
            case OUT:
            default:
                sender.send(SendMessageVo.create(messageVo, CommonMessage.toMain(language), Keyboard.mainKeyboard(language)));
                break;
        }

        userService.updateMenuStatus(userId, menuStatus);
    }


    public String explainLanguageConfig() {
        return "Please select a language.";
    }

    public String explainLocalTimeConfig(LocalDateTime now) {
        StringBuilder message = new StringBuilder();
        message.append("한국분이시라면 별도의 시차조절을 필요로하지 않습니다.^^  <- for korean\n");
        message.append("\n");
        message.append("Please enter the current time for accurate time notification.\n");
        message.append("because the time differs for each country.\n");
        message.append("\n");
        message.append("* Please enter in the following format.\n");
        message.append("* if you entered 0, time adjust initialized.\n");
        message.append("* example) 0 : init time adjust\n");
        message.append("* example) " + DateTimeUtil.toDateString(now.minusDays(1L)) + " 23:00 \n");
        message.append("* example) " + DateTimeUtil.toDateString(now) + " 00:33 \n");
        message.append("* example) " + DateTimeUtil.toDateString(now) + " 14:30 \n");
        message.append("\n");
        message.append("\n");
        message.append("# To return to main, enter a character.\n");
        return message.toString();
    }
}
