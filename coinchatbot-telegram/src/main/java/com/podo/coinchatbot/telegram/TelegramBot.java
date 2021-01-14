package com.podo.coinchatbot.telegram;

import com.cglee079.coinchatbot.coin.CoinManager;
import com.cglee079.coinchatbot.config.cmd.*;
import com.cglee079.coinchatbot.config.id.*;
import com.cglee079.coinchatbot.exception.ServerErrorException;
import com.cglee079.coinchatbot.model.*;
import com.cglee079.coinchatbot.service.*;
import com.cglee079.coinchatbot.telegram.keyboard.KeyboardManager;
import com.cglee079.coinchatbot.telegram.message.MessageMaker;
import com.cglee079.coinchatbot.util.NumberFormmater;
import com.cglee079.coinchatbot.util.TimeStamper;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.meta.updateshandlers.SentCallback;

import javax.annotation.PostConstruct;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

@Slf4j
public class TelegramBot extends AbilityBot {
    private Coin myCoinId = null;

    @Autowired
    private ClientService clientService;

    @Autowired
    private ClientMessageService clientMsgService;

    @Autowired
    private ClientSuggestService clientSuggestService;

    @Autowired
    private ClientTargetService clientTargetService;

    @Autowired
    private CoinInfoService coinInfoService;

    @Autowired
    private CoinConfigService coinConfigService;

    @Autowired
    private CoinMarketConfigService coinMarketConfigService;

    @Autowired
    private CoinManager coinManager;

    private Keyboard km;
    private NumberFormmater nf;
    private MessageMaker msgMaker;
    private HashMap<Market, Boolean> inBtcs;
    private List<Market> enabledMarketIds;

    protected TelegramBot(Coin myCoinId, String botToken, String botUsername) {
        super(botToken, botUsername);
        this.myCoinId = myCoinId;
    }

    @PostConstruct
    public void init() {
        inBtcs = new HashMap<>();
        enabledMarketIds = new ArrayList<Market>();
        List<CoinMarketConfigVo> configMarkets = coinMarketConfigService.list(myCoinId);
        CoinMarketConfigVo configMarket;

        for (int i = 0; i < configMarkets.size(); i++) {
            configMarket = configMarkets.get(i);
            inBtcs.put(configMarket.getMarketId(), configMarket.isInBtc());
            enabledMarketIds.add(configMarket.getMarketId());
        }


        CoinConfigVo config = coinConfigService.get(myCoinId);
        int digitKRW = config.getDigitKRW();
        int digitUSD = config.getDigitUSD();
        int digitBTC = config.getDigitBTC();
        nf = new NumberFormmater(digitKRW, digitUSD, digitBTC);

        km = new Keyboard(enabledMarketIds);
        msgMaker = new MessageMaker(myCoinId, config, nf, inBtcs);
    }

    @Override
    public int creatorId() {
        return 503609560;
    }

