package com.podo.coinchatbot.app.external.exception;

public class ExchangeApiCallException extends RuntimeException {
    public ExchangeApiCallException(String errorMessage) {
        super(errorMessage);
    }
}
