package com.podo.coinchatbot.app.domain.repository;

import com.podo.coinchatbot.app.domain.model.UserTargetAlarm;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;

public interface UserTargetAlarmRepository extends JpaRepository<UserTargetAlarm, Long> {
    List<UserTargetAlarm> findByUserId(Long userId);

    int deleteByUserIdAndTargetPrice(Long userId, BigDecimal targetPrice);

    int deleteByUserId(Long userId);
}
