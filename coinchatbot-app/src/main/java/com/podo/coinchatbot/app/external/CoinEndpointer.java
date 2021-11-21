package com.podo.coinchatbot.app.external;

import com.podo.coinchatbot.app.external.market.MarketApiClient;
import com.podo.coinchatbot.app.external.model.CoinResponse;
import com.podo.coinchatbot.core.Coin;
import com.podo.coinchatbot.core.Market;
import io.sentry.Sentry;
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
        try {
            return marketToApiClient.get(market).getCoin(coin);
        }catch (Exception e){
            Sentry.captureException(e);
            return CoinResponse.error("서버 에러 발생!");
        }
    }

}
