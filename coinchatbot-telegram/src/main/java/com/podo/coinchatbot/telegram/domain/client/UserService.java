package com.podo.coinchatbot.telegram.domain.client;

import com.podo.coinchatbot.core.Coin;
import com.podo.coinchatbot.core.Market;
import com.podo.coinchatbot.telegram.model.UserDto;

import java.time.LocalDateTime;

public interface UserService {
    UserDto get(Coin coin, String telegramId);

    void updateMessageSendAt(Long userId, LocalDateTime messageReceiveAt);

    boolean createNewUser(Coin coin, String telegramId, String username, Market market);
}
