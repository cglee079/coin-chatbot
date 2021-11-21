package com.podo.coinchatbot.app.external.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
public class CoinResponse extends Throwable {

    private Boolean result;
    private String errorMessage;

    private BigDecimal volume;
    private BigDecimal openPrice;
    private BigDecimal lastPrice;
    private BigDecimal lowPrice;
    private BigDecimal highPrice;

    @Builder(builderClassName = "success", builderMethodName = "success")
    public CoinResponse(BigDecimal volume, BigDecimal openPrice, BigDecimal lastPrice, BigDecimal lowPrice, BigDecimal highPrice) {
        this.result = true;
        this.errorMessage = "";
        this.volume = volume;
        this.openPrice = openPrice;
        this.lastPrice = lastPrice;
        this.lowPrice = lowPrice;
        this.highPrice = highPrice;
    }

    public static CoinResponse error(String errorMessage) {
        CoinResponse coinResponse = new CoinResponse();
        coinResponse.result = false;
        coinResponse.errorMessage = errorMessage;
        return coinResponse;
    }

    public boolean isSuccess() {
        return result;
    }

    public BigDecimal getCurrentPrice() {
        return lastPrice;
    }

}
