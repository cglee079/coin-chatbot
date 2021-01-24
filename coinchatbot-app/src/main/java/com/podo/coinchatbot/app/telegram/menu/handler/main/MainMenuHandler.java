package com.podo.coinchatbot.app.telegram.menu.handler.main;

import com.podo.coinchatbot.core.Coin;
import com.podo.coinchatbot.core.Language;
import com.podo.coinchatbot.core.Market;
import com.podo.coinchatbot.app.client.CoinEndpointerUtil;
import com.podo.coinchatbot.app.client.model.CoinResponse;
import com.podo.coinchatbot.app.telegram.keyboard.Keyboard;
import com.podo.coinchatbot.app.telegram.TelegramMessageSender;
import com.podo.coinchatbot.app.client.CoinEndpointer;
import com.podo.coinchatbot.app.model.CoinMeta;
import com.podo.coinchatbot.app.client.ExchangeHolder;
import com.podo.coinchatbot.app.telegram.command.MainCommand;
import com.podo.coinchatbot.app.domain.service.CoinInformationService;
import com.podo.coinchatbot.app.domain.service.UserTargetAlarmService;
import com.podo.coinchatbot.app.domain.service.UserService;
import com.podo.coinchatbot.app.telegram.menu.AbstractMenuHandler;
import com.podo.coinchatbot.app.telegram.menu.handler.main.message.CalculateMessage;
import com.podo.coinchatbot.app.telegram.menu.handler.main.message.ComparedToBTCMessage;
import com.podo.coinchatbot.app.telegram.menu.handler.main.message.CurrentPriceMessage;
import com.podo.coinchatbot.app.telegram.menu.handler.main.message.EachMarketPriceMessage;
import com.podo.coinchatbot.app.telegram.menu.handler.main.message.MainCommonMessage;
import com.podo.coinchatbot.app.telegram.message.HelpMessage;
import com.podo.coinchatbot.app.telegram.message.UserSettingMessage;
import com.podo.coinchatbot.app.model.Menu;
import com.podo.coinchatbot.app.telegram.model.MessageVo;
import com.podo.coinchatbot.app.telegram.model.SendMessageVo;
import com.podo.coinchatbot.app.domain.dto.UserDto;
import com.podo.coinchatbot.app.util.DateTimeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class MainMenuHandler extends AbstractMenuHandler {

    private final CoinEndpointer coinEndpointer;
    private final ExchangeHolder exchangeHolder;
    private final UserService userService;
    private final UserTargetAlarmService userTargetAlarmService;
    private final CoinInformationService coinInformationService;

    @Override
    public Menu getHandleMenu() {
        return Menu.MAIN;
    }

    @Override
    public void handle(MessageVo messageVo, Coin coin, CoinMeta coinMeta, UserDto user, String messageText, TelegramMessageSender sender) {
        Menu menuStatus = Menu.MAIN;
        Market market = user.getMarket();
        Long userId = user.getId();
        Language language = user.getLanguage();
        LocalDateTime now = LocalDateTime.now();

        switch (MainCommand.from(language, messageText)) {
            case CURRENT_PRICE:
                sender.sendMessage(SendMessageVo.create(messageVo, createMessageCurrentPrice(user, coin, coinMeta, now), Keyboard.mainKeyboard(language)));
                break;

            case MARKETS_PRICE:
                sender.sendMessage(SendMessageVo.create(messageVo, createMessageEachMarketPrice(user, coin, coinMeta, now), Keyboard.mainKeyboard(language)));
                break;

            case COMPARED_TO_BTC:
                sender.sendMessage(SendMessageVo.create(messageVo, createMessageComparedToBTC(user, coin, coinMeta, now), Keyboard.mainKeyboard(language)));
                break;

            case CALCULATE: //손익금계산
                sender.sendMessage(SendMessageVo.create(messageVo, createMessageCalculate(user, coin, coinMeta), Keyboard.mainKeyboard(language)));
                break;

            case SHOW_SETTING: //설정정보
                sender.sendMessage(SendMessageVo.create(messageVo, UserSettingMessage.get(user, coinMeta.getCoinFormatter()), Keyboard.mainKeyboard(language)));
                sender.sendMessage(SendMessageVo.create(messageVo, MainCommonMessage.targetAlarms(language, market, userTargetAlarmService.findByUserId(userId), coinMeta.getCoinFormatter()), Keyboard.mainKeyboard(language)));
                break;

            case COIN_LIST: //타 코인 알리미
                sender.sendMessage(SendMessageVo.create(messageVo, MainCommonMessage.explainCoinList(language, coinInformationService.getAll()), Keyboard.mainKeyboard(language)));
                break;

            case HELP: //도움말
                sender.sendMessage(SendMessageVo.create(messageVo, HelpMessage.help(language, coin, coinMeta.getEnableMarkets()), Keyboard.mainKeyboard(language)));
                sender.sendMessage(SendMessageVo.create(messageVo, HelpMessage.explainForForeigner(), Keyboard.mainKeyboard(language)));
                break;

            case SPONSORING: //후원하기
                sender.sendMessage(SendMessageVo.create(messageVo, MainCommonMessage.explainSupport(language), Keyboard.mainKeyboard(language)));
                break;

            case DAYLOOP_ALARM_CONFIG: // 일일 알림주기 설정
                sender.sendMessage(SendMessageVo.create(messageVo, MainCommonMessage.explainDayloopConfig(language), Keyboard.setDayloopAlertKeyboard(language)));
                menuStatus = Menu.DAYLOOP_ALARM_CONFIG;
                break;

            case TIMELOOP_ALARM_CONFIG: // 시간 알림 주기 설정
                sender.sendMessage(SendMessageVo.create(messageVo, MainCommonMessage.explainTimeloopConfig(language), Keyboard.setTimeloopAlertKeyboard(language)));
                menuStatus = Menu.TIMELOOP_ALARM_CONFIG;
                break;

            case MARKET_CONFIG: // 거래소 설정
                sender.sendMessage(SendMessageVo.create(messageVo, MainCommonMessage.explainMarketConfig(language), Keyboard.setMarketKeyboard(language, coinMeta.getEnableMarkets())));
                menuStatus = Menu.MARKET_CONFIG;
                break;

            case TARGET_ALARM_CONFIG: // 목표가 설정
                sender.sendMessage(SendMessageVo.create(messageVo, MainCommonMessage.targetAlarms(language, market, userTargetAlarmService.findByUserId(userId), coinMeta.getCoinFormatter()), Keyboard.targetAlarmConfigKeyboard(language)));
                menuStatus = Menu.TARGET_ALARM_CONFIG;
                break;

            case INVESET_CONFIG: //투자금액 설정
                sender.sendMessage(SendMessageVo.create(messageVo, MainCommonMessage.explainInvestConfig(language, market, coinMeta), Keyboard.defaultKeyboard()));
                menuStatus = Menu.INVEST_CONFIG;
                break;

            case COINCOUNT_CONFIG: // 코인개수 설정
                sender.sendMessage(SendMessageVo.create(messageVo, MainCommonMessage.explainCoinCountConfig(language, coinMeta), Keyboard.defaultKeyboard()));
                menuStatus = Menu.COIN_COUNT_CONFIG;
                break;

            case SEND_MESSAGE: // 문의 건의
                sender.sendMessage(SendMessageVo.create(messageVo, MainCommonMessage.explainSendSuggest(language), Keyboard.defaultKeyboard()));
                menuStatus = Menu.SUGGEST_MESSAGE;
                break;

            case STOP_ALL_ALARAM: // 모든알림 중지
                sender.sendMessage(SendMessageVo.create(messageVo, MainCommonMessage.explainStopAllAlert(language), Keyboard.confirmStopKeyboard(language)));
                menuStatus = Menu.STOP_ALL_ALARM;
                break;

            case HAPPY_LINE: // 행복회로
                if (user.getInvest() == null) {
                    sender.sendMessage(SendMessageVo.create(messageVo, MainCommonMessage.pleaseConfigInvest(language), Keyboard.mainKeyboard(language)));
                    break;
                }

                if (user.getCoinCount() == null) {
                    sender.sendMessage(SendMessageVo.create(messageVo, MainCommonMessage.pleaseConfigCoinCount(language), Keyboard.mainKeyboard(language)));
                    break;
                }

                sender.sendMessage(SendMessageVo.create(messageVo, MainCommonMessage.explainHappyLine(language, market, coinMeta), Keyboard.defaultKeyboard()));
                menuStatus = Menu.HAPPY_COIN_PRICE;
                break;

            case PREFERENCE:  // 설정
                sender.sendMessage(SendMessageVo.create(messageVo, "Set Preference", Keyboard.preferenceKeyboard(language)));
                menuStatus = Menu.PREFERENCE;
                break;

            default:
                sender.sendMessage(SendMessageVo.create(messageVo, createMessageCurrentPrice(user, coin, coinMeta, now), Keyboard.mainKeyboard(language)));
                break;

        }

        userService.updateMenuStatus(userId, menuStatus);
    }

    private String createMessageCurrentPrice(UserDto user, Coin coin, CoinMeta coinMeta, LocalDateTime now) {
        Market market = user.getMarket();

        CoinResponse coinResponse = CoinEndpointerUtil.getCoin(coinEndpointer, coin, market);

        BigDecimal currentPrice = coinResponse.getCurrentPrice();

        if (coinMeta.isBTCMarket(market)) {
            BigDecimal currentBTC = currentPrice;
            BigDecimal currentMoney = CoinEndpointerUtil.btcToMoney(coinEndpointer, market, currentPrice);
            return CurrentPriceMessage.getInBtc(user, currentMoney, currentBTC, coinMeta.getCoinFormatter(), now);
        }

        return CurrentPriceMessage.getInMoney(user, currentPrice, coinMeta.getCoinFormatter(), now);
    }

    public String createMessageEachMarketPrice(UserDto user, Coin coin, CoinMeta coinMeta, LocalDateTime now) {
        Map<Market, BigDecimal> marketToCurrentPrice = new LinkedHashMap<>();

        for (Market market : coinMeta.getEnableMarkets()) {
            CoinResponse coinResponse = coinEndpointer.getCoin(coin, market);

            if (!coinResponse.isSuccess()) {
                marketToCurrentPrice.put(market, null);
                continue;
            }

            if (coinMeta.isBTCMarket(market)) {
                marketToCurrentPrice.put(market, CoinEndpointerUtil.btcToMoney(coinEndpointer, market, coinResponse.getCurrentPrice()));
            } else {
                marketToCurrentPrice.put(market, coinResponse.getCurrentPrice());
            }
        }

        BigDecimal exchangeRate = exchangeHolder.getCurrentExchangeRate();
        return EachMarketPriceMessage.get(user, marketToCurrentPrice, exchangeRate, coinMeta.getCoinFormatter(), now);
    }

    public String createMessageComparedToBTC(UserDto user, Coin coin, CoinMeta coinMeta, LocalDateTime now) {
        StringBuilder message = new StringBuilder();
        Market userMarket = user.getMarket();
        Market targetMarket = userMarket;
        Language language = user.getLanguage();
        String userLocaleNow = DateTimeUtil.toDateTimeString(now, user.getTimeDifference());

        message.append(ComparedToBTCMessage.currentTime(userLocaleNow, language));

        // 비트대비 변화량을 제공하지 않는 거래소에 대해서 처리
        if (userMarket.isKRW() && (userMarket == Market.COINNEST || userMarket == Market.KORBIT)) {
            message.append(ComparedToBTCMessage.notSupportMarket(userMarket, language));

            for (Market enableMarket : coinMeta.getEnableMarkets()) {
                if (enableMarket.isKRW() && enableMarket != Market.COINNEST && enableMarket != Market.KORBIT) {
                    message.append(ComparedToBTCMessage.replaceAnotherMarket(enableMarket, language));
                    targetMarket = enableMarket;
                    break;
                }
            }

            message.append("\n");
        }

        // 비트대비 변화량을 제공하지 않는 거래소에 대해서 처리
        else if (userMarket.isUSD() && userMarket == Market.BITTREX) {
            message.append(ComparedToBTCMessage.notSupportMarket(userMarket, language));

            for (Market enableMarket : coinMeta.getEnableMarkets()) {
                if (enableMarket.isUSD() && enableMarket != Market.BITTREX) {
                    message.append(ComparedToBTCMessage.replaceAnotherMarket(enableMarket, language));
                    targetMarket = enableMarket;
                    break;
                }
            }
            message.append("\n");
        }

        CoinResponse coinResponse = CoinEndpointerUtil.getCoin(coinEndpointer, coin, targetMarket);
        CoinResponse bitcoinResponse = CoinEndpointerUtil.getCoin(coinEndpointer, Coin.BTC, targetMarket);

        BigDecimal coinCurrentPrice = coinResponse.getCurrentPrice();
        BigDecimal coinOpenPrice = coinResponse.getOpenPrice();
        BigDecimal bitcoinCurrentPrice = bitcoinResponse.getCurrentPrice();
        BigDecimal bitcoinOpenPrice = bitcoinResponse.getOpenPrice();

        if (coinMeta.isBTCMarket(userMarket)) {
            message.append(
                    ComparedToBTCMessage.result(
                            language, coin, targetMarket,
                            bitcoinOpenPrice, bitcoinCurrentPrice,
                            CoinEndpointerUtil.btcToMoney(coinEndpointer, userMarket, coinOpenPrice),
                            CoinEndpointerUtil.btcToMoney(coinEndpointer, userMarket, coinCurrentPrice),
                            coinMeta.getCoinFormatter())
            );
        } else {
            message.append(
                    ComparedToBTCMessage.result(
                            language, coin, targetMarket,
                            bitcoinOpenPrice, bitcoinCurrentPrice, coinOpenPrice, coinCurrentPrice,
                            coinMeta.getCoinFormatter())
            );
        }

        return message.toString();

    }

    public String createMessageCalculate(UserDto user, Coin coin, CoinMeta coinMeta) {
        Language language = user.getLanguage();
        Market market = user.getMarket();
        BigDecimal invest = user.getInvest();
        BigDecimal coinCount = user.getCoinCount();

        if (Objects.isNull(invest)) {
            return MainCommonMessage.pleaseConfigInvest(language);
        }

        if (Objects.isNull(coinCount)) {
            return MainCommonMessage.pleaseConfigCoinCount(language);
        }

        CoinResponse coinResponse = CoinEndpointerUtil.getCoin(coinEndpointer, coin, market);
        BigDecimal currentPrice = coinResponse.getCurrentPrice();
        BigDecimal averageCoinPrice = invest.divide(coinCount, 10, RoundingMode.HALF_UP);

        if (coinMeta.isBTCMarket(market)) {
            CoinResponse bitcoinResponse = CoinEndpointerUtil.getCoin(coinEndpointer, Coin.BTC, market);

            return CalculateMessage.calcResultInBTC(invest, coinCount, averageCoinPrice, currentPrice, bitcoinResponse.getCurrentPrice(), user, coinMeta.getCoinFormatter());
        }

        return CalculateMessage.calcResult(invest, coinCount, averageCoinPrice, currentPrice, user, coinMeta.getCoinFormatter());
    }


}
