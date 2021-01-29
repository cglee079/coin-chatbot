package com.podo.coinchatbot.app.domain.repository;

import com.podo.coinchatbot.app.domain.model.User;
import com.podo.coinchatbot.core.Coin;
import com.podo.coinchatbot.core.Market;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByCoinAndChatId(Coin coin, Long telegramId);

    List<User> findByCoinAndMarketAndTimeloopAlarm(Coin coin, Market market, Integer timeloop);

    List<User> findByCoinAndMarketAndDayloopAlarm(Coin coin, Market market, Integer dayLoop);
}
