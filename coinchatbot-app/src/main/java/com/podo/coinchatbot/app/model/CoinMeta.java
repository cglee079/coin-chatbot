package com.podo.coinchatbot.app.model;

import com.podo.coinchatbot.telegram.CoinFormatter;
import com.podo.coinchatbot.core.Market;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
public class CoinMeta {

    private final List<Market> btcMarkets;

    @Getter
    private final List<Market> enableMarkets;

    @Getter
    private final CoinFormatter coinFormatter;

    @Getter
    private final Example example;

    public boolean isBTCMarket(Market market) {
        return btcMarkets.contains(market);
    }

    public Market getFirstEnableMarkets() {
        return enableMarkets.get(0);
    }

    @Builder
    @Getter
    public static class Example {
        private BigDecimal investKRW;
        private BigDecimal investUSD;
        private BigDecimal coinCount;
        private BigDecimal targetKRW;
        private BigDecimal targetUSD;
        private BigDecimal targetRate;
    }


}
