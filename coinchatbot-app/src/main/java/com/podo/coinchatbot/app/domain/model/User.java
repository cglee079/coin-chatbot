package com.podo.coinchatbot.app.domain.model;

import com.podo.coinchatbot.app.model.Menu;
import com.podo.coinchatbot.core.Coin;
import com.podo.coinchatbot.core.Language;
import com.podo.coinchatbot.core.Market;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Coin coin;

    @Enumerated(EnumType.STRING)
    private Market market;

    private Long telegramId;

    private Long chatId;

    private String username;

    private Integer dayloopAlarm;

    private Integer timeloopAlarm;

    @Enumerated(EnumType.STRING)
    private Menu menuStatus;

    private BigDecimal invest;

    private BigDecimal coinCount;

    private Integer errorCount;

    @Enumerated(EnumType.STRING)
    private Language language;

    private Long timeDifference;

    private LocalDateTime messageSendAt;

    @CreatedDate
    private LocalDateTime createAt;

    @LastModifiedDate
    private LocalDateTime updateAt;

    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;

    @Builder
    public User(Coin coin, Market market, Long telegramId, String username, Long chatId,
                Integer dayloopAlarm, Integer timeloopAlarm, Menu menuStatus, BigDecimal invest,
                BigDecimal coinCount, Integer errorCount, Language language, Long timeDifference,
                LocalDateTime messageSendAt, UserStatus userStatus) {
        this.coin = coin;
        this.market = market;
        this.telegramId = telegramId;
        this.chatId = chatId;
        this.username = username;
        this.dayloopAlarm = dayloopAlarm;
        this.timeloopAlarm = timeloopAlarm;
        this.menuStatus = menuStatus;
        this.invest = invest;
        this.coinCount = coinCount;
        this.errorCount = errorCount;
        this.language = language;
        this.timeDifference = timeDifference;
        this.messageSendAt = messageSendAt;
        this.userStatus = userStatus;
    }

    public void updateMessageSendAt(LocalDateTime messageReceiveAt) {
        this.messageSendAt = messageReceiveAt;
    }

    public void moveMenu(Menu menuStatus) {
        this.menuStatus = menuStatus;
    }

    public void changeDayloopAlarm(Integer dayloopValue) {
        this.dayloopAlarm = dayloopValue;
    }

    public void changeTimeloopAlarm(Integer timeloopValue) {
        this.timeloopAlarm = timeloopValue;
    }

    public void changeInvest(BigDecimal invest) {
        this.invest = invest;
    }

    public void changeMarket(Market changedMarket) {
        this.market = changedMarket;
    }

    public void changeCoinCount(BigDecimal coinCount) {
        this.coinCount = coinCount;
    }

    public void stopAllAlarm() {
        this.dayloopAlarm = 0;
        this.timeloopAlarm = 0;
    }

    public void changeLanguage(Language changedLanguage) {
        language = changedLanguage;
    }

    public void changeTimeDifference(Long timeDifference) {
        this.timeDifference = timeDifference;
    }

    public void increaseErrorCount() {
        this.errorCount++;
        if (errorCount > 5) {
            this.userStatus = UserStatus.DEAD;
        }
    }
}