    /**
     * 사용자로부터 받은 메세지 처리
     */
    @Override
    public void onUpdateReceived(Update update) {
        clientMsgService.insert(myCoinId, update);

        Message message = null;
        if (update.getMessage() != null) {
            message = update.getMessage();
        } else if (update.getEditedMessage() != null) {
            message = update.getEditedMessage();
        }

        User user = message.getFrom();
        String username = user.getLastName() + " " + user.getFirstName();
        Integer userId = user.getId();
        Integer messageId = message.getMessageId();
        String cmd = message.getText();

        ClientVo client = clientService.get(myCoinId, userId);
        clientService.updateMsgDate(client);

        //Client가 새로 시작한 경우
        if (message.getText().equals("/start") || client == null) {
            Lang lang = Lang.KR;
            StringBuilder msg = new StringBuilder("");

            //새로운 사용자
            if (clientService.openChat(myCoinId, userId, username, enabledMarketIds.get(0))) {
                msg.append(msgMaker.msgStartService(lang));
                msg.append(msgMaker.explainHelp(enabledMarketIds, lang));
                sendMessage(userId, null, msg, km.getMainKeyboard(lang));
                sendMessage(userId, null, msgMaker.explainSetForeginer(lang), km.getMainKeyboard(lang));
            }

            // 이미 이전에 챗봇을 시작한 사용.
            else {
                msg.append(msgMaker.msgAlreadyStartService(lang));
                sendMessage(userId, null, msg, km.getMainKeyboard(lang));
                sendMessage(userId, messageId, messageInfo(userId), km.getMainKeyboard(lang));
            }

            return;
        }

        //사용자 메뉴 상태에 따른 로직
        Lang lang = client.getLang();
        MenuState stateId = client.getStateId();
        Market marketId = client.getMarketId();
        if (client.isEnabled()) {
            switch (stateId) {
                case MAIN:
                    handleMenu(userId, messageId, cmd, marketId, lang);
                    break;
                case SET_DAYLOOP:
                    handleSetDayloop(userId, messageId, cmd, lang);
                    break;
                case SET_TIMELOOP:
                    handleSetTimeloop(userId, messageId, cmd, lang);
                    break;
                case SET_MARKET:
                    handleSetMarket(userId, messageId, cmd, lang);
                    break;
                case SET_TARGET:
                    handleSetTarget(userId, messageId, cmd, marketId, lang);
                    break;
                case ADD_TARGET:
                    handleAddTarget(userId, messageId, cmd, lang);
                    break;
                case DEL_TARGET:
                    handleDelTarget(userId, messageId, cmd, marketId, lang);
                    break;
                case SET_INVEST:
                    handleSetPrice(userId, messageId, cmd, marketId, lang);
                    break;
                case SET_COINCNT:
                    handleSetNumber(userId, messageId, cmd, lang);
                    break;
                case SEND_MSG:
                    handleSendMsg(userId, username, messageId, cmd, lang);
                    break;
                case CONFIRM_STOP:
                    handleConfirmStop(userId, username, messageId, cmd, lang);
                    break;
                case HAPPY_LINE:
                    handleHappyLine(userId, username, messageId, cmd, lang);
                    break;
                case PREFERENCE:
                    handlePreference(userId, username, messageId, cmd, lang);
                    break;
                case PREF_LANGUAGE:
                    handleSetLanguage(userId, username, messageId, cmd, lang);
                    break;
                case PREF_TIMEADJUST:
                    handleTimeAdjust(userId, username, messageId, cmd, lang);
                    break;
            }
        } else {
            clientService.openChat(myCoinId, userId, username, marketId);
            sendMessage(userId, null, msgMaker.msgStartService(lang), null);
            sendMessage(userId, null, msgMaker.msgAlreadyStartService(lang), null);
            sendMessage(userId, messageId, messageInfo(userId), km.getMainKeyboard(lang));
        }

    }

    /* 메인 메뉴 응답 처리 */
    private void handleMenu(Integer userId, Integer messageId, String cmd, Market marketId, Lang lang) {
        MenuState stateId = MenuState.MAIN;

        switch (MainCmd.from(lang, cmd)) {
            case CURRENT_PRICE: //현재가
                sendMessage(userId, messageId, messageCurrentPrice(userId), km.getMainKeyboard(lang));
                break;

            case MARKETS_PRICE: //거래소별 현재가
                sendMessage(userId, messageId, messageEachMarketPrice(userId), km.getMainKeyboard(lang));
                break;

            case CHANGE_RATE: //비트대비 변화량
                sendMessage(userId, messageId, messageBTC(userId), km.getMainKeyboard(lang));
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
                sendMessage(userId, messageId, messageCurrentPrice(userId), km.getMainKeyboard(lang));
                break;

        }

        clientService.updateStateId(myCoinId, userId.toString(), stateId);
    }


    /* 일일 알림 설정 응답 처리 */
    private void handleSetDayloop(Integer userId, Integer messageId, String cmd, Lang lang) {
        StringBuilder msg = new StringBuilder("");

        DayloopCmd inCmd = DayloopCmd.from(lang, cmd);

        int dayloop = -1;
        if (inCmd != null) {
            dayloop = inCmd.getValue();
        }

        if (dayloop == -1) {
            msg.append(msgMaker.msgDayloopNoSet(lang));
        } else {
            if (clientService.updateDayLoop(myCoinId, userId.toString(), dayloop)) {
                if (dayloop == 0) {
                    msg.append(msgMaker.msgDayloopStop(lang));
                } else {
                    msg.append(msgMaker.msgDayloopSet(dayloop, lang));
                }
            } else {
                msg.append(msgMaker.msgDayloopNoSet(lang));
            }
        }

        msg.append(msgMaker.msgToMain(lang));

        sendMessage(userId, messageId, msg, km.getMainKeyboard(lang));
        clientService.updateStateId(myCoinId, userId.toString(), MenuState.MAIN);
    }

    /* 시간 알림 설정 응답 처리 */
    private void handleSetTimeloop(Integer userId, Integer messageId, String cmd, Lang lang) {
        StringBuilder msg = new StringBuilder("");

        TimeloopCmd inCmd = TimeloopCmd.from(lang, cmd);

        int timeloop = -1;
        if (inCmd != null) {
            timeloop = inCmd.getValue();
        }

        if (timeloop == -1) {
            msg.append(msgMaker.msgTimeloopNoSet(lang));
        } else {
            if (clientService.updateTimeLoop(myCoinId, userId.toString(), timeloop)) {
                if (timeloop == 0) {
                    msg.append(msgMaker.msgTimeloopStop(lang));
                } else {
                    msg.append(msgMaker.msgTimeloopSet(timeloop, lang));
                }
            } else {
                msg.append(msgMaker.msgTimeloopNoSet(lang));
            }
        }

        msg.append(msgMaker.msgToMain(lang));

        sendMessage(userId, messageId, msg, km.getMainKeyboard(lang));
        clientService.updateStateId(myCoinId, userId.toString(), MenuState.MAIN);
    }

