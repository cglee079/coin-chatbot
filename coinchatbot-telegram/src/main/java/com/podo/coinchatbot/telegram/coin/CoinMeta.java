package com.podo.coinchatbot.telegram.coin;

import com.podo.coinchatbot.core.Market;
import com.podo.coinchatbot.telegram.util.NumberFormatter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class CoinMeta {

    private final Map<Market, Boolean> isBTCMarket;

    @Getter
    private final List<Market> enableMarkets;

    @Getter
    private final NumberFormatter numberFormatter;

    public boolean isBTCMarket(Market market) {
        return isBTCMarket.get(market);
    }
}
