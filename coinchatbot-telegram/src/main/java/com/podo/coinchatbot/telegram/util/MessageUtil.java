package com.podo.coinchatbot.telegram.util;

import com.podo.coinchatbot.core.Lang;
import com.podo.coinchatbot.core.Market;
import com.podo.coinchatbot.telegram.command.MarketCommand;
import lombok.experimental.UtilityClass;

@UtilityClass
public class MessageUtil {

    public static String toMarketStr(Market market, Lang lang) {
        switch (lang) {
            case KR:
                return MarketCommand.from(market).kr();
            case EN:
                return MarketCommand.from(market).en();
        }
        return null;
    }
}
