package com.podo.coinchatbot.app.client.exception;

import com.podo.coinchatbot.core.Coin;

public class CoinApiCallException extends RuntimeException {

    public CoinApiCallException(Coin coin, String errorMessage) {
        super(coin.name() + " 정보를 가져올수 없습니다, ERROR : " + errorMessage);
    }

}
