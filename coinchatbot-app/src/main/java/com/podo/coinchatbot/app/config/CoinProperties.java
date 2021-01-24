package com.podo.coinchatbot.app.config;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ConfigurationProperties("coin")
public class CoinProperties {

    private List<CoinProperty> properties = new ArrayList<>();

    @Getter
    @Setter
    public static class CoinProperty {
        private String id;
        private BotConfig botConfig;
        private List<MarketConfig> marketConfigs;
        private DigitConfig digitConfig;
        private Example example;

        @Getter
        @Setter
        public static class BotConfig {
            private String token;
            private String username;
            private String url;
            private Boolean enabled;
        }

        @Getter
        @Setter
        public static class MarketConfig {
            private String id;
            private String parameter;
            private Boolean isBtc;
            private Boolean enabled;
        }

        @Getter
        @Setter
        public static class DigitConfig {
            private Integer krw;
            private Integer usd;
            private Integer btc;
        }

        @Getter
        @Setter
        public static class Example {
            private BigDecimal investKrw;
            private BigDecimal investUsd;
            private BigDecimal coinCount;
            private BigDecimal targetKrw;
            private BigDecimal targetUsd;
            private BigDecimal targetRate;
        }


    }



}
