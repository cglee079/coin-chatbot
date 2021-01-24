package com.podo.coinchatbot.app.domain.repository;

import com.podo.coinchatbot.core.Coin;
import com.podo.coinchatbot.app.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByCoinAndChatId(Coin coin, Long telegramId);
}
