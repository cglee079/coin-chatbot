package com.podo.coinchatbot.telegram.app;


import com.podo.coinchatbot.core.Coin;
import com.podo.coinchatbot.core.Lang;
import com.podo.coinchatbot.core.Market;
import com.podo.coinchatbot.telegram.Keyboard;
import com.podo.coinchatbot.telegram.coin.CoinMeta;
import com.podo.coinchatbot.telegram.command.MainCommand;
import com.podo.coinchatbot.telegram.domain.client.UserService;
import com.podo.coinchatbot.telegram.exception.InvalidLanguageException;
import com.podo.coinchatbot.telegram.menu.MenuHandler;
import com.podo.coinchatbot.telegram.message.ClientSettingMessage;
import com.podo.coinchatbot.telegram.message.ExplainHelpMessage;
import com.podo.coinchatbot.telegram.model.UserDto;
import com.podo.coinchatbot.telegram.model.Menu;
import com.podo.coinchatbot.telegram.model.MessageVo;
import com.podo.coinchatbot.telegram.model.SendMessageVo;
import com.podo.coinchatbot.telegram.util.NumberFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.podo.coinchatbot.core.Lang.KR;

@Component
@RequiredArgsConstructor
public class TelegramMessageReceiverHandler {

    private final Coin coin;
    private final List<Market> enabledMarkets;
    private final Map<Menu, MenuHandler> menuHandlers;
    private final TelegramMessageSender telegramMessageSender;
    private final CoinMeta coinMeta;
    private final UserService userService;

    public void receive(Message message) {

        final User telegramUser = message.getFrom();
        final String username = telegramUser.getLastName() + " " + telegramUser.getFirstName();
        final String telegramId = telegramUser.getId() + "";
        final Integer messageId = message.getMessageId();
        final String messageText = message.getText();
        final LocalDateTime messageReceivedAt = LocalDateTime.now();

        final MessageVo messageVo = new MessageVo(telegramId, messageId);

        UserDto user = userService.get(coin, telegramId);

        if (Objects.isNull(user)) {
            userService.createNewUser(coin, telegramId, username, enabledMarkets.get(0));

            telegramMessageSender.send(SendMessageVo.create(messageVo, drawStartMessage(KR) + ExplainHelpMessage.get(enabledMarkets, coin, KR), Keyboard.mainKeyboard(KR)));
            telegramMessageSender.send(SendMessageVo.create(messageVo, explainSetForeigner(), Keyboard.mainKeyboard(KR)));
            return;
        }

        if (messageText.equals("/start")) {
            Lang lang = user.getLang();
            telegramMessageSender.send(SendMessageVo.create(messageVo, explainAlreadyStartService(lang), Keyboard.mainKeyboard(lang)));
            telegramMessageSender.send(SendMessageVo.create(messageVo, ClientSettingMessage.get(user, lang, coinMeta.getNumberFormatter()), Keyboard.mainKeyboard(lang)));
            return;
        }

        userService.updateMessageSendAt(user.getId(), messageReceivedAt);

        handleCommand(messageVo, user, coin, coinMeta, messageText);
    }

    private void handleCommand(MessageVo messageVo, UserDto user, Coin coin, CoinMeta coinMeta, String messageText) {
        final MenuHandler menuHandler = menuHandlers.get(user.getMenuStatus());
        menuHandler.handle(messageVo, coin, coinMeta, user, messageText);
    }

    public String drawStartMessage(Lang lang) {
        switch (lang) {
            case KR:
                return coin + " 알림이 시작되었습니다.\n\n";
            case EN:
                return coin + " Noticer Start.\n\n";
        }

        throw new InvalidLanguageException();
    }

    public String explainSetForeigner() {
        StringBuilder message = new StringBuilder();
        message.append("★  If you are not Korean, Must read!!\n");
        message.append("* Use the " + MainCommand.PREFERENCE.getCommand(Lang.EN) + " Menu.\n");
        message.append("* First. Please set language to English.\n");
        message.append("* Second. Set the time adjustment for accurate notifications. Because of time difference by country.\n");
        return message.toString();
    }

    public String explainAlreadyStartService(Lang lang) {
        switch(lang) {
            case KR : return "이미 " + coin + " 알리미에 설정 정보가 기록되어있습니다.\n";
            case EN : return "Already " + coin + " Noticer Started.\n Database have your setting information.\n";
        }
        throw new InvalidLanguageException();
    }

}
