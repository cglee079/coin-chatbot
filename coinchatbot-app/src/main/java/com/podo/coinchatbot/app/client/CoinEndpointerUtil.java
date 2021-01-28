package com.podo.coinchatbot.app.client;

import com.podo.coinchatbot.core.Coin;
import com.podo.coinchatbot.core.Market;
import com.podo.coinchatbot.app.client.model.CoinResponse;
import com.podo.coinchatbot.app.client.exception.CoinApiCallException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class CoinEndpointerUtil {

    public static CoinResponse getCoin(CoinEndpointer coinEndpointer, Coin coin, Market market){
        CoinResponse coinResponse = coinEndpointer.getCoin(coin, market);

        if (!coinResponse.isSuccess()) {
            throw new CoinApiCallException(coin, coinResponse.getErrorMessage());
        }

        return coinResponse;
    }

    public static BigDecimal btcToMoney(CoinEndpointer coinEndpointer, Market market, BigDecimal btc){
        CoinResponse coinResponse = coinEndpointer.getCoin(Coin.BTC, market);

        if(!coinResponse.isSuccess()){
            throw new CoinApiCallException(Coin.BTC, coinResponse.getErrorMessage());
        }

        return btc.multiply(coinResponse.getCurrentPrice());
    }

}
