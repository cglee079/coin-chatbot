package com.podo.coinchatbot.telegram.menu.main;

import com.podo.coinchatbot.core.Coin;
import com.podo.coinchatbot.core.Lang;
import com.podo.coinchatbot.core.Market;
import com.podo.coinchatbot.epclient.model.CoinResponse;
import com.podo.coinchatbot.telegram.Keyboard;
import com.podo.coinchatbot.telegram.coin.CoinEndpointer;
import com.podo.coinchatbot.telegram.coin.CoinMeta;
import com.podo.coinchatbot.telegram.coin.ExchangeHolder;
import com.podo.coinchatbot.telegram.command.MainCommand;
import com.podo.coinchatbot.telegram.menu.AbstractMenuHandler;
import com.podo.coinchatbot.telegram.message.CommonMessage;
import com.podo.coinchatbot.telegram.model.Menu;
import com.podo.coinchatbot.telegram.model.MessageVo;
import com.podo.coinchatbot.telegram.model.SendMessageVo;
import com.podo.coinchatbot.telegram.model.UserDto;
import com.podo.coinchatbot.telegram.util.TimeStamper;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;

@RequiredArgsConstructor
public class MainMenuHandler extends AbstractMenuHandler {

    private final CoinEndpointer coinEndpointer;
    private final ExchangeHolder exchangeHolder;

    @Override
    public Menu getHandleMenu() {
        return Menu.MAIN;
    }

