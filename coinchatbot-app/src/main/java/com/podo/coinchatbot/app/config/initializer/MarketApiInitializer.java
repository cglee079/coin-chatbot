package com.podo.coinchatbot.app.config.initializer;

import com.podo.coinchatbot.app.client.market.MarketApiClient;
import com.podo.coinchatbot.app.config.CoinProperties;
import com.podo.coinchatbot.core.Coin;
import com.podo.coinchatbot.core.Market;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class MarketApiInitializer {

    public MarketApiInitializer(List<MarketApiClient> marketApiClients, CoinProperties coinProperties) {
        Map<Market, MarketApiClient> marketToMarketApiClient = marketApiClients.stream()
                .collect(Collectors.toMap(MarketApiClient::getMarket, m -> m));

        Map<Market, Map<Coin, String>> marketToParameters = marketApiClients.stream()
                .collect(Collectors.toMap(MarketApiClient::getMarket, m -> new HashMap<>()));

        for (CoinProperties.CoinProperty coinProperty : coinProperties.getProperties()) {
            Coin coin = Coin.valueOf(coinProperty.getId());
            for (CoinProperties.CoinProperty.MarketConfig marketConfig : coinProperty.getMarketConfigs()) {
                Map<Coin, String> coinToParameter = marketToParameters.get(Market.valueOf(marketConfig.getId()));
                coinToParameter.put(coin, marketConfig.getParameter());
            }
        }

        for (Market market : marketToMarketApiClient.keySet()) {
            MarketApiClient marketApiClient = marketToMarketApiClient.get(market);
            marketApiClient.init(marketToParameters.get(market));
        }

    }

}
