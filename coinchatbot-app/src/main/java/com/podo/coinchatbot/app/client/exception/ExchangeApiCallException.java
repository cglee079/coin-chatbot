package com.podo.coinchatbot.app.client.exception;

public class ExchangeApiCallException extends RuntimeException{
    public ExchangeApiCallException(String errorMessage) {
        super(errorMessage);
    }
}
