package com.podo.coinchatbot.app.config;

import com.podo.coinchatbot.app.domain.service.UserService;
import com.podo.coinchatbot.app.model.CoinMeta;
import com.podo.coinchatbot.app.model.Menu;
import com.podo.coinchatbot.app.telegram.CoinFormatter;
import com.podo.coinchatbot.app.telegram.TelegramMessageReceiver;
import com.podo.coinchatbot.app.telegram.TelegramMessageReceiverHandler;
import com.podo.coinchatbot.app.telegram.TelegramMessageSender;
import com.podo.coinchatbot.app.telegram.menu.MenuHandler;
import com.podo.coinchatbot.core.Coin;
import com.podo.coinchatbot.core.Market;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class TelegramAppInitializer {

    public TelegramAppInitializer(ConfigurableListableBeanFactory beanFactory, CoinProperties coinProperties, UserService userService, Map<Menu, MenuHandler> menuHandlers) {
        for (CoinProperties.CoinProperty coinProperty : coinProperties.getProperties()) {
            Coin coin = Coin.valueOf(coinProperty.getId());
            CoinProperties.CoinProperty.BotConfig botConfig = coinProperty.getBotConfig();
            String botToken = botConfig.getToken();
            String botUsername = botConfig.getUsername();
            Boolean botEnabled = botConfig.getEnabled();

            if (Boolean.FALSE.equals(botEnabled)) {
                continue;
            }

            List<CoinProperties.CoinProperty.MarketConfig> marketConfigs = coinProperty.getMarketConfigs();

            List<Market> markets = marketConfigs.stream()
                    .map(CoinProperties.CoinProperty.MarketConfig::getId)
                    .map(Market::valueOf)
                    .collect(Collectors.toList());

            List<Market> btcMarkets = marketConfigs.stream()
                    .filter(CoinProperties.CoinProperty.MarketConfig::getIsBtc)
                    .map(CoinProperties.CoinProperty.MarketConfig::getId)
                    .map(Market::valueOf)
                    .collect(Collectors.toList());

            CoinMeta coinMeta = new CoinMeta(btcMarkets, markets, createCoinFormatter(coinProperty.getDigitConfig()), createCoinExample(coinProperty.getExample()));

            TelegramMessageSender telegramMessageSender = new TelegramMessageSender(coin, botToken);
            TelegramMessageReceiverHandler telegramMessageReceiverHandler = new TelegramMessageReceiverHandler(coin, menuHandlers, coinMeta, userService, telegramMessageSender);
            TelegramMessageReceiver telegramMessageReceiver = new TelegramMessageReceiver(botToken, botUsername, telegramMessageReceiverHandler);

            beanFactory.registerSingleton(coin.name() + TelegramMessageReceiver.class.getName(), telegramMessageReceiver);
        }
    }

    private CoinMeta.Example createCoinExample(CoinProperties.CoinProperty.Example example) {
        return CoinMeta.Example.builder()
                .coinCount(example.getCoinCount())
                .investKRW(example.getInvestKrw())
                .investUSD(example.getInvestUsd())
                .targetKRW(example.getTargetKrw())
                .targetUSD(example.getInvestUsd())
                .targetRate(example.getTargetRate())
                .build();
    }

    private CoinFormatter createCoinFormatter(CoinProperties.CoinProperty.DigitConfig digitConfig) {
        return new CoinFormatter(digitConfig.getKrw(), digitConfig.getUsd(), digitConfig.getUsd());
    }



}