    /* 마켓 설정 응답 처리 */
    private void handleSetMarket(Integer userId, Integer messageId, String cmd, Lang lang) {
        StringBuilder msg = new StringBuilder("");

        MarketCmd inCmd = MarketCmd.from(lang, cmd);
        Market marketId = null;
        if (inCmd != null) {
            marketId = inCmd.getId();
        }

        if (marketId != null) {
            ClientVo client = clientService.get(myCoinId, userId);
            Market currentMarket = client.getMarketId();
            Double currentPrice = client.getInvest();
            double exchangeRate = coinManager.getExchangeRate();

            Double changePrice = currentPrice;

            // 한화에서 달러 거래소로 변경시 환율에 따른 투자금액, 목표가격 변경
            if (currentMarket.isKRW() && marketId.isUSD()) {
                if (currentPrice != null) {
                    changePrice = currentPrice / exchangeRate;
                }

                List<ClientTargetVo> currentTargets = clientTargetService.list(myCoinId, userId);
                List<Double> currentTargetPrices = new ArrayList<>();
                List<Double> changedTargetPrices = new ArrayList<>();
                ClientTargetVo current;
                for (int i = 0; i < currentTargets.size(); i++) {
                    current = currentTargets.get(i);
                    currentTargetPrices.add(current.getPrice());
                    changedTargetPrices.add(current.getPrice() / exchangeRate);
                    clientTargetService.updatePrice(current, nf.formatPrice(current.getPrice() / exchangeRate, marketId));
                }
                msg.append(msgMaker.msgMarketSetChangeCurrency(client, changePrice, currentTargetPrices, changedTargetPrices, marketId));

            }

            // 달러에서 한화 거래소로 변경시 환율에 따른 투자금액, 목표가격 변경
            if (currentMarket.isUSD() && marketId.isKRW()) {
                if (currentPrice != null) {
                    changePrice = currentPrice * exchangeRate;
                }
                List<ClientTargetVo> currentTargets = clientTargetService.list(myCoinId, userId);
                List<Double> currentTargetPrices = new ArrayList<>();
                List<Double> changedTargetPrices = new ArrayList<>();
                ClientTargetVo current;
                for (int i = 0; i < currentTargets.size(); i++) {
                    current = currentTargets.get(i);
                    currentTargetPrices.add(current.getPrice());
                    changedTargetPrices.add(current.getPrice() * exchangeRate);
                    clientTargetService.updatePrice(current, nf.formatPrice(current.getPrice() * exchangeRate, marketId));
                }

                msg.append(msgMaker.msgMarketSetChangeCurrency(client, changePrice, currentTargetPrices, changedTargetPrices, marketId));
            }

            client.setInvest(changePrice);
            client.setMarketId(marketId);
            clientService.update(client);

        } else {
            msg.append(msgMaker.msgMarketNoSet(lang));
        }
        msg.append(msgMaker.msgToMain(lang));

        sendMessage(userId, messageId, msg, km.getMainKeyboard(lang));
        clientService.updateStateId(myCoinId, userId.toString(), MenuState.MAIN);
    }


    private void handleSetTarget(Integer userId, Integer messageId, String cmd, Market marketId, Lang lang) {
        TargetSetCmd inCmd = TargetSetCmd.from(lang, cmd);

        MenuState state = MenuState.MAIN;
        switch (inCmd) {
            case ADD:
                sendMessage(userId, messageId, msgMaker.explainTargetAdd(lang, marketId), km.getDefaultKeyboard());
                state = MenuState.ADD_TARGET;
                break;
            case DEL:

                List<ClientTargetVo> targets = clientTargetService.list(myCoinId, userId);
                List<String> targetCmds = new ArrayList<String>();
                ClientTargetVo target;
                for (int i = 0; i < targets.size(); i++) {
                    target = targets.get(i);
                    targetCmds.add(nf.toMoneyStr(target.getPrice(), marketId) + " " + target.getFocus().getStr(lang));
                }

                sendMessage(userId, messageId, msgMaker.explainTargetDel(lang, marketId), km.getDeleteTargetKeyboard(targetCmds, lang));
                state = MenuState.DEL_TARGET;
                break;
            case OUT:
            default:
                sendMessage(userId, messageId, msgMaker.msgToMain(lang), km.getMainKeyboard(lang));
                break;
        }

        clientService.updateStateId(myCoinId, userId.toString(), state);
    }

