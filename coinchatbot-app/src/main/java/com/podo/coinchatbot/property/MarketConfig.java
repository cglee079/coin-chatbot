package com.podo.coinchatbot.property;

import com.podo.coinchatbot.core.Market;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MarketConfig {
    private Market market;
    private String parameter;
    private Boolean isBtc;
    private Boolean enabled;
}
