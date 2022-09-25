package com.podo.coinchatbot.schedule.alarm.targetalarm;


import com.podo.coinchatbot.external.CoinEndpointer;
import com.podo.coinchatbot.external.CoinEndpointerUtil;
import com.podo.coinchatbot.external.model.CoinResponse;
import com.podo.coinchatbot.app.domain.dto.UserTargetAlarmDto;
import com.podo.coinchatbot.app.domain.service.UserService;
import com.podo.coinchatbot.app.domain.service.UserTargetAlarmService;
import com.podo.coinchatbot.property.MarketConfig;
import com.podo.coinchatbot.telegram.CoinFormatter;
import com.podo.coinchatbot.core.Coin;
import com.podo.coinchatbot.core.Market;
import com.podo.coinchatbot.log.InstanceContext;
import com.podo.coinchatbot.log.ThreadLocalContext;
import com.podo.coinchatbot.telegram.TelegramMessageAlarmSender;
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

    private final ExecutorService executorService = Executors.newFixedThreadPool(3);
    private final UserService userService;
    private final TargetAlarmEachTargetExecutor targetAlarmEachTargetExcecutor;
    private final UserTargetAlarmService userTargetAlarmService;
    private final CoinEndpointer coinEndpointer;
    private final Map<Coin, CoinFormatter> coinToCoinFormatters;
    private final Map<Coin, TelegramMessageAlarmSender> coinToTelegramMessageSender;

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

        instanceContext.put("job.numberOfTarget", forFocusUpTargetAlarm.size() + forFocusDownTargetAlarm.size());
    }

    private void alarmEachTarget(InstanceContext instanceContext, Market market, BigDecimal currentPrice, Coin coin, UserTargetAlarmDto target) {
        CoinFormatter coinFormatter = coinToCoinFormatters.get(coin);
        TelegramMessageAlarmSender telegramMessageSender = coinToTelegramMessageSender.get(coin);

        executorService.submit(() -> {
            try {
                ThreadLocalContext.init("coin-target-alarm");
                ThreadLocalContext.put("job.id", instanceContext.getId());
                ThreadLocalContext.put("coin.id", coin);
                targetAlarmEachTargetExcecutor.alarmEachTarget(market, currentPrice, coinFormatter, telegramMessageSender, target);
            } catch (Exception e) {
                userService.increaseErrorCount(target.getUserId());
                ThreadLocalContext.putException(e);
            } finally {
                LOGGER.info("", StructuredArguments.value("context", ThreadLocalContext.toLog()));
                ThreadLocalContext.removeAll();
            }
        });
    }

}

