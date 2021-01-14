package com.podo.coinchatbot.telegram.coin;

import com.podo.coinchatbot.core.Coin;
import com.podo.coinchatbot.core.Market;
import com.podo.coinchatbot.epclient.model.CoinResponse;
import org.json.JSONObject;

public interface CoinEndpointer {
    CoinResponse getCoin(Coin coin, Market market);

    Double btcToMoney(Double price, Market market);

    Double getCurrentPrice(Coin coin, Market market);
}
