package com.podo.coinchatbot.app.telegram.menu.handler;

import com.podo.coinchatbot.app.domain.dto.UserDto;
import com.podo.coinchatbot.app.domain.service.UserService;
import com.podo.coinchatbot.app.domain.service.UserSuggestService;
import com.podo.coinchatbot.app.model.CoinMeta;
import com.podo.coinchatbot.app.model.Menu;
import com.podo.coinchatbot.app.telegram.TelegramMessageSender;
import com.podo.coinchatbot.app.telegram.command.SuggestMessageCommand;
import com.podo.coinchatbot.app.telegram.exception.InvalidUserLanguageException;
import com.podo.coinchatbot.app.telegram.keyboard.Keyboard;
import com.podo.coinchatbot.app.telegram.menu.AbstractMenuHandler;
import com.podo.coinchatbot.app.telegram.message.CommonMessage;
import com.podo.coinchatbot.app.telegram.model.MessageVo;
import com.podo.coinchatbot.app.telegram.model.SendMessageVo;
import com.podo.coinchatbot.core.Coin;
import com.podo.coinchatbot.core.Language;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class SuggestMessageHandler extends AbstractMenuHandler {

    @Value("${admin.telegramId:}")
    private final Integer adminTelegramId;

    @Value("${admin.chatId:}")
    private final Long adminChatId;

    private final UserService userService;
    private final UserSuggestService userSuggestService;

    @Override
    public Menu getHandleMenu() {
        return Menu.SUGGEST_MESSAGE;
    }

    @Override
    @Transactional
    public void handle(MessageVo messageVo, Coin coin, CoinMeta coinMeta, UserDto user, String messageText, TelegramMessageSender sender) {
        Long userId = user.getId();
        Language language = user.getLanguage();

        SuggestMessageCommand command = SuggestMessageCommand.from(language, messageText);

        switch (command) {
            case OUT:
                sender.sendMessage(SendMessageVo.create(messageVo, CommonMessage.toMain(language), Keyboard.mainKeyboard(language)));
                break;
            default:
                userSuggestService.insertNew(userId, messageText);
                sender.sendMessage(SendMessageVo.create(messageVo, messageThankYouSuggest(language), Keyboard.mainKeyboard(language)));

                //TO ADMIN
                StringBuilder adminNotifyMessage = new StringBuilder();
                adminNotifyMessage.append("메세지가 도착했습니다!\n------------------\n\n");
                adminNotifyMessage.append(messageText);
                adminNotifyMessage.append("\n\n------------------\n");
                adminNotifyMessage.append(" By ");
                adminNotifyMessage.append(user.getUsername() + " [" + userId + " ]");
                sender.sendMessage(SendMessageVo.create(new MessageVo(adminTelegramId, adminChatId, null), adminNotifyMessage.toString(), Keyboard.mainKeyboard(language)));
                break;
        }

        userService.updateMenuStatus(userId, Menu.MAIN);
    }


    public String messageThankYouSuggest(Language language) {
        StringBuilder message = new StringBuilder();
        switch (language) {
            case KR:
                message.append("의견 감사드립니다.\n");
                message.append("성투하세요^^!\n");
                break;
            case EN:
                message.append("Thank you for your suggest.\n");
                message.append("You will succeed in your investment :)!\n");
                break;
            default:
                throw new InvalidUserLanguageException();
        }
        return message.toString();
    }

}
