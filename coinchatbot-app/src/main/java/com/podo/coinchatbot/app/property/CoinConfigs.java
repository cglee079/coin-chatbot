package com.podo.coinchatbot.app.property;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ConfigurationProperties("coin")
public class CoinConfigs {
    private List<CoinConfig> properties = new ArrayList<>();
}
