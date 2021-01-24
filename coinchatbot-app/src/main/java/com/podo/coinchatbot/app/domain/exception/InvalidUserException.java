package com.podo.coinchatbot.app.domain.exception;

import com.podo.coinchatbot.core.Coin;

public class InvalidUserException extends RuntimeException {
    public InvalidUserException(Coin coin, Long chatId) {
        super(String.format("찾을 수 없는 유저 정보 입니다. Coin : %s, ChatId : %s", coin, chatId));
    }

    public InvalidUserException(Long userId) {
        super("찾을 수 없는 유저ID 입니다 id : " + userId);
    }
}
