package com.podo.coinchatbot.app.client.exception;

public class MarketApiCallException extends RuntimeException {

    public MarketApiCallException(String msg) { // 생성자
        super(msg);
    }

    public String getTelegramMsg() {
        return this.getMessage();
    }

}
