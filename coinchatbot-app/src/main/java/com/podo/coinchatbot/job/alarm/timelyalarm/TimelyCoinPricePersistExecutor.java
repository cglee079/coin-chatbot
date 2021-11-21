package com.podo.coinchatbot.job.alarm.timelyalarm;

import com.podo.coinchatbot.external.CoinEndpointer;
import com.podo.coinchatbot.external.model.CoinResponse;
import com.podo.coinchatbot.app.domain.dto.TimelyCoinPriceInsertDto;
import com.podo.coinchatbot.app.domain.model.TimelyCoinPriceStatus;
import com.podo.coinchatbot.app.domain.service.TimelyCoinPriceService;
import com.podo.coinchatbot.core.Coin;
import com.podo.coinchatbot.core.Market;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class TimelyCoinPricePersistExecutor {

    private final TimelyCoinPriceService timelyCoinPriceService;
    private final CoinEndpointer coinEndpointer;

    public void saveCoinPrice(Coin coin, LocalDateTime now, Market market) {

        CoinResponse coinResponse = coinEndpointer.getCoin(coin, market);

        if (!coinResponse.isSuccess()) {
            timelyCoinPriceService.insertByCopyBeforeHour(coin, market, now, coinResponse.getErrorMessage());
            return;
        }

        BigDecimal lowPrice = coinResponse.getLowPrice();
        BigDecimal highPrice = coinResponse.getHighPrice();
        BigDecimal volume = coinResponse.getVolume();
        BigDecimal lastPrice = coinResponse.getLastPrice();

        timelyCoinPriceService.insertNew(
                TimelyCoinPriceInsertDto.builder()
                        .coin(coin)
                        .dateTime(now)
                        .highPrice(highPrice)
                        .lowPrice(lowPrice)
                        .lastPrice(lastPrice)
                        .market(market)
                        .status(TimelyCoinPriceStatus.SUCCESS)
                        .volume(volume)
                        .build()
        );
    }
}
