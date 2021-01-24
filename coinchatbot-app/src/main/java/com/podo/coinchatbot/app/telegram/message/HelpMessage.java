package com.podo.coinchatbot.app.telegram.message;

import com.podo.coinchatbot.core.Coin;
import com.podo.coinchatbot.core.Language;
import com.podo.coinchatbot.core.Market;
import com.podo.coinchatbot.app.telegram.command.MainCommand;
import com.podo.coinchatbot.app.telegram.exception.InvalidUserLanguageException;
import com.podo.coinchatbot.app.util.MessageUtil;
import lombok.experimental.UtilityClass;

import java.util.List;

import static com.podo.coinchatbot.core.Language.KR;

@UtilityClass
public class HelpMessage {

    public static String explainForForeigner() {
        StringBuilder message = new StringBuilder();
        message.append("★  If you are not Korean, Must read!!\n");
        message.append("* Use the " + MainCommand.PREFERENCE.getCommand(Language.EN) + " Menu.\n");
        message.append("* First. Please set language to English.\n");
        message.append("* Second. Set the time adjustment for accurate notifications. Because of time difference by country.\n");
        return message.toString();
    }


    public static String help(Language language, Coin coin, List<Market> enabledMarkets) {
        if (language.equals(KR)) {
            return kr(enabledMarkets, coin, language);
        } else if (language.equals(Language.EN)) {
            return en(enabledMarkets, coin, language);
        }

        throw new InvalidUserLanguageException();
    }

    private static String kr(List<Market> enabledMarkets, Coin coin, Language language) {
        StringBuilder message = new StringBuilder();
        message.append(coin + " 알리미\n");
        message.append("\n");
        message.append("별도의 시간 알림 주기 설정을 안하셨다면,\n");
        message.append("3시간 주기로 " + coin + " 가격 알림이 전송됩니다.\n");
        message.append("\n");
        message.append("별도의 일일 알림 주기 설정을 안하셨다면,\n");
        message.append("1일 주기로 거래량, 상한가, 하한가, 종가가 비교되어 전송됩니다.\n");
        message.append("\n");
        message.append("별도의 거래소 설정을 안하셨다면,\n");

        //
        message.append(MessageUtil.toMarketStr(enabledMarkets.get(0), language) + " 기준의 정보가 전송됩니다.\n");
        message.append("\n");
        message.append("투자금액,코인개수를 설정하시면,\n");
        message.append("원금, 현재금액, 손익금을 확인 하실 수 있습니다.\n");
        message.append("\n");
        message.append("목표가격을 설정하시면,\n");
        message.append("목표가격이 되었을때 알림을 받을 수 있습니다.\n");
        message.append("목표가격을 위한 가격정보는 각 거래소에서 1분 주기로 갱신됩니다.\n");
        message.append("\n");
        message.append("프리미엄 정보를 확인 하실 수 있습니다.\n");
        message.append("\n");
        message.append("비트코인대비 변화량을 확인 하실 수 있습니다.\n");
        message.append("\n");

        message.append("거래소 By ");
        for (int i = 0; i < enabledMarkets.size(); i++) {
            message.append(MessageUtil.toMarketStr(enabledMarkets.get(i), language) + ", ");
        }
        message.append("\n");
        message.append("환율정보 By the European Central Bank\n");
        message.append("\n");
        message.append("Developed By podo ( cglee079@gmail.com )\n");

        return message.toString();
    }

    private static String en(List<Market> enabledMarkets, Coin coin, Language language) {
        StringBuilder message = new StringBuilder();

        message.append(coin + " Coin Noticer\n");
        message.append("\n");
        message.append("If you are using this service for the first time,\n");
        message.append(coin + " price are sent every 3 hours.\n");
        message.append("\n");
        message.append("If you are using this service for the first time,\n");
        message.append(coin + " price are sent every 1 days. (with high, low, last price and volume)\n");
        message.append("\n");
        message.append("If you are using this service for the first time,\n");
        message.append("Information based on ");
        //

        message.append(MessageUtil.toMarketStr(enabledMarkets.get(0), language) + "  market will be sent.\n");
        message.append("\n");
        message.append("If you set the amount of investment and the number of coins,\n");
        message.append("you can check the current amount of profit and loss.\n");
        message.append("\n");
        message.append("If you set Target price,\n");
        message.append("Once you reach the target price, you will be notified.\n");
        message.append("Coin price information is updated every 1 minute.\n");
        message.append("\n");
        message.append("You can check the coin price on each market\n");
        message.append("\n");
        message.append("You can check coin price change rate compared to BTC\n");
        message.append("\n");

        message.append("* Markets : ");
        for (Market enabledMarket : enabledMarkets) {
            message.append(MessageUtil.toMarketStr(enabledMarket, language) + ", ");
        }
        message.append("\n");
        message.append("* Exchange Rate Information By the European Central Bank\n");
        message.append("\n");
        message.append("Developed By CGLEE ( cglee079@gmail.com )\n");

        return message.toString();
    }
}
