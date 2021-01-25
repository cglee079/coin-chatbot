package com.podo.coinchatbot.app.property;

import com.podo.coinchatbot.core.Coin;
import com.podo.coinchatbot.core.Market;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
