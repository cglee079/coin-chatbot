package com.podo.coinchatbot.external.exception;

public class ExchangeApiCallException extends RuntimeException {
    public ExchangeApiCallException(String errorMessage) {
        super(errorMessage);
    }
}
