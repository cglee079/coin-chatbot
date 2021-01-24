package com.podo.coinchatbot.app.telegram.menu.handler;

import com.podo.coinchatbot.app.telegram.exception.UserInvalidInputException;
import com.podo.coinchatbot.app.util.NumberUtil;
import com.podo.coinchatbot.core.Coin;
import com.podo.coinchatbot.core.Language;
import com.podo.coinchatbot.core.Market;
import com.podo.coinchatbot.app.telegram.keyboard.Keyboard;
import com.podo.coinchatbot.app.telegram.TelegramMessageSender;
import com.podo.coinchatbot.app.model.CoinMeta;
import com.podo.coinchatbot.app.domain.service.UserService;
import com.podo.coinchatbot.app.telegram.exception.InvalidUserLanguageException;
import com.podo.coinchatbot.app.telegram.menu.AbstractMenuHandler;
import com.podo.coinchatbot.app.telegram.message.CommonMessage;
import com.podo.coinchatbot.app.model.Menu;
import com.podo.coinchatbot.app.telegram.model.MessageVo;
import com.podo.coinchatbot.app.telegram.model.SendMessageVo;
import com.podo.coinchatbot.app.domain.dto.UserDto;
import com.podo.coinchatbot.app.telegram.CoinFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class InvestConfigHandler extends AbstractMenuHandler {

    private final UserService userService;

    @Override
    public Menu getHandleMenu() {
        return Menu.INVEST_CONFIG;
    }

    @Override
    @Transactional
    public void handle(MessageVo messageVo, Coin coin, CoinMeta coinMeta, UserDto user, String messageText, TelegramMessageSender sender) {
        Long userId = user.getId();
        Language language = user.getLanguage();
        Market market = user.getMarket();
        StringBuilder message = new StringBuilder();

        BigDecimal invest = extractInvest(messageText);
        userService.updateInvest(userId, invest);

        if (NumberUtil.eq(BigDecimal.ZERO, invest)) {
            message.append(messageInvestInit(language));
        } else {
            message.append(messageInvestConfig(invest, market, language, coinMeta.getCoinFormatter()));
        }

        message.append(CommonMessage.toMain(language));

        sender.sendMessage(SendMessageVo.create(messageVo, message.toString(), Keyboard.mainKeyboard(language)));
        userService.updateMenuStatus(userId, Menu.MAIN);
    }

    private BigDecimal extractInvest(String messageText) {
        try {
            return BigDecimal.valueOf(Double.parseDouble(messageText));
        } catch (NumberFormatException e) {
            throw new UserInvalidInputException("투자금액 형식이 적절하지 않습니다.");
        }
    }

    public String messageInvestInit(Language language) {
        switch (language) {
            case KR:
                return "투자금액이 초기화 되었습니다.\n";
            case EN:
                return "Investment Price has been init.\n";
            default:
                throw new InvalidUserLanguageException();
        }
    }

    public String messageInvestConfig(BigDecimal invest, Market market, Language language, CoinFormatter coinFormatter) {
        switch (language) {
            case KR:
                return "투자금액이 " + coinFormatter.toInvestAmountStr(invest, market) + "으로 설정되었습니다.\n";
            case EN:
                return "The investment amount is set at " + coinFormatter.toInvestAmountStr(invest, market) + "\n";
            default:
                throw new InvalidUserLanguageException();
        }
    }

}
