package com.podo.coinchatbot.app.job;


import com.podo.coinchatbot.app.client.CoinEndpointer;
import com.podo.coinchatbot.app.client.CoinEndpointerUtil;
import com.podo.coinchatbot.app.client.model.CoinResponse;
import com.podo.coinchatbot.app.config.CoinProperties;
import com.podo.coinchatbot.app.domain.dto.UserDto;
import com.podo.coinchatbot.app.domain.dto.UserTargetAlarmDto;
import com.podo.coinchatbot.app.domain.service.UserService;
import com.podo.coinchatbot.app.domain.service.UserTargetAlarmService;
import com.podo.coinchatbot.app.telegram.CoinFormatter;
import com.podo.coinchatbot.app.telegram.MessageContext;
import com.podo.coinchatbot.app.telegram.TelegramMessageSender;
import com.podo.coinchatbot.app.telegram.exception.InvalidUserLanguageException;
import com.podo.coinchatbot.app.telegram.model.MessageVo;
import com.podo.coinchatbot.app.telegram.model.SendMessageVo;
import com.podo.coinchatbot.core.Coin;
import com.podo.coinchatbot.core.Language;
import com.podo.coinchatbot.core.Market;
import lombok.RequiredArgsConstructor;
import net.logstash.logback.argument.StructuredArguments;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class TargetAlarmJob implements Job {

    private final TargetAlarmEachCoin targetAlarmEachCoin;
    private final Map<Coin, List<CoinProperties.CoinProperty.MarketConfig>> coinToMarketConfigs;
    private static final Logger LOGGER = LoggerFactory.getLogger("JOB_LOGGER");

    @Scheduled(cron = "30 0/5 * * * *")
    @Override
    public void run() {
        TargetAlarmJobContext targetAlarmJobContext = new TargetAlarmJobContext();
        targetAlarmJobContext.putDateTime("jobStartAt", LocalDateTime.now());

        for (Coin coin : coinToMarketConfigs.keySet()) {
            List<CoinProperties.CoinProperty.MarketConfig> marketConfigs = coinToMarketConfigs.get(coin);
            for (CoinProperties.CoinProperty.MarketConfig marketConfig : marketConfigs) {
                try {
                    targetAlarmEachCoin.alarmEachCoin(targetAlarmJobContext, coin, marketConfig);
                } catch (Exception e) {
                    targetAlarmJobContext.putException(e);
                }
            }
        }

        targetAlarmJobContext.putDateTime("jobEndAt", LocalDateTime.now());

        LOGGER.info("", StructuredArguments.value("context", targetAlarmJobContext.toLog()));
    }

}