    /* 목표가 설정 응답 처리 */
    private void handleAddTarget(Integer userId, Integer messageId, String cmd, Lang lang) {
        ClientVo client = clientService.get(myCoinId, userId);
        Market marketId = client.getMarketId();
        StringBuilder msg = new StringBuilder("");
        boolean valid = false;

        // 코인 가격 정보를 가져옴.
        double currentValue = -1;
        try {
            JSONObject coinObj = coinManager.getCoin(myCoinId, marketId);
            currentValue = coinObj.getDouble("last");

            if (inBtcs.get(marketId)) {
                currentValue = coinManager.getMoney(coinObj, marketId).getDouble("last");
            }

        } catch (ServerErrorException e) {
            e.printStackTrace();
            msg.append(msgMaker.warningWaitSecond(lang) + e.getTelegramMsg());
        }

        if (currentValue != -1) {
            String priceStr = cmd.trim();
            double targetPrice = -1;

            //사용자의 입력이 유효한 패턴인지 검증.
            if (priceStr.matches("^\\d*(\\.?\\d*)$")) {
                targetPrice = Double.valueOf(priceStr);
                valid = true;

            } else if (priceStr.matches("^[+-]?\\d*(\\.?\\d*)%$")) {
                if (priceStr.equals("%")) {
                    priceStr = "0%";
                }

                priceStr = priceStr.replace("%", "");
                double percent = (Double.valueOf(priceStr) / 100);

                if (percent == 0) {
                    valid = true;
                    targetPrice = currentValue;
                } else if (percent > 0) {
                    valid = true;
                    targetPrice = currentValue + currentValue * percent;
                } else if (percent < 0 && percent >= -100) {
                    valid = true;
                    double a = (currentValue * percent) * -1;
                    targetPrice = currentValue - a;
                } else if (percent < -100) {
                    msg.append(msgMaker.warningTargetPriceAddPercent(lang));
                }

            } else {
                msg.append(msgMaker.warningTargetPriceAddFormat(lang));
            }

            //유효한 입력을 했다면, DB 정보 갱신
            if (valid) {
                msg.append(msgMaker.msgTargetPriceSetResult(targetPrice, currentValue, marketId, lang));

                ClientTargetVo target = ClientTargetVo.builder()
                        .coinId(myCoinId)
                        .userId(userId.toString())
                        .price(nf.formatPrice(targetPrice, marketId))
                        .createDate(TimeStamper.getDateTime())
                        .build();

                if (targetPrice >= currentValue) {
                    target.setFocus(TargetFocus.UP);
                } else {
                    target.setFocus(TargetFocus.DOWN);
                }

                if (!clientTargetService.insert(target)) {
                    msg.delete(0, msg.length());
                    msg.append(msgMaker.warningAlreadyTarget(lang));
                }
            }

        }

        msg.append(msgMaker.msgToMain(lang));
        sendMessage(userId, messageId, msg, km.getMainKeyboard(lang));
        clientService.updateStateId(myCoinId, userId.toString(), MenuState.MAIN);
    }

    private void handleDelTarget(Integer userId, Integer messageId, String cmd, Market marketId, Lang lang) {
        StringBuilder msg = new StringBuilder();

        TargetDelCmd inCmd = TargetDelCmd.from(lang, cmd);

        switch (inCmd) {
            case OUT:
                break;
            default:
                try {
                    Double price = Double.valueOf(cmd.replaceAll("[^-?0-9-?.]+", ""));

                    if (clientTargetService.delete(myCoinId, userId.toString(), price) == 1) {
                        msg.append(msgMaker.msgCompleteDelTarget(price, marketId, lang));
                    } else {
                        throw new NumberFormatException();
                    }

                } catch (NumberFormatException e) {
                    msg.append(msgMaker.warningTargetPriceDelFormat(lang));
                }
                break;
        }

        msg.append(msgMaker.msgToMain(lang));
        sendMessage(userId, messageId, msg.toString(), km.getMainKeyboard(lang));
        clientService.updateStateId(myCoinId, userId.toString(), MenuState.MAIN);

    }

