package com.podo.coinchatbot.app.job.alarm.targetalarm;


import com.podo.coinchatbot.app.client.CoinEndpointer;
import com.podo.coinchatbot.app.client.CoinEndpointerUtil;
import com.podo.coinchatbot.app.client.model.CoinResponse;
import com.podo.coinchatbot.app.domain.dto.UserTargetAlarmDto;
import com.podo.coinchatbot.app.domain.service.UserTargetAlarmService;
import com.podo.coinchatbot.app.property.MarketConfig;
import com.podo.coinchatbot.app.telegram.CoinFormatter;
import com.podo.coinchatbot.app.telegram.TelegramMessageSender;
import com.podo.coinchatbot.core.Coin;
import com.podo.coinchatbot.core.Market;
import com.podo.coinchatbot.log.InstanceContext;
import com.podo.coinchatbot.log.ThreadLocalContext;
import lombok.RequiredArgsConstructor;
import net.logstash.logback.argument.StructuredArguments;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@RequiredArgsConstructor
public class TargetAlarmEachCoinExecutor {

    private static final Logger LOGGER = LoggerFactory.getLogger("ALARM_LOGGER");

    private final TargetAlarmEachTargetExcecutor targetAlarmEachTargetExcecutor;
    private final UserTargetAlarmService userTargetAlarmService;
    private final CoinEndpointer coinEndpointer;
    private final Map<Coin, CoinFormatter> coinToCoinFormatters;
    private final Map<Coin, TelegramMessageSender> coinToTelegramMessageSender;
    private final ExecutorService executorService = Executors.newFixedThreadPool(3);

    @Transactional
    public void alarmEachCoin(InstanceContext instanceContext, Coin coin, MarketConfig marketConfig) {

        Market market = marketConfig.getMarket();
        CoinResponse coinResponse = CoinEndpointerUtil.getCoin(coinEndpointer, coin, market);
        BigDecimal currentPrice = coinResponse.getCurrentPrice();

        if (marketConfig.getIsBtc()) {
            currentPrice = CoinEndpointerUtil.btcToMoney(coinEndpointer, market, currentPrice);
        }

        List<UserTargetAlarmDto> forFocusUpTargetAlarm = userTargetAlarmService.getForFocusUpTargetAlarm(coin, market, currentPrice);
        for (UserTargetAlarmDto target : forFocusUpTargetAlarm) {
            alarmEachTarget(instanceContext, market, currentPrice, coin, target);
        }

        List<UserTargetAlarmDto> forFocusDownTargetAlarm = userTargetAlarmService.getForFocusDownTargetAlarm(coin, market, currentPrice);
        for (UserTargetAlarmDto target : forFocusDownTargetAlarm) {
            alarmEachTarget(instanceContext, market, currentPrice, coin, target);
        }

        instanceContext.put("numberOfTarget", forFocusUpTargetAlarm.size() + forFocusDownTargetAlarm.size());
    }

    private void alarmEachTarget(InstanceContext instanceContext, Market market, BigDecimal currentPrice, Coin coin, UserTargetAlarmDto target) {
        CoinFormatter coinFormatter = coinToCoinFormatters.get(coin);
        TelegramMessageSender telegramMessageSender = coinToTelegramMessageSender.get(coin);

        executorService.submit(() -> {
            try {
                ThreadLocalContext.init("target-alarm");
                ThreadLocalContext.put("jobId", instanceContext.getId());
                ThreadLocalContext.put("coin", coin);
                targetAlarmEachTargetExcecutor.alarmEachTarget(market, currentPrice, coinFormatter, telegramMessageSender, target);
            } catch (Exception e) {
                ThreadLocalContext.putException(e);
                throw new RuntimeException(e);
            } finally {
                LOGGER.info("", StructuredArguments.value("context", ThreadLocalContext.toLog()));
                ThreadLocalContext.removeAll();
            }
        });
    }

}

