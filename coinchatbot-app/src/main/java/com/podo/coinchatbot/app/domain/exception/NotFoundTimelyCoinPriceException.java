package com.podo.coinchatbot.app.domain.exception;

import com.podo.coinchatbot.core.Coin;
import com.podo.coinchatbot.core.Market;

import java.time.LocalDateTime;

public class NotFoundTimelyCoinPriceException extends RuntimeException {
    public NotFoundTimelyCoinPriceException(Coin coin, Market market, LocalDateTime localDateTime) {
        super(String.format("찾을 수 없는 코인 시간 정보 입니다 Coin : %s, Market : %s,DateTime : %s", coin, market, localDateTime));
    }
}
