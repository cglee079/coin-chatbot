package com.podo.coinchatbot.app.client;

import com.podo.coinchatbot.app.client.market.MarketApiClient;
import com.podo.coinchatbot.app.client.model.CoinResponse;
import com.podo.coinchatbot.core.Coin;
import com.podo.coinchatbot.core.Market;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CoinEndpointer {

    private final Map<Market, MarketApiClient> marketToApiClient;

    public CoinEndpointer(List<MarketApiClient> marketApiClients) {
        this.marketToApiClient = marketApiClients.stream()
                .collect(Collectors.toMap(MarketApiClient::getMarket, m -> m));
    }

    public CoinResponse getCoin(Coin coin, Market market) {
        return marketToApiClient.get(market).getCoin(coin);
    }

}
