package com.podo.coinchatbot.telegram.menu.handler;

import com.podo.coinchatbot.app.domain.dto.UserDto;
import com.podo.coinchatbot.app.domain.service.UserService;
import com.podo.coinchatbot.app.model.CoinMeta;
import com.podo.coinchatbot.app.model.Menu;
import com.podo.coinchatbot.telegram.TelegramMessageSender;
import com.podo.coinchatbot.telegram.command.PrefLangCommand;
import com.podo.coinchatbot.telegram.exception.InvalidUserLanguageException;
import com.podo.coinchatbot.telegram.keyboard.Keyboard;
import com.podo.coinchatbot.telegram.menu.AbstractMenuHandler;
import com.podo.coinchatbot.telegram.message.CommonMessage;
import com.podo.coinchatbot.telegram.message.HelpMessage;
import com.podo.coinchatbot.telegram.model.MessageVo;
import com.podo.coinchatbot.telegram.model.SendMessageVo;
import com.podo.coinchatbot.core.Coin;
import com.podo.coinchatbot.core.Language;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class PreferenceLanguageConfigHandler extends AbstractMenuHandler {

    private final UserService userService;

    @Override
    public Menu getHandleMenu() {
        return Menu.PREFERENC_LANGUAGE;
    }

    @Override
    @Transactional
    public void handle(MessageVo messageVo, Coin coin, CoinMeta coinMeta, UserDto user, String messageText, TelegramMessageSender sender) {
        Long userId = user.getId();
        Language language = user.getLanguage();

        PrefLangCommand command = PrefLangCommand.from(language, messageText);

        switch (command) {
            case SET_KR:
            case SET_EN:
                Language changedLanguage = command.getValue();
                userService.updateLanguage(userId, changedLanguage);
                sender.sendMessage(SendMessageVo.create(messageVo, messageLanguageConfigSuccess(changedLanguage) + CommonMessage.toMain(changedLanguage), Keyboard.mainKeyboard(changedLanguage)));
                sender.sendMessage(SendMessageVo.create(messageVo, HelpMessage.help(changedLanguage, coin, coinMeta.getEnableMarkets()), Keyboard.mainKeyboard(changedLanguage)));
                break;
            default:
                sender.sendMessage(SendMessageVo.create(messageVo, CommonMessage.toMain(language), Keyboard.mainKeyboard(language)));
                break;
        }

        userService.updateMenuStatus(userId, Menu.MAIN);
    }


    public String messageLanguageConfigSuccess(Language language) {
        switch (language) {
            case KR:
                return "언어가 한국어로 변경되었습니다.\n";
            case EN:
                return "Language changed to English.\n";
            default:
                throw new InvalidUserLanguageException();
        }
    }


}