    /* 투자 금액 설정 응답 처리 */
    private void handleSetPrice(Integer userId, Integer messageId, String cmd, Market marketId, Lang lang) {
        String priceStr = cmd;
        StringBuilder msg = new StringBuilder("");
        double price = -1;

        try { // case1. 평균단가에 문자가 포함될때
            price = Double.parseDouble(priceStr);
            if (clientService.updateInvest(myCoinId, userId.toString(), price)) {
                if (price == 0) {
                    msg.append(msgMaker.msgPriceInit(lang));
                } // case2. 초기화
                else {
                    msg.append(msgMaker.msgPriceSet(price, marketId, lang));
                } // case3.설정완료
            } else {
                msg.append(msgMaker.warningNeedToStart(lang));
            }
        } catch (NumberFormatException e) {
            msg.append(msgMaker.warningPriceFormat(lang));
        }

        msg.append(msgMaker.msgToMain(lang));

        sendMessage(userId, messageId, msg, km.getMainKeyboard(lang));
        clientService.updateStateId(myCoinId, userId.toString(), MenuState.MAIN);
    }

    /* 코인 개수 설정 응답 처리 */
    private void handleSetNumber(Integer userId, Integer messageId, String cmd, Lang lang) {
        String numberStr = cmd;
        StringBuilder msg = new StringBuilder("");

        try { // case1. 코인개수에 문자가 포함될때
            double number = Double.parseDouble(numberStr);

            if (clientService.updateNumber(myCoinId, userId.toString(), number)) {
                if (number == 0) {
                    msg.append(msgMaker.msgCoinCountInit(lang));
                } // case2. 초기화
                else {
                    msg.append(msgMaker.msgCoinCountSet(number, lang));
                } // case3.설정완료
            } else {
                msg.append(msgMaker.warningNeedToStart(lang));
            }

        } catch (NumberFormatException e) {
            msg.append(msgMaker.warningCoinCountFormat(lang));
        }

        msg.append(msgMaker.msgToMain(lang));

        sendMessage(userId, messageId, msg, km.getMainKeyboard(lang));
        clientService.updateStateId(myCoinId, userId.toString(), MenuState.MAIN);
    }

    /* 개발자에게 메세지 보내기, 응답 처리 */
    private void handleSendMsg(Integer userId, String username, Integer messageId, String message, Lang lang) {

        SendMessageCmd inCmd = SendMessageCmd.from(lang, message);

        switch (inCmd) {
            case OUT:
                sendMessage(userId, messageId, msgMaker.msgToMain(lang), km.getMainKeyboard(lang));
                break;
            default:
                clientSuggestService.insert(myCoinId, userId, username, message);
                sendMessage(userId, messageId, msgMaker.msgThankyouSuggest(lang), km.getMainKeyboard(lang));

                //To Developer
                StringBuilder msg = new StringBuilder("");
                msg.append("메세지가 도착했습니다!\n------------------\n\n");
                msg.append(message);
                msg.append("\n\n------------------\n");
                msg.append(" By ");
                msg.append(username + " [" + userId + " ]");

                sendMessage(this.creatorId(), null, msg, km.getMainKeyboard(lang));
                break;
        }

        clientService.updateStateId(myCoinId, userId.toString(), MenuState.MAIN);
    }


    /* 모든 알림 중지 응답 처리 */
    private void handleConfirmStop(Integer userId, String username, Integer messageId, String cmd, Lang lang) {
        StringBuilder msg = new StringBuilder("");

        StopConfirmCmd inCmd = StopConfirmCmd.from(lang, cmd);
        switch (inCmd) {
            case YES:
                if (clientService.stopChat(myCoinId, userId) && clientTargetService.delete(myCoinId, userId) >= 0) {
                    msg.append(msgMaker.msgStopAllNotice(lang));
                } else {
                    msg.append(msgMaker.warningNeedToStart(lang));
                }
                break;
            case NO:
            default:
                msg.append("\n");
                break;
        }

        msg.append(msgMaker.msgToMain(lang));

        sendMessage(userId, messageId, msg, km.getMainKeyboard(lang));
        clientService.updateStateId(myCoinId, userId.toString(), MenuState.MAIN);
    }


    /* 행복회로 메뉴 클릭 시, 투자금액 && 코인개수 설정 입력되었는지 확인 */
    private MenuState checkHappyLine(Integer userId, Integer messageId, Market marketId, Lang lang) {
        MenuState stateId = MenuState.MAIN;

        String msg = "";
        ClientVo client = clientService.get(myCoinId, userId);
        if (client != null) {
            if (client.getInvest() == null) {
                msg = msgMaker.msgPleaseSetInvestmentAmount(lang);
                sendMessage(userId, messageId, msg, km.getMainKeyboard(lang));
            } else if (client.getCoinCnt() == null) {
                msg = msgMaker.msgPleaseSetTheNumberOfCoins(lang);
                sendMessage(userId, messageId, msg, km.getMainKeyboard(lang));
            } else {
                msg = msgMaker.explainHappyLine(marketId, lang);
                sendMessage(userId, messageId, msg, km.getDefaultKeyboard());
                stateId = MenuState.HAPPY_LINE;
            }
        }

        return stateId;
    }

