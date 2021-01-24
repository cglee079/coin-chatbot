package com.podo.coinchatbot.app.telegram.menu.handler;

import com.podo.coinchatbot.core.Coin;
import com.podo.coinchatbot.core.Language;
import com.podo.coinchatbot.core.Market;
import com.podo.coinchatbot.app.telegram.keyboard.Keyboard;
import com.podo.coinchatbot.app.telegram.TelegramMessageSender;
import com.podo.coinchatbot.app.model.CoinMeta;
import com.podo.coinchatbot.app.telegram.command.TargetAlarmConfigCommand;
import com.podo.coinchatbot.app.domain.service.UserTargetAlarmService;
import com.podo.coinchatbot.app.domain.service.UserService;
import com.podo.coinchatbot.app.telegram.exception.InvalidUserLanguageException;
import com.podo.coinchatbot.app.telegram.menu.AbstractMenuHandler;
import com.podo.coinchatbot.app.telegram.message.CommonMessage;
import com.podo.coinchatbot.app.model.Menu;
import com.podo.coinchatbot.app.telegram.model.MessageVo;
import com.podo.coinchatbot.app.telegram.model.SendMessageVo;
import com.podo.coinchatbot.app.domain.dto.UserDto;
import com.podo.coinchatbot.app.domain.dto.UserTargetAlarmDto;
import com.podo.coinchatbot.app.telegram.CoinFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TargetAlarmConfigHandler extends AbstractMenuHandler {

    private final UserService userService;
    private final UserTargetAlarmService userTargetAlarmService;

    @Override
    public Menu getHandleMenu() {
        return Menu.TARGET_ALARM_CONFIG;
    }

    @Override
    @Transactional
    public void handle(MessageVo messageVo, Coin coin, CoinMeta coinMeta, UserDto user, String messageText, TelegramMessageSender sender) {
        Long userId = user.getId();
        Language language = user.getLanguage();
        Market market = user.getMarket();
        CoinFormatter coinFormatter = coinMeta.getCoinFormatter();
        TargetAlarmConfigCommand command = TargetAlarmConfigCommand.from(language, messageText);

        Menu menuStatus = Menu.MAIN;
        switch (command) {
            case ADD:
                sender.sendMessage(SendMessageVo.create(messageVo, explainTargetAdd(language, market, coinMeta), Keyboard.defaultKeyboard()));
                menuStatus = Menu.TARGET_ALARM_ADD;
                break;
            case DEL:
                List<UserTargetAlarmDto> targetAlarms = userTargetAlarmService.findByUserId(userId);
                List<String> targetDeleteCommandText = targetAlarms.stream()
                        .map(t -> coinFormatter.toMoneyStr(t.getTargetPrice(), market) + " " + t.getTargetFocus().getStr(language))
                        .collect(Collectors.toList());

                sender.sendMessage(SendMessageVo.create(messageVo, explainTargetDel(language), Keyboard.deleteTargetKeyboard(targetDeleteCommandText, language)));
                menuStatus = Menu.TARGET_ALARM_DELETE;
                break;
            case OUT:
            default:
                sender.sendMessage(SendMessageVo.create(messageVo, CommonMessage.toMain(language), Keyboard.mainKeyboard(language)));
                break;
        }

        userService.updateMenuStatus(userId, menuStatus);
    }

    public String explainTargetAdd(Language language, Market market, CoinMeta coinMeta) {
        StringBuilder message = new StringBuilder();
        CoinMeta.Example example = coinMeta.getExample();
        CoinFormatter coinFormatter = coinMeta.getCoinFormatter();

        BigDecimal exampleTargetRate = example.getTargetRate();
        BigDecimal exampleTargetPrice = null;

        if (market.isKRW()) {
            exampleTargetPrice = example.getTargetKRW();
        }

        if (market.isUSD()) {
            exampleTargetPrice = example.getTargetUSD();
        }

        switch (language) {
            case KR:
                message.append("목표가격을 설정해주세요.\n");
                message.append("목표가격이 되었을 때 알림이 전송됩니다.\n");
                message.append("목표가격을 위한 가격정보는 1분 주기로 갱신됩니다.\n");
                message.append("\n");
                message.append("* 목표가격은 숫자 또는 백분율로 입력해주세요.\n");
                message.append("* ex) " + exampleTargetPrice + "  : 목표가격 " + coinFormatter.toMoneyStr(exampleTargetPrice, market) + "\n");
                message.append("* ex) " + exampleTargetRate + "%    : 현재가 +" + exampleTargetRate + "%\n");
                message.append("* ex) -" + exampleTargetRate + "%  : 현재가 -" + exampleTargetRate + "%\n");
                message.append("\n");
                message.append("\n");
                message.append("# 메인으로 돌아가시려면 문자를 입력해주세요.\n");
                break;
            case EN:
                message.append("Please set Target Price.\n");
                message.append("Once you reach the target price, you will be notified.\n");
                message.append("Coin price information is updated every 1 minute.\n");
                message.append("\n");
                message.append("* Please enter the target price in numbers or percentages.\n");
                message.append("* If you enter 0, it is initialized.\n");
                message.append("* example)  " + exampleTargetPrice + "  : Target price " + coinFormatter.toMoneyStr(exampleTargetPrice, market) + "\n");
                message.append("* example)  " + exampleTargetRate + "   : Current Price +" + exampleTargetRate + "\n");
                message.append("* example)  -" + exampleTargetRate + "  : Current Prcie -" + exampleTargetRate + "\n");
                message.append("\n");
                message.append("\n");
                message.append("# To return to main, enter a character.\n");
                break;
        }

        return message.toString();
    }

    public String explainTargetDel(Language language) {
        switch (language) {
            case KR:
                return "삭제할 목표가를 선택해주세요.\n";
            case EN:
                return "Please select Target you want to delete \n";
            default:
                throw new InvalidUserLanguageException();
        }
    }

}
