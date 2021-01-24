package com.podo.coinchatbot.app.domain.model;

import com.podo.coinchatbot.app.model.TargetFocus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class UserTargetAlarm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private BigDecimal targetPrice;

    @Enumerated(EnumType.STRING)
    private TargetFocus targetFocus;

    @CreatedDate
    private LocalDateTime createAt;

    @LastModifiedDate
    private LocalDateTime updateAt;

    public UserTargetAlarm(Long userId, BigDecimal targetPrice, TargetFocus targetFocus) {
        this.userId = userId;
        this.targetPrice = targetPrice;
        this.targetFocus = targetFocus;
    }

    public void updateTargetPrice(BigDecimal targetPrice) {
        this.targetPrice = targetPrice;
    }
}
