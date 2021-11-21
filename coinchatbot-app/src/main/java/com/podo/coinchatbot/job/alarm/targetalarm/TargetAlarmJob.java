package com.podo.coinchatbot.job.alarm.targetalarm;


import com.podo.coinchatbot.job.Job;
import com.podo.coinchatbot.property.MarketConfig;
import com.podo.coinchatbot.util.DateTimeUtil;
import com.podo.coinchatbot.core.Coin;
import com.podo.coinchatbot.log.InstanceContext;
import lombok.RequiredArgsConstructor;
import net.logstash.logback.argument.StructuredArguments;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class TargetAlarmJob implements Job {

    private final TargetAlarmEachCoinExecutor targetAlarmEachCoinExecutor;
    private final Map<Coin, List<MarketConfig>> coinToEnabledMarketConfigs;
    private static final Logger LOGGER = LoggerFactory.getLogger("JOB_LOGGER");

    @Scheduled(cron = "30 0/5 * * * *")
    @Override
    public void run() {
        InstanceContext instanceContext = new InstanceContext("target-alarm-job");
        instanceContext.put("job.startAt", DateTimeUtil.toFullContextString(LocalDateTime.now()));

        for (Coin coin : coinToEnabledMarketConfigs.keySet()) {
            List<MarketConfig> marketConfigs = coinToEnabledMarketConfigs.get(coin);
            for (MarketConfig marketConfig : marketConfigs) {
                try {
                    targetAlarmEachCoinExecutor.alarmEachCoin(instanceContext, coin, marketConfig);
                } catch (Exception e) {
                    instanceContext.putException(e);
                }
            }
        }

        instanceContext.put("job.endAt", DateTimeUtil.toFullContextString(LocalDateTime.now()));

        LOGGER.info("", StructuredArguments.value("context", instanceContext.toLog()));
    }

}

