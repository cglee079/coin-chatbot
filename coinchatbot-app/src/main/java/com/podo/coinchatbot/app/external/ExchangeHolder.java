package com.podo.coinchatbot.app.external;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ExchangeHolder {

    private final ExchangeApiClient exchangeApiClient;
    private final BigDecimal exchangeRate;

    public ExchangeHolder(ExchangeApiClient exchangeApiClient) {
        this.exchangeApiClient = exchangeApiClient;
        this.exchangeRate = exchangeApiClient.usdToKrw();
    }

    public BigDecimal getCurrentExchangeRate() {
        return exchangeRate;
    }
}

