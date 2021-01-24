package com.podo.coinchatbot.app.telegram.exception;

public class UserInvalidInputException extends RuntimeException{

    public UserInvalidInputException(String message) {
        super(message);
    }
}
