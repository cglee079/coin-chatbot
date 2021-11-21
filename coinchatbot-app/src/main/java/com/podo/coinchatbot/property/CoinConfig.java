package com.podo.coinchatbot.property;

import com.podo.coinchatbot.core.Coin;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CoinConfig {
    private Coin coin;
    private BotConfig botConfig;
    private List<MarketConfig> marketConfigs;
    private DigitConfig digitConfig;
    private Example example;
}
