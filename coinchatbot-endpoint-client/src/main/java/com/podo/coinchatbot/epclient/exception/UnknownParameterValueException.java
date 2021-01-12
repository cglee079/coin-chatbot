package com.podo.coinchatbot.epclient.exception;

public class UnknownParameterValueException extends RuntimeException{
    public UnknownParameterValueException(String param) {
        super("찾을 수 없는 parameter 입니다 : " + param);

    }
}
