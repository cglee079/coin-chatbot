package com.podo.coinchatbot.app.domain.service;

import com.podo.coinchatbot.app.domain.dto.TimelyCoinPriceDto;
import com.podo.coinchatbot.app.domain.dto.TimelyCoinPriceInsertDto;
import com.podo.coinchatbot.app.domain.exception.NotFoundTimelyCoinPriceException;
import com.podo.coinchatbot.app.domain.model.TimelyCoinPrice;
import com.podo.coinchatbot.app.domain.model.TimelyCoinPriceStatus;
import com.podo.coinchatbot.app.domain.repository.TimelyCoinPriceRepository;
import com.podo.coinchatbot.core.Coin;
import com.podo.coinchatbot.core.Market;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TimelyCoinPriceService {

    private final TimelyCoinPriceRepository timelyCoinPriceRepository;

    @Transactional(readOnly = true)
    public TimelyCoinPriceDto getByCoinAndMarketAndDateTime(Coin coin, Market market, LocalDateTime dateTime) {
        LocalDateTime dateTimeTruncatedMinute = dateTime.truncatedTo(ChronoUnit.MINUTES);
        Optional<TimelyCoinPrice> timelyCoinPriceOptional = timelyCoinPriceRepository.findByCoinAndMarketAndDateTime(coin, market, dateTimeTruncatedMinute);

        return timelyCoinPriceOptional
                .map(TimelyCoinPriceDto::new)
                .orElseThrow(() -> new NotFoundTimelyCoinPriceException(coin, market, dateTimeTruncatedMinute));
    }

    @Transactional
    public void insertNew(TimelyCoinPriceInsertDto timelyCoinPriceInsertDto) {
        timelyCoinPriceRepository.save(timelyCoinPriceInsertDto.toEntity());
    }

    @Transactional
    public void insertByCopyBeforeHour(Coin coin, Market market, LocalDateTime dateTime, String errorMessage) {
        LocalDateTime dateTimeTruncatedMinute = dateTime.truncatedTo(ChronoUnit.MINUTES);
        LocalDateTime beforeDateTime = dateTime.minusHours(1);
        Optional<TimelyCoinPrice> beforeHourTimelyCoinPriceOptional = timelyCoinPriceRepository.findByCoinAndMarketAndDateTime(coin, market, beforeDateTime);

        TimelyCoinPrice beforeHourTimelyCoinPrice = beforeHourTimelyCoinPriceOptional
                .orElseThrow(() -> new NotFoundTimelyCoinPriceException(coin, market, beforeDateTime));

        timelyCoinPriceRepository.save(
                TimelyCoinPrice.builder()
                        .volume(beforeHourTimelyCoinPrice.getVolume())
                        .lastPrice(beforeHourTimelyCoinPrice.getLastPrice())
                        .highPrice(beforeHourTimelyCoinPrice.getHighPrice())
                        .lowPrice(beforeHourTimelyCoinPrice.getLowPrice())
                        .coin(coin)
                        .dateTime(dateTimeTruncatedMinute)
                        .status(TimelyCoinPriceStatus.ERROR)
                        .additionalInfo(errorMessage)
                        .market(market)
                        .build()
        );
    }
}
