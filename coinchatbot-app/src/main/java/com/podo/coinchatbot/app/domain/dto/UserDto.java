package com.podo.coinchatbot.app.domain.dto;

import com.podo.coinchatbot.app.domain.model.User;
import com.podo.coinchatbot.app.domain.model.UserStatus;
import com.podo.coinchatbot.app.model.Menu;
import com.podo.coinchatbot.core.Coin;
import com.podo.coinchatbot.core.Language;
import com.podo.coinchatbot.core.Market;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class UserDto {
    private Long id;
    private Coin coin;
    private Integer telegramId;
    private Long chatId;
    private String username;
    private Long timeDifference;
    private Language language;
    private Menu menuStatus;
    private Market market;
    private Integer timeloopAlarm;
    private Integer dayloopAlarm;
    private BigDecimal invest;
    private BigDecimal coinCount;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private LocalDateTime messageSendAt;
    private Integer errorCount;
    private UserStatus userStatus;


    public UserDto(User user) {
        this.id = user.getId();
        this.coin = user.getCoin();
        this.telegramId = user.getTelegramId();
        this.chatId = user.getChatId();
        this.username = user.getUsername();
        this.menuStatus = user.getMenuStatus();
        this.language = user.getLanguage();
        this.timeDifference = user.getTimeDifference();
        this.market = user.getMarket();
        this.timeloopAlarm = user.getTimeloopAlarm();
        this.dayloopAlarm = user.getDayloopAlarm();
        this.invest = user.getInvest();
        this.coinCount = user.getCoinCount();
        this.createAt = user.getCreateAt();
        this.updateAt = user.getUpdateAt();
        this.messageSendAt = user.getMessageSendAt();
        this.errorCount = user.getErrorCount();
        this.userStatus = user.getUserStatus();
    }
}
