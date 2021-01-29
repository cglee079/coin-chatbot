package com.podo.coinchatbot.app.telegram.command;


import com.podo.coinchatbot.core.Language;
import com.podo.coinchatbot.core.Market;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MarketCommand implements CommandEnum {
    NULL(null, "", ""),
    COINONE(Market.COINONE, "코인원", "Coinone"),
    BITHUMB(Market.BITHUMB, "빗썸", "Bithumb"),
    UPBIT(Market.UPBIT, "업비트", "Upbit"),
    COINNEST(Market.COINNEST, "코인네스트", "Coinnest"),
    KORBIT(Market.KORBIT, "코빗", "Korbit"),
    GOPAX(Market.GOPAX, "고팍스", "Gopax"),
    CASHIEREST(Market.CASHIEREST, "캐셔레스트", "Cashierest"),

    BITFINEX(Market.BITFINEX, "비트파이넥스", "Bitfinex"),
    BITTREX(Market.BITTREX, "비트렉스", "Bitrrext"),
    POLONIEX(Market.POLONIEX, "폴로닉스", "Poloniex"),
    BINANCE(Market.BINANCE, "바이낸스", "Binance"),
    HUOBI(Market.HUOBI, "후오비", "Huobi"),
    HADAX(Market.HADAX, "후닥스", "Hadax"),
    OKEX(Market.OKEX, "오케이엑스", "Okex"),

    OUT(null, "나가기", "OUT");

    private final Market value;
    private final String kr;
    private final String en;

    @Override
    public String kr() {
        return kr;
    }

    @Override
    public String en() {
        return en;
    }

    public static MarketCommand from(Language language, String str) {
        return CommandEnum.from(values(), language, str, MarketCommand.OUT);
    }

    public static MarketCommand from(Market marketId) {
        MarketCommand rs = null;

        for (MarketCommand b : values()) {
            if (b.getValue() == marketId) {
                return b;
            }
        }

        return rs;
    }


}
