package com.podo.coinchatbot.app.domain.dto;


import com.podo.coinchatbot.app.domain.model.TimelyCoinPrice;
import com.podo.coinchatbot.app.domain.model.TimelyCoinPriceStatus;
import com.podo.coinchatbot.core.Coin;
import com.podo.coinchatbot.core.Market;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class TimelyCoinPriceInsertDto {
    private Coin coin;
    private LocalDateTime dateTime;
    private Market market;
    private BigDecimal highPrice;
    private BigDecimal lowPrice;
    private BigDecimal lastPrice;
    private BigDecimal volume;
    private TimelyCoinPriceStatus status;
    private String additionalInfo;

    public TimelyCoinPrice toEntity() {
        return TimelyCoinPrice.builder()
                .coin(coin)
                .market(market)
                .dateTime(dateTime)
                .highPrice(highPrice)
                .lowPrice(lowPrice)
                .lastPrice(lastPrice)
                .volume(volume)
                .status(status)
                .additionalInfo(additionalInfo)
                .build();
    }
}
