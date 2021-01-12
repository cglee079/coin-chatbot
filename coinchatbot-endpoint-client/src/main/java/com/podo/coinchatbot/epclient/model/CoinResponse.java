package com.podo.coinchatbot.epclient.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CoinResponse extends Throwable {

    private Boolean result;
    private String errorMessage;

    private Double volume;
    private Double openPrice;
    private Double lastPrice;
    private Double lowPrice;
    private Double highPrice;

    @Builder(builderClassName = "success", builderMethodName = "success")
    public CoinResponse(Double volume, Double openPrice, Double lastPrice, Double lowPrice, Double highPrice) {
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
}
