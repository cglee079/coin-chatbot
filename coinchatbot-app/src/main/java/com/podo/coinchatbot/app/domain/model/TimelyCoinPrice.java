package com.podo.coinchatbot.app.domain.model;

import com.podo.coinchatbot.core.Coin;
import com.podo.coinchatbot.core.Market;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
import java.time.temporal.ChronoUnit;

@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class TimelyCoinPrice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Coin coin;

    @Enumerated(EnumType.STRING)
    private Market market;

    private LocalDateTime dateTime;

    private BigDecimal highPrice;

    private BigDecimal lowPrice;

    private BigDecimal lastPrice;

    private BigDecimal volume;

    @Enumerated(EnumType.STRING)
    private TimelyCoinPriceStatus status;

    private String additionalInfo;

    @Builder
    public TimelyCoinPrice(Coin coin, LocalDateTime dateTime, Market market, BigDecimal highPrice,
                           BigDecimal lowPrice, BigDecimal lastPrice, BigDecimal volume,
                           TimelyCoinPriceStatus status, String additionalInfo) {
        this.coin = coin;
        this.dateTime = dateTime.truncatedTo(ChronoUnit.HOURS);
        this.market = market;
        this.highPrice = highPrice;
        this.lowPrice = lowPrice;
        this.lastPrice = lastPrice;
        this.volume = volume;
        this.status = status;
        this.additionalInfo = additionalInfo;
    }
}
