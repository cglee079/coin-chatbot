package com.podo.coinchatbot.app.job.alarm;

import com.podo.coinchatbot.app.property.CoinConfig;
import com.podo.coinchatbot.app.property.CoinConfigs;
import com.podo.coinchatbot.app.property.DigitConfig;
import com.podo.coinchatbot.app.property.MarketConfig;
import com.podo.coinchatbot.app.telegram.CoinFormatter;
import com.podo.coinchatbot.app.telegram.TelegramMessageSender;
import com.podo.coinchatbot.core.Coin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
public class AlarmJobConfig {

    @Bean
    public Map<Coin, CoinFormatter> coinToCoinFormatters(CoinConfigs coinConfigs) {
        return coinConfigs.getProperties()
                .stream()
                .collect(Collectors.toMap(CoinConfig::getCoin, c -> createCoinFormatter(c.getDigitConfig())));
    }

    private CoinFormatter createCoinFormatter(DigitConfig digitConfig) {
        return new CoinFormatter(digitConfig.getKrw(), digitConfig.getUsd(), digitConfig.getUsd());
    }

    @Bean
    public Map<Coin, TelegramMessageSender> coinToTelegramMessageSender(CoinConfigs coinConfigs) {
        Map<Coin, TelegramMessageSender> coinToTelegramMessageSender = new EnumMap<>(Coin.class);
        coinConfigs.getProperties()
                .stream()
                .filter(c -> c.getBotConfig().getEnabled())
                .forEach(c ->coinToTelegramMessageSender.put(c.getCoin(), new TelegramMessageSender(c.getBotConfig().getToken())));

        return coinToTelegramMessageSender;
    }

    @Bean
    public Map<Coin, List<MarketConfig>> coinToEnabledMarketConfigs(CoinConfigs coinConfigs) {

        Map<Coin, List<MarketConfig>> coinToEnabledMarketConfigs = new HashMap<>();

        for (CoinConfig property : coinConfigs.getProperties()) {

            if (!property.getBotConfig().getEnabled()) {
                continue;
            }

            List<MarketConfig> enableMarketConfigs = property.getMarketConfigs().stream()
                    .filter(MarketConfig::getEnabled)
                    .collect(Collectors.toList());

            coinToEnabledMarketConfigs.put(property.getCoin(), enableMarketConfigs);
        }

        return coinToEnabledMarketConfigs;

    }
}
