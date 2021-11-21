package com.podo.coinchatbot.property;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class Example {
    private BigDecimal investKrw;
    private BigDecimal investUsd;
    private BigDecimal coinCount;
    private BigDecimal targetKrw;
    private BigDecimal targetUsd;
    private BigDecimal targetRate;
}
