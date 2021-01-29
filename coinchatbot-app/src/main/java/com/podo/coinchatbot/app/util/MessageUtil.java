package com.podo.coinchatbot.app.util;

import com.podo.coinchatbot.app.telegram.command.MarketCommand;
import com.podo.coinchatbot.core.Language;
import com.podo.coinchatbot.core.Market;
import lombok.experimental.UtilityClass;

@UtilityClass
public class MessageUtil {

    public static String toMarketStr(Market market, Language language) {
        switch (language) {
            case KR:
                return MarketCommand.from(market).kr();
            case EN:
                return MarketCommand.from(market).en();
        }
        return null;
    }
}
