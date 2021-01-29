package com.podo.coinchatbot.app.telegram.menu.handler;

import com.podo.coinchatbot.app.domain.dto.UserDto;
import com.podo.coinchatbot.app.domain.service.UserService;
import com.podo.coinchatbot.app.model.CoinMeta;
import com.podo.coinchatbot.app.model.Menu;
import com.podo.coinchatbot.app.telegram.CoinFormatter;
import com.podo.coinchatbot.app.telegram.TelegramMessageSender;
import com.podo.coinchatbot.app.telegram.exception.InvalidUserLanguageException;
import com.podo.coinchatbot.app.telegram.exception.UserInvalidInputException;
import com.podo.coinchatbot.app.telegram.keyboard.Keyboard;
import com.podo.coinchatbot.app.telegram.menu.AbstractMenuHandler;
import com.podo.coinchatbot.app.telegram.message.CommonMessage;
import com.podo.coinchatbot.app.telegram.model.MessageVo;
import com.podo.coinchatbot.app.telegram.model.SendMessageVo;
import com.podo.coinchatbot.app.util.NumberUtil;
import com.podo.coinchatbot.core.Coin;
import com.podo.coinchatbot.core.Language;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class CoinCountConfigHandler extends AbstractMenuHandler {

    private final UserService userService;

    @Override
    public Menu getHandleMenu() {
        return Menu.COIN_COUNT_CONFIG;
    }

    @Override
    @Transactional
    public void handle(MessageVo messageVo, Coin coin, CoinMeta coinMeta, UserDto user, String messageText, TelegramMessageSender sender) {
        Long userId = user.getId();
        Language language = user.getLanguage();
        StringBuilder message = new StringBuilder();

        BigDecimal coinCount = extractCoinCount(messageText);

        userService.updateCoinCount(userId, coinCount);

        if (NumberUtil.eq(BigDecimal.ZERO, coinCount)) {
            message.append(messageCoinCountInit(language));
        } else {
            message.append(messageCoinCountConfig(coinCount, language, coinMeta.getCoinFormatter()));
        }

        message.append(CommonMessage.toMain(language));

        sender.sendMessage(SendMessageVo.create(messageVo, message.toString(), Keyboard.mainKeyboard(language)));
        userService.updateMenuStatus(userId, Menu.MAIN);
    }

    private BigDecimal extractCoinCount(String messageText) {
        try {
            return BigDecimal.valueOf(Double.parseDouble(messageText));
        } catch (NumberFormatException e) {
            throw new UserInvalidInputException("코인개수 형식이 올바르지 않습니다.");
        }
    }

    public String messageCoinCountInit(Language language) {
        switch (language) {
            case KR:
                return "코인개수가 초기화 되었습니다.\n";
            case EN:
                return "Investment Price has been init.\n";
            default:
                throw new InvalidUserLanguageException();
        }
    }

    public String messageCoinCountConfig(BigDecimal coinCount, Language language, CoinFormatter coinFormatter) {
        switch (language) {
            case KR:
                return "코인개수가 " + coinFormatter.toCoinCntStr(coinCount, language) + "로 설정되었습니다.\n";
            case EN:
                return "The number of coins is set at " + coinFormatter.toCoinCntStr(coinCount, language) + "\n";
            default:
                throw new InvalidUserLanguageException();
        }
    }

}
