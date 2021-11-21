package com.podo.coinchatbot.app.external;

import com.podo.coinchatbot.app.external.market.MarketApiClient;
import com.podo.coinchatbot.app.property.CoinConfig;
import com.podo.coinchatbot.app.property.CoinConfigs;
import com.podo.coinchatbot.app.property.MarketConfig;
import com.podo.coinchatbot.core.Coin;
import com.podo.coinchatbot.core.Market;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class MarketApiInitializer {

    public MarketApiInitializer(List<MarketApiClient> marketApiClients, CoinConfigs coinConfigs) {
        initMarketApiParameter(marketApiClients, coinConfigs);
    }

    // 각 Market Api Client에 코인별 파라미터를 초기화 시킨다.
    private void initMarketApiParameter(List<MarketApiClient> marketApiClients, CoinConfigs coinConfigs) {
        Map<Market, MarketApiClient> marketToMarketApiClient = marketApiClients.stream()
                .collect(Collectors.toMap(MarketApiClient::getMarket, m -> m));

        Map<Market, Map<Coin, String>> marketToCoinParameters = marketApiClients.stream()
                .collect(Collectors.toMap(MarketApiClient::getMarket, m -> new HashMap<>()));

        for (CoinConfig coinProperty : coinConfigs.getProperties()) {
            for (MarketConfig marketConfig : coinProperty.getMarketConfigs()) {
                Map<Coin, String> coinToParameter = marketToCoinParameters.get(marketConfig.getMarket());
                coinToParameter.put(coinProperty.getCoin(), marketConfig.getParameter());
            }
        }

        for (Market market : marketToMarketApiClient.keySet()) {
            MarketApiClient marketApiClient = marketToMarketApiClient.get(market);
            marketApiClient.init(marketToCoinParameters.get(market));
        }
    }

}
