package com.podo.coinchatbot.app.telegram;


import com.podo.coinchatbot.app.telegram.exception.TelegramApiRuntimeException;
import com.podo.coinchatbot.app.telegram.exception.UserInvalidInputException;
import com.podo.coinchatbot.app.telegram.message.CommonMessage;
import com.podo.coinchatbot.core.Coin;
import com.podo.coinchatbot.core.Language;
import com.podo.coinchatbot.app.telegram.keyboard.Keyboard;
import com.podo.coinchatbot.app.model.CoinMeta;
import com.podo.coinchatbot.app.domain.service.UserService;
import com.podo.coinchatbot.app.telegram.exception.InvalidUserLanguageException;
import com.podo.coinchatbot.app.telegram.menu.MenuHandler;
import com.podo.coinchatbot.app.telegram.message.UserSettingMessage;
import com.podo.coinchatbot.app.telegram.message.HelpMessage;
import com.podo.coinchatbot.app.domain.dto.UserDto;
import com.podo.coinchatbot.app.model.Menu;
import com.podo.coinchatbot.app.telegram.model.MessageVo;
import com.podo.coinchatbot.app.telegram.model.SendMessageVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.argument.StructuredArguments;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.podo.coinchatbot.core.Language.KR;

@Slf4j
@RequiredArgsConstructor
public class TelegramMessageReceiverHandler {

    private static final Logger LOGGER =LoggerFactory.getLogger("MESSAGE_LOGGER");

    private final Coin coin;
    private final Map<Menu, MenuHandler> menuHandlers;
    private final CoinMeta coinMeta;
    private final UserService userService;
    private final TelegramMessageSender telegramMessageSender;

    @Transactional
    public void handle(Update update) {
        Message message = getMessage(update);
        Integer telegramId = message.getFrom().getId();
        Long chatId = message.getChatId();
        Integer messageId = message.getMessageId();
        UserDto user = userService.getOrNull(coin, chatId);

        try {
            MessageContext.init();
            handle(message);
        } catch (TelegramApiRuntimeException e) {
            handleException(e);
            if (user != null) {
                telegramMessageSender.send(SendMessageVo.create(new MessageVo(telegramId, chatId, messageId), e.getMessage() + CommonMessage.toMain(user.getLanguage()), Keyboard.mainKeyboard(user.getLanguage())));
                userService.increaseErrorCount(user.getId());
                userService.updateMenuStatus(user.getId(), Menu.MAIN);
            }
        } catch (UserInvalidInputException e) {
            handleException(e);
            if (user != null) {
                telegramMessageSender.send(SendMessageVo.create(new MessageVo(telegramId, chatId, messageId), e.getMessage() + CommonMessage.toMain(user.getLanguage()), Keyboard.mainKeyboard(user.getLanguage())));
                userService.updateMenuStatus(user.getId(), Menu.MAIN);
            }
        } catch (Exception e) {
            handleException(e);
            if (user != null) {
                telegramMessageSender.send(SendMessageVo.create(new MessageVo(telegramId, chatId, messageId), e.getMessage() + CommonMessage.warningWaitSecond(user.getLanguage()), Keyboard.mainKeyboard(user.getLanguage())));
                userService.updateMenuStatus(user.getId(), Menu.MAIN);
            }
        } finally {
            LOGGER.info("", StructuredArguments.value("context", MessageContext.toLog()));
            MessageContext.removeAll();
        }
    }

    private void handleException(Exception e) {
        e.printStackTrace();
        MessageContext.put("exceptionMessage", e.getMessage());
        MessageContext.put("stackTrace", Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.joining("\n")));
    }

    public void handle(Message message) {

        final User telegramUser = message.getFrom();
        final String username = telegramUser.getLastName() + " " + telegramUser.getFirstName();
        final Integer telegramId = telegramUser.getId();
        final Long chatId = message.getChatId();
        final Integer messageId = message.getMessageId();
        final String messageText = message.getText();
        final LocalDateTime messageReceivedAt = LocalDateTime.now();

        MessageContext.put("telegramId", telegramId);
        MessageContext.put("chatId", chatId);
        MessageContext.put("messageText", messageText);
        MessageContext.put("messageId", messageId);
        MessageContext.putDateTime("messageReceivedAt", messageReceivedAt);

        final MessageVo messageVo = new MessageVo(telegramId, chatId, messageId);

        UserDto user = userService.getOrNull(coin, chatId);

        if (Objects.isNull(user)) {
            userService.createNewUser(coin, telegramId, chatId, username, coinMeta.getFirstEnableMarkets(), messageReceivedAt);

            telegramMessageSender.send(SendMessageVo.create(messageVo, drawStartMessage(KR, coin) + HelpMessage.help(KR, coin, coinMeta.getEnableMarkets()), Keyboard.mainKeyboard(KR)));
            telegramMessageSender.send(SendMessageVo.create(messageVo, HelpMessage.explainForForeigner(), Keyboard.mainKeyboard(KR)));
            return;
        }

        if (messageText.equals("/start")) {
            Language language = user.getLanguage();
            telegramMessageSender.send(SendMessageVo.create(messageVo, explainAlreadyStartService(language, coin), Keyboard.mainKeyboard(language)));
            telegramMessageSender.send(SendMessageVo.create(messageVo, UserSettingMessage.get(user, coinMeta.getCoinFormatter()), Keyboard.mainKeyboard(language)));
            return;
        }

        userService.updateMessageSendAt(user.getId(), messageReceivedAt);

        handleCommand(messageVo, user, coin, coinMeta, messageText, telegramMessageSender);
    }

    private Message getMessage(Update update) {
        if (Objects.nonNull(update.getEditedMessage())) {
            return update.getEditedMessage();
        }

        return update.getMessage();
    }

    private void handleCommand(MessageVo messageVo, UserDto user, Coin coin, CoinMeta coinMeta, String messageText, TelegramMessageSender sender) {
        final MenuHandler menuHandler = menuHandlers.get(user.getMenuStatus());
        menuHandler.handle(messageVo, coin, coinMeta, user, messageText, sender);
    }

    public String drawStartMessage(Language language, Coin coin) {
        switch (language) {
            case KR:
                return coin + " 알림이 시작되었습니다.\n\n";
            case EN:
                return coin + " Noticer Start.\n\n";
        }

        throw new InvalidUserLanguageException();
    }


    public String explainAlreadyStartService(Language language, Coin coin) {
        switch (language) {
            case KR:
                return "이미 " + coin + " 알리미에 설정 정보가 기록되어있습니다.\n";
            case EN:
                return "Already " + coin + " Noticer Started.\n Database have your setting information.\n";
        }
        throw new InvalidUserLanguageException();
    }

}
