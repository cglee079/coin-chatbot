package com.podo.coinchatbot.app.domain.repository;

import com.podo.coinchatbot.app.domain.model.TimelyCoinPrice;
import com.podo.coinchatbot.core.Coin;
import com.podo.coinchatbot.core.Market;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface TimelyCoinPriceRepository extends JpaRepository<TimelyCoinPrice, Long> {

    Optional<TimelyCoinPrice> findByCoinAndMarketAndDateTime(Coin coin, Market market, LocalDateTime dateTime);
}
