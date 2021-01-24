package com.podo.coinchatbot.app.config.initializer;

import com.podo.coinchatbot.app.config.CoinProperties;
import com.podo.coinchatbot.app.domain.service.UserTargetAlarmService;
import com.podo.coinchatbot.app.telegram.CoinFormatter;
import com.podo.coinchatbot.app.telegram.TelegramMessageSender;
import com.podo.coinchatbot.core.Coin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
public class JobAlarmInitializer {

    @Bean
    public Map<Coin, List<CoinProperties.CoinProperty.MarketConfig>> coinToMarketConfigs(CoinProperties coinProperties) {
        return coinProperties.getProperties()
                .stream()
                .collect(Collectors.toMap(c -> Coin.valueOf(c.getId()), CoinProperties.CoinProperty::getMarketConfigs));
    }

    @Bean
    public Map<Coin, CoinFormatter> coinToCoinFormatters(CoinProperties coinProperties) {
        return coinProperties.getProperties()
                .stream()
                .collect(Collectors.toMap(c -> Coin.valueOf(c.getId()), c -> createCoinFormatter(c.getDigitConfig())));
    }

    private CoinFormatter createCoinFormatter(CoinProperties.CoinProperty.DigitConfig digitConfig) {
        return new CoinFormatter(digitConfig.getKrw(), digitConfig.getUsd(), digitConfig.getUsd());
    }

    @Bean
    public Map<Coin, TelegramMessageSender> coinToTelegramMessageSender(List<TelegramMessageSender> telegramMessageSenders) {
        return telegramMessageSenders.stream()
                .collect(Collectors.toMap(TelegramMessageSender::getCoin, t -> t));
    }
}
