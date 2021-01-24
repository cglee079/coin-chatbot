package com.podo.coinchatbot.app.domain.service;

import com.podo.coinchatbot.app.domain.model.UserTargetAlarm;
import com.podo.coinchatbot.app.domain.exception.InvalidUserTargetAlarmException;
import com.podo.coinchatbot.app.domain.repository.UserTargetAlarmRepository;
import com.podo.coinchatbot.app.domain.dto.UserTargetAlarmDto;
import com.podo.coinchatbot.app.domain.dto.UserTargetAlarmInsertDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserTargetAlarmService {

    private final UserTargetAlarmRepository userTargetAlarmRepository;

    public List<UserTargetAlarmDto> findByUserId(Long userId) {
        List<UserTargetAlarm> targets = userTargetAlarmRepository.findByUserId(userId);
        return targets.stream()
                .map(UserTargetAlarmDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void changeTargetPrice(Long id, BigDecimal targetPrie) {
        Optional<UserTargetAlarm> userTargetAlarmOptional = userTargetAlarmRepository.findById(id);

        UserTargetAlarm userTargetAlarm = userTargetAlarmOptional.orElseThrow(() -> new InvalidUserTargetAlarmException(id));

        userTargetAlarm.updateTargetPrice(targetPrie);
    }

    @Transactional
    public void insertNew(UserTargetAlarmInsertDto target) {
        userTargetAlarmRepository.save(target.toEntity());
    }

    @Transactional
    public void deleteByUserIdAndPrice(Long userId, BigDecimal targetPrice) {
        userTargetAlarmRepository.deleteByUserIdAndTargetPrice(userId, targetPrice);
    }

    @Transactional
    public void deleteByUserId(Long userId) {
        userTargetAlarmRepository.deleteByUserId(userId);
    }
}
