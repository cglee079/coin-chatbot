package com.podo.coinchatbot.app.job;


import com.podo.coinchatbot.app.client.CoinEndpointer;
import com.podo.coinchatbot.app.client.CoinEndpointerUtil;
import com.podo.coinchatbot.app.client.model.CoinResponse;
import com.podo.coinchatbot.app.config.CoinProperties;
import com.podo.coinchatbot.app.domain.dto.UserTargetAlarmDto;
import com.podo.coinchatbot.app.domain.service.UserTargetAlarmService;
import com.podo.coinchatbot.app.telegram.CoinFormatter;
import com.podo.coinchatbot.app.telegram.TelegramMessageSender;
import com.podo.coinchatbot.core.Coin;
import com.podo.coinchatbot.core.Market;
import lombok.RequiredArgsConstructor;
import net.logstash.logback.argument.StructuredArguments;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TargetAlarmEachCoin {

    private static final Logger LOGGER = LoggerFactory.getLogger("JOB_LOGGER");

    private final TargetAlarmEachTarget targetAlarmEachTarget;
    private final UserTargetAlarmService userTargetAlarmService;
    private final CoinEndpointer coinEndpointer;
    private final Map<Coin, CoinFormatter> coinToCoinFormatters;
    private final Map<Coin, TelegramMessageSender> coinToTelegramMessageSender;
    private final ExecutorService executorService = Executors.newFixedThreadPool(3);

    @Transactional
    public void alarmEachCoin(TargetAlarmJobContext targetAlarmJobContext, Coin coin, CoinProperties.CoinProperty.MarketConfig marketConfig) {

        Market market = Market.valueOf(marketConfig.getId());
        CoinResponse coinResponse = CoinEndpointerUtil.getCoin(coinEndpointer, coin, market);
        BigDecimal currentPrice = coinResponse.getCurrentPrice();

        if (marketConfig.getIsBtc()) {
            currentPrice = CoinEndpointerUtil.btcToMoney(coinEndpointer, market, currentPrice);
        }

        List<UserTargetAlarmDto> forFocusUpTargetAlarm = userTargetAlarmService.getForFocusUpTargetAlarm(coin, market, currentPrice);
        for (UserTargetAlarmDto target : forFocusUpTargetAlarm) {
            alarmEachTarget(targetAlarmJobContext, market, currentPrice, coin, target);
        }

        List<UserTargetAlarmDto> forFocusDownTargetAlarm = userTargetAlarmService.getForFocusDownTargetAlarm(coin, market, currentPrice);
        for (UserTargetAlarmDto target : forFocusDownTargetAlarm) {
            alarmEachTarget(targetAlarmJobContext, market, currentPrice, coin, target);
        }

        targetAlarmJobContext.put("targetAlarmSize", forFocusUpTargetAlarm.size() + forFocusDownTargetAlarm.size());

    }

    private void alarmEachTarget(TargetAlarmJobContext targetAlarmJobContext, Market market, BigDecimal currentPrice, Coin coin, UserTargetAlarmDto target) {
        CoinFormatter coinFormatter = coinToCoinFormatters.get(coin);
        TelegramMessageSender telegramMessageSender = coinToTelegramMessageSender.get(coin);

        executorService.submit(() -> {
            try {
                TargetAlarmEachTargetContext.init();
                TargetAlarmEachTargetContext.put("jobId", targetAlarmJobContext.getId());
                TargetAlarmEachTargetContext.put("coin", coin);
                targetAlarmEachTarget.alarmEachTarget(market, currentPrice, coinFormatter, telegramMessageSender, target);
            } catch (Exception e) {
                TargetAlarmEachTargetContext.put("exceptionMessage", e.getMessage());
                TargetAlarmEachTargetContext.put("stackTrace", Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.joining("\n")));
                throw new RuntimeException(e);
            } finally {
                LOGGER.info("", StructuredArguments.value("context", TargetAlarmEachTargetContext.toLog()));
                TargetAlarmEachTargetContext.removeAll();
            }
        });
    }

}