    /* 행복 회로 응답 처리 */
    private void handleHappyLine(Integer userId, String username, Integer messageId, String cmd, Lang lang) {
        StringBuilder msg = new StringBuilder("");
        String priceStr = cmd;

        try { // case1. 평균단가에 문자가 포함될때
            double happyPrice = Double.parseDouble(priceStr);
            ClientVo client = clientService.get(myCoinId, userId);
            double price = client.getInvest();
            double coinCnt = client.getCoinCnt();
            Market marketId = client.getMarketId();

            msg.append(msgMaker.msgHappyLineResult(price, coinCnt, happyPrice, marketId, lang));

        } catch (NumberFormatException e) {
            msg.append(msgMaker.warningHappyLineFormat(lang));
        }

        msg.append(msgMaker.msgToMain(lang));

        sendMessage(userId, messageId, msg, km.getMainKeyboard(lang));
        clientService.updateStateId(myCoinId, userId.toString(), MenuState.MAIN);

    }

    /* 환경설정 응답 처리 */
    private void handlePreference(Integer userId, String username, Integer messageId, String cmd, Lang lang) {
        MenuState stateId = MenuState.MAIN;

        PrefCmd inCmd = PrefCmd.from(lang, cmd);

        switch (inCmd) {
            case SET_LANG: //언어 설정
                sendMessage(userId, messageId, msgMaker.explainSetLanguage(lang), km.getSetLanguageKeyboard(lang));
                stateId = MenuState.PREF_LANGUAGE;
                break;
            case TIME_ADJUST: //시차 조절
                sendMessage(userId, messageId, msgMaker.explainTimeAdjust(lang), km.getDefaultKeyboard(lang));
                stateId = MenuState.PREF_TIMEADJUST;
                break;
            case OUT:
                sendMessage(userId, messageId, msgMaker.msgToMain(lang), km.getMainKeyboard(lang));
                break;
            default:
                sendMessage(userId, messageId, msgMaker.msgToMain(lang), km.getMainKeyboard(lang));
                break;
        }

        clientService.updateStateId(myCoinId, userId.toString(), stateId);
    }


    /* 환경설정 - 언어 응답 처리 */
    private void handleSetLanguage(Integer userId, String username, Integer messageId, String cmd, Lang lang) {
        StringBuilder msg = new StringBuilder("");
        Lang langID = lang;

        PrefLangCmd inCmd = PrefLangCmd.from(lang, cmd);

        switch (inCmd) {
            case SET_KR:
            case SET_US:
                langID = inCmd.getId();
                if (clientService.updateLanguage(myCoinId, userId.toString(), langID)) {
                    msg.append(msgMaker.msgSetLanguageSuccess(langID));
                    msg.append(msgMaker.msgToMain(langID));
                } else {
                    msg.append(msgMaker.warningWaitSecond(lang));
                    msg.append(msgMaker.msgToMain(lang));
                }
                sendMessage(userId, messageId, msg, km.getMainKeyboard(langID));
                sendMessage(userId, messageId, msgMaker.explainHelp(enabledMarketIds, langID), null);
                break;
            default:
                msg.append(msgMaker.msgToMain(lang));
                sendMessage(userId, messageId, msg, km.getMainKeyboard(langID));
                break;
        }

        clientService.updateStateId(myCoinId, userId.toString(), MenuState.MAIN);
    }


    /* 환경설정 시차 조절 응답 처리 */
    private void handleTimeAdjust(Integer userId, String username, Integer messageId, String cmd, Lang lang) {
        StringBuilder msg = new StringBuilder("");
        String enteredDateStr = cmd;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        if (cmd.equals("0")) {
            clientService.updateLocalTime(myCoinId, userId.toString(), (long) 0);
            msg.append(msgMaker.msgTimeAdjustSuccess(new Date()));
            msg.append(msgMaker.msgToMain(lang));
        } else {
            try {
                Date enteredDate = format.parse(enteredDateStr);
                Date currentDate = new Date();
                long time = enteredDate.getTime() - currentDate.getTime();

                clientService.updateLocalTime(myCoinId, userId.toString(), time);

                msg.append(msgMaker.msgTimeAdjustSuccess(enteredDate));
                msg.append(msgMaker.msgToMain(lang));
            } catch (ParseException e) {
                msg.append(msgMaker.warningTimeAdjustFormat(lang));
                msg.append(msgMaker.msgToMain(lang));
            }
        }


        sendMessage(userId, messageId, msg, km.getMainKeyboard(lang));
        clientService.updateStateId(myCoinId, userId.toString(), MenuState.MAIN);
    }


