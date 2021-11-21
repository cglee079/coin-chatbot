package com.podo.coinchatbot.job.alarm.timelyalarm;

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

@RequiredArgsConstructor
@Component
public class TimelyAlarmJob implements Job {

    private static final Logger LOGGER = LoggerFactory.getLogger("JOB_LOGGER");

    private final HourlyAlarmExecutor hourlyAlarmExecutor;
    private final DailyAlarmExecutor dailyAlarmExecutor;
    private final TimelyCoinPricePersistExecutor timelyCoinPricePersistExecutor;
    private final Map<Coin, List<MarketConfig>> coinToEnableMarketConfigs;

    @Scheduled(cron = "02 00 0/1 * * *")
    public void run() {
        InstanceContext instanceContext = new InstanceContext("timely-alarm-job");

        try {
            LocalDateTime now = LocalDateTime.now();
            instanceContext.put("job.startAt", DateTimeUtil.toFullContextString(now));
            doRun(instanceContext, now);
        } catch (Exception e) {
            instanceContext.putException(e);
        } finally {
            instanceContext.put("job.endAt", DateTimeUtil.toFullContextString(LocalDateTime.now()));
            LOGGER.info("", StructuredArguments.value("context", instanceContext.toLog()));
        }
    }

    private void doRun(InstanceContext instanceContext, LocalDateTime now) {

        for (Coin coin : coinToEnableMarketConfigs.keySet()) {
            for (MarketConfig marketConfig : coinToEnableMarketConfigs.get(coin)) {
                try {
                    timelyCoinPricePersistExecutor.saveCoinPrice(coin, now, marketConfig.getMarket());
                } catch (Exception e) {
                    instanceContext.putException(e);
                }
            }
        }

        // 사용자에게 시간별 알림 메세지를 전송
        for (Coin coin : coinToEnableMarketConfigs.keySet()) {
            for (int timeloop = 1; timeloop <= 12; timeloop++) {
                if (now.getHour() % timeloop == 0) {
                    for (MarketConfig marketConfig : coinToEnableMarketConfigs.get(coin)) {
                        try {
                            hourlyAlarmExecutor.sendHourlyAlarm(coin, marketConfig, timeloop, now);
                        } catch (Exception e) {
                            instanceContext.putException(e);
                        }
                    }
                }
            }
        }

        // 사용자에게 일일 알림 메세지를 전송
        for (Coin coin : coinToEnableMarketConfigs.keySet()) {
            for (int dayloop = 1; dayloop <= 7; dayloop++) {
                if (now.getDayOfMonth() % dayloop == 0) {
                    for (MarketConfig marketConfig : coinToEnableMarketConfigs.get(coin)) {
                        try {
                            dailyAlarmExecutor.sendDailyAlarm(coin, marketConfig, dayloop, now);
                        } catch (Exception e) {
                            instanceContext.putException(e);
                        }
                    }
                }
            }
        }

    }


}
