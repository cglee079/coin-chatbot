package com.podo.coinchatbot.epclient.exception;

public class ExchangeApiCallException extends RuntimeException{
    public ExchangeApiCallException(String errorMessage) {
        super(errorMessage);
    }
}