    /* 현재가 */
    private String messageCurrentPrice(Integer userId) {
        JSONObject coinObj = null;
        double currentValue = 0;
        ClientVo client = clientService.get(myCoinId, userId);
        Market marketId = client.getMarketId();
        Lang lang = client.getLang();

        try {
            coinObj = coinManager.getCoin(myCoinId, marketId);
        } catch (ServerErrorException e) {
            e.printStackTrace();
            return msgMaker.warningWaitSecond(lang) + e.getTelegramMsg();
        }

        if (coinObj == null) {
            log.info("가격정보를 보낼 수 없습니다. :: return NULL");
            return msgMaker.warningWaitSecond(lang) + "Coin NULL";
        }

        currentValue = coinObj.getDouble("last");


        JSONObject coinMoney = null;
        if (inBtcs.get(marketId)) {
            coinMoney = coinManager.getMoney(coinObj, marketId);
        }

        return msgMaker.msgCurrentPrice(currentValue, coinMoney, client);
    }


    /* 거래소별 가격 */
    public String messageEachMarketPrice(Integer userId) {
        LinkedHashMap<Market, Double> lasts = new LinkedHashMap<>();

        Market marketId = null;
        for (int i = 0; i < enabledMarketIds.size(); i++) {
            marketId = enabledMarketIds.get(i);
            lasts.put(marketId, coinManager.getCoinLast(myCoinId, marketId, inBtcs.get(marketId)));
        }

        ClientVo client = clientService.get(myCoinId, userId);
        double exchangeRate = coinManager.getExchangeRate();
        return msgMaker.msgEachMarketPrice(exchangeRate, lasts, client);
    }

