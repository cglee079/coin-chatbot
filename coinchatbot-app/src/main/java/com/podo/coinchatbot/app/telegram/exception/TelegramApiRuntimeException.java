package com.podo.coinchatbot.app.telegram.exception;

public class TelegramApiRuntimeException extends RuntimeException{
    public TelegramApiRuntimeException(Exception e, Long chatId, String message) {
        super(String.format("메세지를 전송 할 수 없었습니다. chatId : %s, MESSAGE : %s", chatId, message), e);
    }
}