    @Override
    public void handle(MessageVo messageVo, Coin coin, CoinMeta coinMeta, UserDto user, String messageText) {
        Long userId = user.getId();
        Lang lang = user.getLang();

        switch (MainCommand.from(lang, messageText)) {
            case CURRENT_PRICE:
                sender().send(SendMessageVo.create(messageVo, createMessageCurrentPrice(user, coin, coinMeta), Keyboard.mainKeyboard(lang));
                break;

            case MARKETS_PRICE:
                sender().send(SendMessageVo.create(messageVo, createMessageEachMarketPrice(user, coin, coinMeta), Keyboard.mainKeyboard(lang));
                break;

            case COMPARED_TO_BTC:
                sender().send(SendMessageVo.create(messageVo, createMessageComparedToBTC(user, coin, coinMeta), Keyboard.mainKeyboard(lang));
                break;

            case CALCULATE: //손익금계산
                sendMessage(userId, messageId, messageCalc(userId), km.getMainKeyboard(lang));
                break;

            case SHOW_SETTING: //설정정보
                sendMessage(userId, messageId, messageInfo(userId), km.getMainKeyboard(lang));
                sendMessage(userId, messageId, msgMaker.showTargetList(lang, marketId, clientTargetService.list(myCoinId, userId)), km.getMainKeyboard(lang));
                break;

            case COIN_LIST: //타 코인 알리미
                sendMessage(userId, messageId, msgMaker.explainCoinList(coinInfoService.list(myCoinId), lang), km.getMainKeyboard(lang));
                break;

            case HELP: //도움말
                sendMessage(userId, messageId, msgMaker.explainHelp(enabledMarketIds, lang), km.getMainKeyboard(lang));
                sendMessage(userId, null, msgMaker.explainSetForeginer(lang), km.getMainKeyboard(lang));
                break;

            case SPONSORING: //후원하기
                sendMessage(userId, messageId, msgMaker.explainSupport(lang), km.getMainKeyboard(lang));
                break;

            case SET_DAYLOOP: // 일일 알림주기 설정
                sendMessage(userId, messageId, msgMaker.explainSetDayloop(lang), km.getSetDayloopAlertKeyboard(lang));
                stateId = MenuState.SET_DAYLOOP;
                break;

            case SET_TIMELOOP: // 시간 알림 주기 설정
                sendMessage(userId, messageId, msgMaker.explainSetTimeloop(lang), km.getSetTimeloopAlertKeyboard(lang));
                stateId = MenuState.SET_TIMELOOP;
                break;

            case SET_MARKET: // 거래소 설정
                sendMessage(userId, messageId, msgMaker.explainMarketSet(lang), km.getSetMarketKeyboard(lang));
                stateId = MenuState.SET_MARKET;
                break;

            case SET_TARGET: // 목표가 설정
                sendMessage(userId, messageId, msgMaker.showTargetList(lang, marketId, clientTargetService.list(myCoinId, userId)), km.getSetTargetKeyboard(lang));
                stateId = MenuState.SET_TARGET;
                break;

            case SET_INVEST: //투자금액 설정
                sendMessage(userId, messageId, msgMaker.explainSetPrice(lang, marketId), km.getDefaultKeyboard());
                stateId = MenuState.SET_INVEST;
                break;

            case SET_COINCNT: // 코인개수 설정
                sendMessage(userId, messageId, msgMaker.explainSetCoinCount(lang), km.getDefaultKeyboard());
                stateId = MenuState.SET_COINCNT;
                break;

            case SEND_MESSAGE: // 문의 건의
                sendMessage(userId, messageId, msgMaker.explainSendSuggest(lang), km.getDefaultKeyboard());
                stateId = MenuState.SEND_MSG;
                break;

            case STOP_ALERTS: // 모든알림 중지
                sendMessage(userId, messageId, msgMaker.explainStop(lang), km.getConfirmStopKeyboard(lang));
                stateId = MenuState.CONFIRM_STOP;
                break;

            case HAPPY_LINE: // 행복회로
                stateId = checkHappyLine(userId, messageId, marketId, lang);
                break;

            case PREFERENCE:  // 설정
                sendMessage(userId, messageId, "Set Preference", km.getPreferenceKeyboard(lang));
                stateId = MenuState.PREFERENCE;
                break;

            default:
                sendMessage(userId, messageId, createMessageCurrentPrice(userId), km.getMainKeyboard(lang));
                break;

        }

        clientService.updateStateId(myCoinId, userId.toString(), stateId);
    }

    private String createMessageCurrentPrice(UserDto user, Coin coin, CoinMeta coinMeta) {
        Lang lang = user.getLang();
        Market market = user.getMarket();

        CoinResponse coinResponse = coinEndpointer.getCoin(coin, market);

        if (!coinResponse.isSuccess()) {
            return CommonMessage.warningWaitSecond(lang);
        }

        Double currentPrice = coinResponse.getCurrentPrice();

        if (coinMeta.isBTCMarket(market)) {
            Double currentBTC = currentPrice;
            Double currentMoney = coinEndpointer.btcToMoney(currentPrice, market);
            return CurrentPriceMessage.getInBtc(user, currentMoney, currentBTC, coinMeta.getNumberFormatter());
        }

        return CurrentPriceMessage.getInMoney(user, currentPrice, coinMeta.getNumberFormatter());
    }

    public String createMessageEachMarketPrice(UserDto user, Coin coin, CoinMeta coinMeta) {
        Map<Market, Double> marketToCurrentPrice = new LinkedHashMap<>();

        for (Market market : coinMeta.getEnableMarkets()) {
            Double currentPrice = marketToCurrentPrice.put(market, coinEndpointer.getCurrentPrice(coin, market));
            if (coinMeta.isBTCMarket(market)) {
                marketToCurrentPrice.put(market, coinEndpointer.btcToMoney(currentPrice, market));
            } else {
                marketToCurrentPrice.put(market, currentPrice);
            }
        }

        double exchangeRate = exchangeHolder.getCurrentExchangeRate();
        return EachMarketPriceMessage.get(user, marketToCurrentPrice, exchangeRate, coinMeta.getNumberFormatter());
    }

    /* 비트코인 대비 변화량 */
    public String createMessageComparedToBTC(UserDto user, Coin coin, CoinMeta coinMeta) {
        StringBuilder message = new StringBuilder();
        Market userMarket = user.getMarket();
        Market targetMarket = userMarket;
        Lang lang = user.getLang();
        String now = TimeStamper.getDateTime(user.getLocaltime());

        message.append(ComparedToBTCMessage.currentTime(now, lang));

        // 비트대비 변화량을 제공하지 않는 거래소에 대해서 처리
        if (userMarket.isKRW() && (userMarket == Market.COINNEST || userMarket == Market.KORBIT)) {
            message.append(ComparedToBTCMessage.notSupportMarket(userMarket, lang));

            for (Market enableMarket : coinMeta.getEnableMarkets()) {
                if (enableMarket.isKRW() && enableMarket != Market.COINNEST && enableMarket != Market.KORBIT) {
                    message.append(ComparedToBTCMessage.notSupportMarket(enableMarket, lang));
                    targetMarket = enableMarket;
                    break;
                }
            }

            message.append("\n");
        }

        // 비트대비 변화량을 제공하지 않는 거래소에 대해서 처리
        if (userMarket.isUSD() && userMarket == Market.BITTREX) {
            message.append(ComparedToBTCMessage.notSupportMarket(userMarket, lang));

            for (Market enableMarket : coinMeta.getEnableMarkets()) {
                if (enableMarket.isUSD() && enableMarket != Market.BITTREX) {
                    message.append(ComparedToBTCMessage.notSupportMarket(enableMarket, lang));
                    targetMarket = enableMarket;
                    break;
                }
            }
            message.append("\n");
        }

        CoinResponse coinResponse = coinEndpointer.getCoin(coin, userMarket);
        CoinResponse bitcoinResponse = coinEndpointer.getCoin(Coin.BTC, userMarket);

        if (!coinResponse.isSuccess() || !bitcoinResponse.isSuccess()) {
            return CommonMessage.warningWaitSecond(lang);
        }

        Double coinCurrentPrice = coinResponse.getCurrentPrice();
        Double coinOpenPrice = coinResponse.getOpenPrice();
        Double bitcoinCurrentPrice = bitcoinResponse.getCurrentPrice();
        Double bitcoinOpenPrice = bitcoinResponse.getOpenPrice();

        if (coinMeta.isBTCMarket(userMarket)) {
            message.append(
                    ComparedToBTCMessage.result(
                            lang, coin, targetMarket,
                            bitcoinOpenPrice, bitcoinCurrentPrice,
                            coinEndpointer.btcToMoney(coinOpenPrice, userMarket),
                            coinEndpointer.btcToMoney(coinCurrentPrice, userMarket),
                            coinMeta.getNumberFormatter())
            );
        } else{
            message.append(
                    ComparedToBTCMessage.result(
                            lang, coin, targetMarket,
                            bitcoinOpenPrice, bitcoinCurrentPrice,coinOpenPrice, coinCurrentPrice,
                            coinMeta.getNumberFormatter())
            );
        }

        return message.toString();

    }

    /* 손익금 계산 */
    public String messageCalc(Integer userId) {
        ClientVo client = clientService.get(myCoinId, userId);
        Lang lang = client.getLang();

        if (client.getInvest() == null) {
            return msgMaker.msgPleaseSetInvestmentAmount(lang);
        } else if (client.getCoinCnt() == null) {
            return msgMaker.msgPleaseSetTheNumberOfCoins(lang);
        } else {
            try {
                JSONObject coin = coinEndpointer.getCoin(myCoinId, client.getMarketId());
                return calcResult(client, coin.getDouble("last"));
            } catch (ServerErrorException e) {
                e.printStackTrace();
                return msgMaker.warningWaitSecond(lang) + e.getTelegramMsg();
            }
        }
    }

    public String calcResult(ClientVo client, Double coinValue) {
        double price = client.getInvest();
        double cnt = client.getCoinCnt();
        Lang lang = client.getLang();
        Market marketId = client.getMarketId();

        double avgPrice = (double) ((double) price / cnt);

        JSONObject btcObj = null;
        if (inBtcs.get(marketId)) {
            try {
                btcObj = coinEndpointer.getCoin(Coin.BTC, client.getMarketId());
            } catch (ServerErrorException e) {
                e.printStackTrace();
                return msgMaker.warningWaitSecond(lang) + e.getTelegramMsg();
            }
        }

        return msgMaker.msgCalcResult(price, cnt, avgPrice, coinValue, btcObj, client);
    }


    /* 설정 정보 확인 */
    public String messageInfo(Integer userId) {
        ClientVo client = clientService.get(myCoinId, userId);
        Lang lang = client.getLang();

        return msgMaker.msgClientSetting(client, lang);
    }
}
