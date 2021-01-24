package com.podo.coinchatbot.app.domain.repository;

import com.podo.coinchatbot.app.domain.model.UserTargetAlarm;
import com.podo.coinchatbot.app.model.TargetFocus;
import com.podo.coinchatbot.core.Coin;
import com.podo.coinchatbot.core.Market;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface UserTargetAlarmRepository extends JpaRepository<UserTargetAlarm, Long> {
    List<UserTargetAlarm> findByUserId(Long userId);

    int deleteByUserIdAndTargetPrice(Long userId, BigDecimal targetPrice);

    int deleteByUserId(Long userId);

    @Query("select t from UserTargetAlarm t inner join User u on t.userId = u.id" +
            " where u.coin = :coin and u.market = :market and t.targetPrice <= :currentPrice and t.targetFocus = 'UP'")
    List<UserTargetAlarm> findForFocusUpTargetAlarm(Coin coin, Market market, BigDecimal currentPrice);

    @Query("select t from UserTargetAlarm t inner join User u on t.userId = u.id" +
            " where u.coin = :coin and u.market = :market and t.targetPrice >= :currentPrice and t.targetFocus = 'DOWN'")
    List<UserTargetAlarm> findForFocusDownTargetAlarm(Coin coin, Market market, BigDecimal currentPrice);

}