    /* 비트코인 대비 변화량 */
    public String messageBTC(Integer userId) {
        JSONObject coin = null;
        JSONObject btc = null;
        StringBuilder msg = new StringBuilder("");

        ClientVo client = clientService.get(myCoinId, userId);
        Market marketId = client.getMarketId();
        Lang lang = client.getLang();
        String date = TimeStamper.getDateTime(client.getLocaltime());

        msg.append(msgMaker.msgBTCCurrentTime(date, lang));

        // * 비트대비 변화량을 제공하지 않는 거래소에 대해서 처리
        if (marketId.isKRW() && (marketId == Market.COINNEST || marketId == Market.KORBIT)) {
            if (marketId == Market.COINNEST) {
                msg.append(msgMaker.msgBTCNotSupportAPI(Market.COINNEST, lang));
            }
            if (marketId == Market.KORBIT) {
                msg.append(msgMaker.msgBTCNotSupportAPI(Market.KORBIT, lang));
            }

            marketId = null;
            Market temp = null;
            for (int i = 0; i < enabledMarketIds.size(); i++) {
                temp = enabledMarketIds.get(i);
                if (temp.isKRW() && temp != Market.COINNEST && temp != Market.KORBIT) {
                    msg.append(msgMaker.msgBTCReplaceAnotherMarket(temp, lang));
                    marketId = temp;
                    break;
                }
            }
            msg.append("\n");
        }

        // * 비트대비 변화량을 제공하지 않는 거래소에 대해서 처리
        if (marketId.isUSD() && marketId == Market.BITTREX) {
            if (marketId == Market.BITTREX) {
                msg.append(msgMaker.msgBTCNotSupportAPI(Market.BITTREX, lang));
            }

            marketId = null;
            Market temp = null;
            for (int i = 0; i < enabledMarketIds.size(); i++) {
                temp = enabledMarketIds.get(i);
                if (temp.isUSD() && temp != Market.BITTREX) {
                    msg.append(msgMaker.msgBTCReplaceAnotherMarket(temp, lang));
                    marketId = temp;
                    break;
                }
            }
            msg.append("\n");
        }


        if (marketId != null) {
            try {
                coin = coinManager.getCoin(myCoinId, marketId);
                btc = coinManager.getCoin(Coin.BTC, marketId);
            } catch (ServerErrorException e) {
                e.printStackTrace();
                return msgMaker.warningWaitSecond(lang) + e.getTelegramMsg();
            }

            if (coin != null && btc != null) {
                double coinCV = coin.getDouble("last");
                double coinBV = coin.getDouble("first");
                double btcCV = btc.getDouble("last");
                double btcBV = btc.getDouble("first");
                JSONObject coinMoney = null;
                if (inBtcs.get(marketId)) {
                    coinMoney = coinManager.getMoney(coin, marketId);
                }
                msg.append(msgMaker.msgBTCResult(coinCV, coinBV, btcCV, btcBV, coinMoney, marketId, lang));

                return msg.toString();
            } else {
                return msgMaker.warningNeedToStart(lang) + "Coin NULL\n";
            }
        } else {
            return msg.toString();
        }

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
                JSONObject coin = coinManager.getCoin(myCoinId, client.getMarketId());
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
                btcObj = coinManager.getCoin(Coin.BTC, client.getMarketId());
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


    /******************/
    /** Send Message **/
    /******************/
    public void sendMessage(Integer id, Integer msgId, StringBuilder msg, ReplyKeyboard keyboard) {
        this.sendMessage(id.toString(), msgId, msg.toString(), keyboard);
    }

    public void sendMessage(Integer id, Integer msgId, String msg, ReplyKeyboard keyboard) {
        this.sendMessage(id.toString(), msgId, msg, keyboard);
    }

    public void sendMessage(String userId, Integer msgId, String msg, ReplyKeyboard keyboard) {
        log.info(">>>  " + myCoinId + "  [id :" + userId + " ]  ");

        SendMessage sendMessage = new SendMessage(userId, msg);
        sendMessage.setReplyToMessageId(msgId);
        sendMessage.enableHtml(true);

        if (keyboard != null) {
            sendMessage.setReplyMarkup(keyboard);
        }

        try {
            this.sender.executeAsync(sendMessage, new SentCallback<Message>() {

                @Override
                public void onError(BotApiMethod<Message> arg0, TelegramApiRequestException e) {
                    log.info(" {}  [id : {} ]  에게 메세지를 보낼 수 없습니다.  : {}", myCoinId, userId, e.getMessage());
                    clientService.increaseClientErrorCount(myCoinId, userId);
                }

                @Override
                public void onException(BotApiMethod<Message> arg0, Exception e) {
                }

                @Override
                public void onResult(BotApiMethod<Message> arg0, Message arg1) {
                }
            });
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }

    /* 목표가 알림 */
    public void sendTargetPriceMessage(List<ClientTargetVo> targets, Market marketId, JSONObject coinObj) {
        double currentValue = coinObj.getDouble("last");
        if (inBtcs.get(marketId)) {
            currentValue = coinManager.getMoney(coinObj, marketId).getDouble("last");
        }

        ClientTargetVo target = null;
        String userId;
        Lang lang = null;
        String msg = "";

        Double targetPrice = -1.0;
        for (int i = 0; i < targets.size(); i++) {
            target = targets.get(i);
            userId = target.getUserId();
            lang = clientService.get(myCoinId, userId).getLang();
            targetPrice = target.getPrice();

            msg = msgMaker.msgTargetPriceNotify(currentValue, targetPrice, marketId, lang);
            sendMessage(target.getUserId(), null, msg, null);
            if (clientTargetService.delete(myCoinId, userId, targetPrice) == 1) {
                sendMessage(userId, null, msgMaker.msgTargetPriceDeleted(lang), null);
            }
        }
    }

    /* 시간 알림 */
    public void sendTimelyMessage(List<ClientVo> clients, Market marketId, TimelyInfoVo coinCurrent, TimelyInfoVo coinBefore) {
        ClientVo client = null;

        JSONObject coinCurrentMoney = null;
        JSONObject coinBeforeMoney = null;
        if (inBtcs.get(marketId)) {
            coinCurrentMoney = coinManager.getMoney(coinCurrent, marketId);
            coinBeforeMoney = coinManager.getMoney(coinBefore, marketId);
        }

        int clientLength = clients.size();
        for (int i = 0; i < clientLength; i++) {
            client = clients.get(i);
            String msg = msgMaker.msgSendTimelyMessage(client, coinCurrent, coinBefore, coinCurrentMoney, coinBeforeMoney);
            sendMessage(client.getUserId(), null, msg, null);
        }
    }

    /* 일일 알림 */
    public void sendDailyMessage(List<ClientVo> clients, Market marketId, TimelyInfoVo coinCurrent, TimelyInfoVo coinBefore) {
        ClientVo client = null;
        int clientLength = clients.size();

        JSONObject coinCurrentMoney = null;
        JSONObject coinBeforeMoney = null;
        if (inBtcs.get(marketId)) {
            coinCurrentMoney = coinManager.getMoney(coinCurrent, marketId);
            coinBeforeMoney = coinManager.getMoney(coinBefore, marketId);
        }

        String msg = null;
        for (int i = 0; i < clientLength; i++) {
            client = clients.get(i);
            msg = msgMaker.msgSendDailyMessage(client, coinCurrent, coinBefore, coinCurrentMoney, coinBeforeMoney);
            sendMessage(client.getUserId(), null, msg, null);

            if (client.getCoinCnt() != null && client.getInvest() != null) {
                sendMessage(client.getUserId(), null, calcResult(client, coinCurrent.getLast()), null);
            }
        }
    }
}


