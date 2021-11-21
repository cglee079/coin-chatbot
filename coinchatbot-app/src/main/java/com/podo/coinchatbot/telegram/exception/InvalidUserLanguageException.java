package com.podo.coinchatbot.telegram.exception;

public class InvalidUserLanguageException extends InternalServerException {

    public InvalidUserLanguageException() {
        super("알 수 없는 언어 입니다.");
    }
}
