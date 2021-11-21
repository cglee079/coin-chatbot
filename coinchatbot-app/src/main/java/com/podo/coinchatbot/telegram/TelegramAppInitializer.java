package com.podo.coinchatbot.telegram;

import com.podo.coinchatbot.app.domain.service.UserService;
import com.podo.coinchatbot.app.model.CoinMeta;
import com.podo.coinchatbot.app.model.Menu;
import com.podo.coinchatbot.property.BotConfig;
import com.podo.coinchatbot.property.CoinConfig;
import com.podo.coinchatbot.property.CoinConfigs;
import com.podo.coinchatbot.property.DigitConfig;
import com.podo.coinchatbot.property.Example;
import com.podo.coinchatbot.property.MarketConfig;
import com.podo.coinchatbot.telegram.menu.MenuHandler;
import com.podo.coinchatbot.core.Coin;
import com.podo.coinchatbot.core.Market;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Primary
@Component
public class TelegramAppInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger("DEBUG_LOGGER");

    public TelegramAppInitializer(ConfigurableListableBeanFactory beanFactory, CoinConfigs coinConfigs, UserService userService, Map<Menu, MenuHandler> menuHandlers) {
        for (CoinConfig coinConfig : coinConfigs.getProperties()) {
            initEachCoin(beanFactory, userService, menuHandlers, coinConfig);
        }

    }

    private void initEachCoin(ConfigurableListableBeanFactory beanFactory, UserService userService, Map<Menu, MenuHandler> menuHandlers, CoinConfig coinConfig) {
        Coin coin = coinConfig.getCoin();
        BotConfig botConfig = coinConfig.getBotConfig();
        String botToken = botConfig.getToken();
        String botUsername = botConfig.getUsername();
        Boolean botEnabled = botConfig.getEnabled();

        if (Boolean.FALSE.equals(botEnabled)) {
            return;
        }

        List<MarketConfig> marketConfigs = coinConfig.getMarketConfigs();
        List<Market> markets = createEnabledMarkets(marketConfigs);
        List<Market> btcMarkets = createBtcMarkets(marketConfigs);

        CoinMeta coinMeta = new CoinMeta(btcMarkets, markets, createCoinFormatter(coinConfig.getDigitConfig()), createCoinExample(coinConfig.getExample()));

        TelegramMessageSender telegramMessageSender = new TelegramMessageSender(botToken);
        TelegramMessageReceiverHandler telegramMessageReceiverHandler = new TelegramMessageReceiverHandler(coin, menuHandlers, coinMeta, userService, telegramMessageSender);
        TelegramMessageReceiver telegramMessageReceiver = new TelegramMessageReceiver(botToken, botUsername, telegramMessageReceiverHandler);

        beanFactory.registerSingleton(coin.name() + TelegramMessageReceiver.class.getName(), telegramMessageReceiver);

        LOGGER.info("{} 초기화 완료", coin);
    }

    private List<Market> createEnabledMarkets(List<MarketConfig> marketConfigs) {
        return marketConfigs.stream()
                .filter(MarketConfig::getEnabled)
                .map(MarketConfig::getMarket)
                .collect(Collectors.toList());
    }

    private List<Market> createBtcMarkets(List<MarketConfig> marketConfigs) {
        return marketConfigs.stream()
                .filter(MarketConfig::getEnabled)
                .filter(MarketConfig::getIsBtc)
                .map(MarketConfig::getMarket)
                .collect(Collectors.toList());
    }

    private CoinMeta.Example createCoinExample(Example example) {
        return CoinMeta.Example.builder()
                .coinCount(example.getCoinCount())
                .investKRW(example.getInvestKrw())
                .investUSD(example.getInvestUsd())
                .targetKRW(example.getTargetKrw())
                .targetUSD(example.getInvestUsd())
                .targetRate(example.getTargetRate())
                .build();
    }

    private CoinFormatter createCoinFormatter(DigitConfig digitConfig) {
        return new CoinFormatter(digitConfig.getKrw(), digitConfig.getUsd(), digitConfig.getUsd());
    }

}
