package com.podo.coinchatbot.telegram;

import com.podo.coinchatbot.core.Coin;
import com.podo.coinchatbot.core.Lang;
import com.podo.coinchatbot.core.Market;
import com.podo.coinchatbot.telegram.command.MainCommand;
import com.podo.coinchatbot.telegram.command.MarketCommand;
import com.podo.coinchatbot.telegram.command.SendMessageCommand;
import com.podo.coinchatbot.telegram.model.UserDto;
import com.podo.coinchatbot.telegram.model.ClientTargetDto;
import com.podo.coinchatbot.telegram.model.CoinConfigDto;
import com.podo.coinchatbot.telegram.model.CoinInfoDto;
import com.podo.coinchatbot.telegram.model.TimelyInfoDto;
import com.podo.coinchatbot.telegram.util.NumberFormatter;
import com.podo.coinchatbot.telegram.util.TimeStamper;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

public class MessageMaker {
    private Coin myCoinId;
    private NumberFormatter numberFormatter;
    private String version;
    private String exInvestKR;
    private String exInvestUS;
    private String exCoinCnt;
    private String exTargetKR;
    private String exTargetUS;
    private String exTargetRate;
    private HashMap<Market, Boolean> inBtcs;

    public MessageMaker(Coin myCoinId, CoinConfigDto config, NumberFormatter nts, HashMap<Market, Boolean> inBtcs) {
        this.myCoinId = myCoinId;
        this.inBtcs = inBtcs;
        version = config.getVersion();
        exInvestKR = config.getExInvestKRW();
        exInvestUS = config.getExInvestUSD();
        exCoinCnt = config.getExCoinCnt();
        exTargetKR = config.getExTargetKRW();
        exTargetUS = config.getExTargetUSD();
        exTargetRate = config.getExTargetRate();

        this.numberFormatter = nts;

    }


    /********************/
    /** Common Message **/
    /********************/
    public String warningNeedToStart(Lang lang) {
        StringBuilder msg = new StringBuilder("");
        switch (lang) {
            case KR:
                msg.append("알림을 먼저 시작해주세요. \n 명령어 /start  <<< 클릭!.\n");
                break;
            case EN:
                msg.append("Please start this service first.\n /start <<< click here.\n");
                break;
        }
        return msg.toString();
    }

    public String warningWaitSecond(Lang lang) {
        StringBuilder msg = new StringBuilder("");
        switch (lang) {
            case KR:
                msg.append("잠시 후 다시 보내주세요.\n");
                break;
            case EN:
                msg.append("Please send message again after a while.\n");
                break;
        }
        return msg.toString();
    }


    public String msgToMain(Lang lang) {
        StringBuilder msg = new StringBuilder("");
        switch (lang) {
            case KR:
                msg.append("\n# 메인화면으로 이동합니다.\n");
                break;
            case EN:
                msg.append("\n# Changed to main menu.\n");
                break;
        }
        return msg.toString();
    }

    public String msgPleaseSetInvestmentAmount(Lang lang) {
        StringBuilder msg = new StringBuilder("");
        switch (lang) {
            case KR:
                msg.append("먼저 투자금액을 설정해주세요.\n메뉴에서 '" + MainCommand.SET_INVEST.getCommand(lang) + "' 을 클릭해주세요.");
                break;
            case EN:
                msg.append("Please set your investment amount first.\nPlease Click '" + MainCommand.SET_INVEST.getCommand(lang) + "' on the main menu.");
                break;
        }
        return msg.toString();
    }

    public String msgPleaseSetTheNumberOfCoins(Lang lang) {
        StringBuilder msg = new StringBuilder("");
        switch (lang) {
            case KR:
                msg.append("먼저 코인개수를 설정해주세요.\n메뉴에서 '" + MainCommand.SET_COINCNT.getCommand(lang) + "' 을 클릭해주세요.");
                break;
            case EN:
                msg.append("Please set the number of coins first.\nPlease Click '" + MainCommand.SET_COINCNT.getCommand(lang) + "' on the main menu.");
                break;
        }
        return msg.toString();
    }

    /*******************/
    /** Start Message **/
    /*******************/


    /*****************************/
    /** Current Price Message ****/
    /*****************************/
    public String msgCurrentPrice(double currentValue, JSONObject coinMoney, UserDto client) {
        StringBuilder msg = new StringBuilder();
        Lang lang = client.getLang();
        Market marketId = client.getMarket();
        String date = TimeStamper.getDateTime(client.getLocaltime());

        switch (lang) {
            case KR:
                msg.append("현재시각 : " + date + "\n");
                if (inBtcs.get(marketId)) {
                    double currentMoney = coinMoney.getDouble("last");
                    double currentBTC = currentValue;
                    msg.append("현재가격 : " + numberFormatter.toMoneyStr(currentMoney, marketId) + " [" + numberFormatter.toBTCStr(currentBTC) + "]\n");
                } else {
                    msg.append("현재가격 : " + numberFormatter.toMoneyStr(currentValue, marketId) + "\n");
                }
                break;

            case EN:
                msg.append("Current Time  : " + date + "\n");
                if (inBtcs.get(marketId)) {
                    double currentMoney = coinMoney.getDouble("last");
                    double currentBTC = currentValue;
                    msg.append("Current Price : " + numberFormatter.toMoneyStr(currentMoney, marketId) + " [" + numberFormatter.toBTCStr(currentBTC) + "]\n");
                } else {
                    msg.append("Current Price : " + numberFormatter.toMoneyStr(currentValue, marketId) + "\n");
                }
                break;
        }
        return msg.toString();
    }

    /**********************************/
    /** Each Market Price Message *****/
    /**********************************/
    public String msgEachMarketPrice(double exchangeRate, LinkedHashMap<Market, Double> lasts, UserDto client) {
        StringBuilder msg = new StringBuilder();
        Market marketId = client.getMarket();
        Lang lang = client.getLang();
        String date = TimeStamper.getDateTime(client.getLocaltime());
        double mylast = lasts.get(marketId);

        switch (lang) {
            case KR:
                msg.append("현재 시각  : " + date + "\n");
                msg.append("\n");
                msg.append("나의 거래소 : " + toMarketStr(marketId, lang) + "\n");
                msg.append("금일의 환율 : $1 = " + numberFormatter.toExchangeRateKRWStr(exchangeRate) + "\n");
                msg.append("----------------------------\n");
                break;
            case EN:
                msg.append("Current Time  : " + date + "\n");
                msg.append("\n");
                msg.append("My Market     : " + toMarketStr(marketId, lang) + "\n");
                msg.append("Exchange rate : $1 = " + numberFormatter.toKRWStr(exchangeRate) + "\n");
                msg.append("----------------------------\n");
                break;
        }

        Iterator<Market> iter = lasts.keySet().iterator();

        Market marketKRW = Market.COINONE; // KRW 대표
        Market marketUSD = Market.BITFINEX; // USD 대표

        if (marketId.isKRW()) { // 설정된 마켓이 한화인 경우,
            while (iter.hasNext()) {
                Market key = iter.next();

                if (key == marketId) { // 내 마켓
                    msg.append("★ ");
                }

                msg.append(toMarketStr(key, lang) + "  : ");
                if (lasts.get(key) == -1) {
                    msg.append("Server Error");
                } else {
                    if (key.isKRW()) {
                        msg.append(numberFormatter.toMoneyStr(lasts.get(key), marketKRW));
                        msg.append("  [" + numberFormatter.toMoneyStr(lasts.get(key) / exchangeRate, marketUSD) + "]");
                    } else if (key.isUSD()) {
                        msg.append(numberFormatter.toMoneyStr(lasts.get(key) * exchangeRate, marketKRW));
                        msg.append("  [" + numberFormatter.toMoneyStr(lasts.get(key), marketUSD) + "]");
                        msg.append(" ( P. " + numberFormatter.toSignPercentStr(mylast, lasts.get(key) * exchangeRate) + ") ");
                    }
                }
                msg.append("\n");
            }
        } else if (marketId.isUSD()) { // 설정된 마켓이 달러인 경우,
            while (iter.hasNext()) {
                Market key = iter.next();
                if (key == marketId) {
                    msg.append("★ ");
                }

                msg.append(toMarketStr(key, lang) + "  : ");
                if (lasts.get(key) == -1) {
                    msg.append("Server Error");
                } else {
                    if (key.isKRW()) {
                        msg.append(numberFormatter.toMoneyStr(lasts.get(key) / exchangeRate, marketUSD));
                        msg.append("  [" + numberFormatter.toMoneyStr(lasts.get(key), marketKRW) + "]");
                    } else if (key.isUSD()) {
                        msg.append(numberFormatter.toMoneyStr(lasts.get(key), marketUSD));
                        msg.append("  [" + numberFormatter.toMoneyStr(lasts.get(key) * exchangeRate, marketKRW) + "]");
                    }
                }
                msg.append("\n");
            }
        }
        return msg.toString();
    }

    /******************************/
    /** BTC change rate Message*****/
    /******************************/
    public String msgBTCCurrentTime(String date, Lang lang) {
        StringBuilder msg = new StringBuilder();
        switch (lang) {
            case KR:
                msg.append("현재시각 : " + date + "\n");
                break;
            case EN:
                msg.append("Current Time : " + date + "\n");
                break;
        }

        msg.append("----------------------\n");
        msg.append("\n");
        return msg.toString();
    }

    public String msgBTCNotSupportAPI(Market marketID, Lang lang) {
        String marketStr = toMarketStr(marketID, lang);
        StringBuilder msg = new StringBuilder("");
        switch (lang) {
            case KR:
                msg.append(marketStr + " API는 해당 정보를 제공하지 않습니다.\n");
                break;
            case EN:
                msg.append(marketStr + " market API does not provide this information.\n");
                break;
        }
        return msg.toString();
    }

    public String msgBTCReplaceAnotherMarket(Market marketId, Lang lang) {
        String marketStr = toMarketStr(marketId, lang);
        StringBuilder msg = new StringBuilder("");
        switch (lang) {
            case KR:
                msg.append(marketStr + " 기준 정보로 대체합니다.\n");
                break;
            case EN:
                msg.append("Replace with " + marketStr + " market information.\n");
                break;
        }
        return msg.toString();
    }

    public String msgBTCResult(double coinCV, double coinBV, double btcCV, double btcBV, JSONObject coinMoney, Market marketId, Lang lang) {
        StringBuilder msg = new StringBuilder("");
        switch (lang) {
            case KR:
                msg.append("BTC 현재 시각 가격 : " + numberFormatter.toOnlyBTCMoneyStr(btcCV, marketId) + "\n");
                msg.append("BTC 24시간전 가격 : " + numberFormatter.toOnlyBTCMoneyStr(btcBV, marketId) + "\n");
                msg.append("\n");
                if (inBtcs.get(marketId)) {
                    msg.append(myCoinId + " 현재 시각 가격 : " + numberFormatter.toMoneyStr(coinMoney.getDouble("last"), marketId) + " [" + numberFormatter.toBTCStr(coinCV) + "]\n");
                    msg.append(myCoinId + " 24시간전 가격 : " + numberFormatter.toMoneyStr(coinMoney.getDouble("first"), marketId) + " [" + numberFormatter.toBTCStr(coinBV) + "]\n");
                } else {
                    msg.append(myCoinId + " 현재 시각 가격 : " + numberFormatter.toMoneyStr(coinCV, marketId) + "\n");
                    msg.append(myCoinId + " 24시간전 가격 : " + numberFormatter.toMoneyStr(coinBV, marketId) + "\n");
                }
                msg.append("\n");
                msg.append("BTC 24시간 변화량 : " + numberFormatter.toSignPercentStr(btcCV, btcBV) + "\n");
                msg.append(myCoinId + " 24시간 변화량 : " + numberFormatter.toSignPercentStr(coinCV, coinBV) + "\n");
                break;

            case EN:
                msg.append("BTC Price at current time : " + numberFormatter.toMoneyStr(btcCV, marketId) + "\n");
                msg.append("BTC Price before 24 hours : " + numberFormatter.toMoneyStr(btcBV, marketId) + "\n");
                msg.append("\n");
                if (inBtcs.get(marketId)) {
                    msg.append(myCoinId + " Price at current time : " + numberFormatter.toMoneyStr(coinMoney.getDouble("last"), marketId) + " [" + numberFormatter.toBTCStr(coinCV) + "]\n");
                    msg.append(myCoinId + " Price before 24 hours : " + numberFormatter.toMoneyStr(coinMoney.getDouble("first"), marketId) + " [" + numberFormatter.toBTCStr(coinBV) + "]\n");
                } else {
                    msg.append(myCoinId + " Price at current time : " + numberFormatter.toMoneyStr(coinCV, marketId) + "\n");
                    msg.append(myCoinId + " Price before 24 hours : " + numberFormatter.toMoneyStr(coinBV, marketId) + "\n");
                }
                msg.append("\n");
                msg.append("BTC 24 hour rate of change : " + numberFormatter.toSignPercentStr(btcCV, btcBV) + "\n");
                msg.append(myCoinId + " 24 hour rate of change : " + numberFormatter.toSignPercentStr(coinCV, coinBV) + "\n");
                break;
        }
        return msg.toString();
    }


    /**************************/
    /** Calculate Message *****/
    /**************************/
    public String msgCalcResult(double price, double cnt, double avgPrice, double coinValue, JSONObject btcObj, UserDto client) {
        StringBuilder msg = new StringBuilder();
        Market marketId = client.getMarket();
        Lang lang = client.getLang();
        String date = TimeStamper.getDateTime(client.getLocaltime());

        switch (lang) {
            case KR:
                msg.append("현재 시각  : " + date + "\n");
                msg.append("\n");
                msg.append("코인개수 : " + numberFormatter.toCoinCntStr(cnt, lang) + "\n");
                if (inBtcs.get(marketId)) {
                    double btcMoney = btcObj.getDouble("last");
                    double avgBTC = avgPrice / btcMoney;
                    double coinBTC = coinValue;
                    double coinMoney = coinValue * btcMoney;
                    msg.append("평균단가 : " + numberFormatter.toMoneyStr(avgPrice, marketId) + "  [" + numberFormatter.toBTCStr(avgBTC) + "]\n");
                    msg.append("현재단가 : " + numberFormatter.toMoneyStr(coinMoney, marketId) + " [" + numberFormatter.toBTCStr(coinBTC) + "]\n");
                    msg.append("---------------------\n");
                    msg.append("투자금액 : " + numberFormatter.toInvestAmountStr(price, marketId) + "\n");
                    msg.append("현재금액 : " + numberFormatter.toInvestAmountStr((int) (coinMoney * cnt), marketId) + "\n");
                    msg.append("손익금액 : " + numberFormatter.toSignInvestAmountStr((int) ((coinMoney * cnt) - (cnt * avgPrice)), marketId) + " (" + numberFormatter.toSignPercentStr((int) (coinMoney * cnt), (int) (cnt * avgPrice)) + ")\n");
                    msg.append("\n");
                } else {
                    double coinMoney = coinValue;
                    msg.append("평균단가 : " + numberFormatter.toMoneyStr(avgPrice, marketId) + "\n");
                    msg.append("현재단가 : " + numberFormatter.toMoneyStr(coinMoney, marketId) + "\n");
                    msg.append("---------------------\n");
                    msg.append("투자금액 : " + numberFormatter.toInvestAmountStr(price, marketId) + "\n");
                    msg.append("현재금액 : " + numberFormatter.toInvestAmountStr((int) (coinMoney * cnt), marketId) + "\n");
                    msg.append("손익금액 : " + numberFormatter.toSignInvestAmountStr((int) ((coinMoney * cnt) - (cnt * avgPrice)), marketId) + " (" + numberFormatter.toSignPercentStr((int) (coinMoney * cnt), (int) (cnt * avgPrice)) + ")\n");
                    msg.append("\n");
                }
                break;

            case EN:
                msg.append("Current Time  : " + date + "\n");
                msg.append("\n");
                msg.append("The number of coins : " + numberFormatter.toCoinCntStr(cnt, lang) + "\n");
                if (inBtcs.get(marketId)) {
                    double btcMoney = btcObj.getDouble("last");
                    double avgBTC = avgPrice / btcMoney;
                    double coinBTC = coinValue;
                    double coinMoney = coinValue * btcMoney;
                    msg.append("Average Coin Price : " + numberFormatter.toMoneyStr(avgPrice, marketId) + "  [ " + numberFormatter.toBTCStr(avgBTC) + "]\n");
                    msg.append("Current Coin Price : " + numberFormatter.toMoneyStr(coinMoney, marketId) + " [ " + numberFormatter.toBTCStr(coinBTC) + "]\n");
                    msg.append("---------------------\n");
                    msg.append("Investment Amount : " + numberFormatter.toInvestAmountStr(price, marketId) + "\n");
                    msg.append("Curernt Amount : " + numberFormatter.toInvestAmountStr((int) (coinMoney * cnt), marketId) + "\n");
                    msg.append("Profit and loss : " + numberFormatter.toSignInvestAmountStr((int) ((coinMoney * cnt) - (cnt * avgPrice)), marketId) + " (" + numberFormatter.toSignPercentStr((int) (coinMoney * cnt), (int) (cnt * avgPrice)) + ")\n");
                    msg.append("\n");
                } else {
                    double coinMoney = coinValue;
                    msg.append("Average Coin Price : " + numberFormatter.toMoneyStr(avgPrice, marketId) + "\n");
                    msg.append("Current Coin Price : " + numberFormatter.toMoneyStr(coinMoney, marketId) + "\n");
                    msg.append("---------------------\n");
                    msg.append("Investment Amount : " + numberFormatter.toInvestAmountStr(price, marketId) + "\n");
                    msg.append("Curernt Amount : " + numberFormatter.toInvestAmountStr((int) (coinMoney * cnt), marketId) + "\n");
                    msg.append("Profit and loss : " + numberFormatter.toSignInvestAmountStr((int) ((coinMoney * cnt) - (cnt * avgPrice)), marketId) + " (" + numberFormatter.toSignPercentStr((int) (coinMoney * cnt), (int) (cnt * avgPrice)) + ")\n");
                    msg.append("\n");
                }
                break;
        }
        return msg.toString();
    }

    /***************************/
    /** Happy Line Message *****/
    /***************************/
    public String warningHappyLineFormat(Lang lang) {
        StringBuilder msg = new StringBuilder("");
        switch (lang) {
            case KR:
                msg.append("코인개수는 숫자로만 입력해주세요.\n");
                break;
            case EN:
                msg.append("Please enter the number of coins only in numbers.\n");
                break;
        }
        return msg.toString();
    }

    public String explainHappyLine(Market marketId, Lang lang) {
        StringBuilder msg = new StringBuilder();
        String exampleTarget = null;
        if (marketId.isKRW()) {
            exampleTarget = exTargetKR;
        }
        if (marketId.isUSD()) {
            exampleTarget = exTargetUS;
        }

        switch (lang) {
            case KR:
                msg.append("원하시는 코인가격을 입력해주세요.\n");
                msg.append("희망 손익금을 확인 하실 수 있습니다.\n");
                msg.append("\n");
                msg.append("* 코인가격은 숫자로 입력해주세요.\n");
                msg.append("* ex) " + exampleTarget + "  : 희망 코인가격 " + numberFormatter.toMoneyStr(Double.parseDouble(exampleTarget), marketId) + "\n");
                msg.append("\n");
                msg.append("\n");
                msg.append("# 메인으로 돌아가시려면 문자를 입력해주세요.\n");
                break;
            case EN:
                msg.append("Please enter the desired coin price.\n");
                msg.append("if enter your desired coin price,  you can see expected profit and loss.\n");
                msg.append("\n");
                msg.append("* Please enter the coin price in numbers only.\n");
                msg.append("* example) " + exampleTarget + "  : desired coin price " + numberFormatter.toMoneyStr(Double.parseDouble(exampleTarget), marketId) + " set\n");
                msg.append("\n");
                msg.append("\n");
                msg.append("# To return to main, enter a character.\n");
                break;
        }

        return msg.toString();
    }

    public String msgHappyLineResult(double price, double coinCnt, double happyPrice, Market marketId, Lang lang) {
        double avgPrice = (double) price / coinCnt;
        StringBuilder msg = new StringBuilder();

        switch (lang) {
            case KR:
                msg.append("코인개수 : " + numberFormatter.toCoinCntStr(coinCnt, lang) + "\n");
                msg.append("평균단가 : " + numberFormatter.toMoneyStr(avgPrice, marketId) + "\n");
                msg.append("희망단가 : " + numberFormatter.toMoneyStr(happyPrice, marketId) + "\n");
                msg.append("---------------------\n");
                msg.append("투자금액 : " + numberFormatter.toInvestAmountStr(price, marketId) + "\n");
                msg.append("희망금액 : " + numberFormatter.toInvestAmountStr((long) (happyPrice * coinCnt), marketId) + "\n");
                msg.append("손익금액 : " + numberFormatter.toSignInvestAmountStr((long) ((happyPrice * coinCnt) - (price)), marketId) + " (" + numberFormatter.toSignPercentStr((int) (happyPrice * coinCnt), price) + ")\n");
                break;
            case EN:
                msg.append("The number of coins : " + numberFormatter.toCoinCntStr(coinCnt, lang) + "\n");
                msg.append("Average Coin Price  : " + numberFormatter.toMoneyStr(avgPrice, marketId) + "\n");
                msg.append("Desired Coin Price  : " + numberFormatter.toMoneyStr(happyPrice, marketId) + "\n");
                msg.append("---------------------\n");
                msg.append("Investment Amount : " + numberFormatter.toInvestAmountStr(price, marketId) + "\n");
                msg.append("Desired Amount : " + numberFormatter.toInvestAmountStr((long) (happyPrice * coinCnt), marketId) + "\n");
                msg.append("Profit and loss : " + numberFormatter.toSignInvestAmountStr((long) ((happyPrice * coinCnt) - (price)), marketId) + "(" + numberFormatter.toSignPercentStr((int) (happyPrice * coinCnt), price) + ")\n");
                break;
        }

        return msg.toString();
    }

    /*********************************/
    /** Set investment Price Message**/
    /********************************/
    public String explainSetPrice(Lang lang, Market marketId) {
        String exampleInvest = null;
        if (marketId.isKRW()) {
            exampleInvest = exInvestKR;
        }
        if (marketId.isUSD()) {
            exampleInvest = exInvestUS;
        }

        StringBuilder msg = new StringBuilder();
        switch (lang) {
            case KR:
                msg.append("투자금액을 입력해주세요.\n");
                msg.append("투자금액과 코인개수를 입력하시면 손익금을 확인 하실 수 있습니다.\n");
                msg.append("\n");
                msg.append("* 투자금액은 숫자로만 입력해주세요.\n");
                msg.append("* 0을 입력하시면 초기화됩니다.\n");
                msg.append("* ex) " + 0 + " : 초기화\n");
                msg.append("* ex) " + exampleInvest + " : 투자금액 " + numberFormatter.toInvestAmountStr(Double.parseDouble(exampleInvest), marketId) + " 설정\n");
                msg.append("\n");
                msg.append("\n");
                msg.append("# 메인으로 돌아가시려면 문자를 입력해주세요.\n");
                break;
            case EN:
                msg.append("Please enter your investment amount.\n");
                msg.append("If you enter the amount of investment and the number of coins, you can see profit and loss.\n");
                msg.append("\n");
                msg.append("* Please enter the investment amount in numbers only.\n");
                msg.append("* If you enter 0, it is initialized.\n");
                msg.append("* example) " + 0 + " : Init investment amount\n");
                msg.append("* example) " + exampleInvest + " : investment amount " + numberFormatter.toInvestAmountStr(Double.parseDouble(exampleInvest), marketId) + " set\n");
                msg.append("\n");
                msg.append("\n");
                msg.append("# To return to main, enter a character.\n");
                break;
        }
        return msg.toString();
    }

    public String warningPriceFormat(Lang lang) {
        StringBuilder msg = new StringBuilder("");
        switch (lang) {
            case KR:
                msg.append("투자금액은 숫자로만 입력해주세요.\n");
                break;
            case EN:
                msg.append("Please enter the investment amount only in numbers.\n");
                break;
        }
        return msg.toString();
    }

    public String msgPriceInit(Lang lang) {
        StringBuilder msg = new StringBuilder("");
        switch (lang) {
            case KR:
                msg.append("투자금액이 초기화 되었습니다.\n");
                break;
            case EN:
                msg.append("Investment Price has been init.\n");
                break;
        }
        return msg.toString();
    }

    public String msgPriceSet(double price, Market marketId, Lang lang) {
        StringBuilder msg = new StringBuilder("");
        switch (lang) {
            case KR:
                msg.append("투자금액이 " + numberFormatter.toInvestAmountStr(price, marketId) + "으로 설정되었습니다.\n");
                break;
            case EN:
                msg.append("The investment amount is set at " + numberFormatter.toInvestAmountStr(price, marketId) + "\n");
                break;
        }
        return msg.toString();
    }

    /***************************/
    /** Set Coin Count Message**/
    /***************************/
    public String explainSetCoinCount(Lang lang) {
        StringBuilder msg = new StringBuilder();
        switch (lang) {
            case KR:
                msg.append("코인개수를 입력해주세요.\n");
                msg.append("투자금액과 코인개수를 입력하시면 손익금을 확인 하실 수 있습니다.\n");
                msg.append("\n");
                msg.append("* 코인개수는 숫자로만 입력해주세요.\n");
                msg.append("* 0을 입력하시면 초기화됩니다.\n");
                msg.append("* ex) " + 0 + " : 초기화\n");
                msg.append("* ex) " + exCoinCnt + " : 코인개수 " + numberFormatter.toCoinCntStr(Double.parseDouble(exCoinCnt), lang) + " 설정\n");
                msg.append("\n");
                msg.append("\n");
                msg.append("# 메인으로 돌아가시려면 문자를 입력해주세요.\n");
                break;
            case EN:
                msg.append("Please enter your number of coins.\n");
                msg.append("If you enter the amount of investment and the number of coins, you can see profit and loss.\n");
                msg.append("\n");
                msg.append("* Please enter the number of coins in numbers only.\n");
                msg.append("* If you enter 0, it is initialized.\n");
                msg.append("* example) " + 0 + " : Init the number of coins\n");
                msg.append("* example) " + exCoinCnt + " : the number of coins " + numberFormatter.toCoinCntStr(Double.parseDouble(exCoinCnt), lang) + " set\n");
                msg.append("\n");
                msg.append("\n");
                msg.append("# To return to main, enter a character.\n");
                break;
        }
        return msg.toString();
    }

    public String warningCoinCountFormat(Lang lang) {
        StringBuilder msg = new StringBuilder("");
        switch (lang) {
            case KR:
                msg.append("코인개수는 숫자로만 입력해주세요.\n");
                break;
            case EN:
                msg.append("Please enter the number of coins only in numbers.\n");
                break;
        }
        return msg.toString();
    }

    public String msgCoinCountInit(Lang lang) {
        StringBuilder msg = new StringBuilder("");
        switch (lang) {
            case KR:
                msg.append("코인개수가 초기화 되었습니다.\n");
                break;
            case EN:
                msg.append("Investment Price has been init.\n");
                break;
        }
        return msg.toString();
    }

    public String msgCoinCountSet(double number, Lang lang) {
        StringBuilder msg = new StringBuilder("");
        switch (lang) {
            case KR:
                msg.append("코인개수가 " + numberFormatter.toCoinCntStr(number, lang) + "로 설정되었습니다.\n");
                break;
            case EN:
                msg.append("The number of coins is set at " + numberFormatter.toCoinCntStr(number, lang) + "\n");
                break;
        }
        return msg.toString();
    }

    /************************/
    /** Set Market Message **/
    /************************/
    public String explainMarketSet(Lang lang) {
        StringBuilder msg = new StringBuilder();
        switch (lang) {
            case KR:
                msg.append("거래중인 거래소를 설정해주세요.\n");
                msg.append("모든 정보는 설정 거래소 기준으로 전송됩니다.\n");
                break;
            case EN:
                msg.append("Please select an market.\n");
                msg.append("All information will be sent on the market you selected.\n");
                break;
        }
        return msg.toString();
    }

    public String msgMarketSet(Market marketId, Lang lang) {
        StringBuilder msg = new StringBuilder("");
        String marketStr = toMarketStr(marketId, lang);

        switch (lang) {
            case KR:
                msg.append("거래소가 " + marketStr + "(으)로 설정되었습니다.\n");
                break;
            case EN:
                msg.append("The exchange has been set up as " + marketStr + ".\n");
                break;
        }
        return msg.toString();
    }

    public String msgMarketNoSet(Lang lang) {
        StringBuilder msg = new StringBuilder("");
        switch (lang) {
            case KR:
                msg.append("거래소가 설정되지 않았습니다.\n");
                break;
            case EN:
                msg.append("The market has not been set up.\n");
                break;
        }
        return msg.toString();
    }

    public String msgMarketSetChangeCurrency(UserDto client, Double changePrice, List<Double> currentTargetPrices, List<Double> changedTargetPrices, Market changeMarketId) {
        StringBuilder msg = new StringBuilder();
        Market marketId = client.getMarket();
        Lang lang = client.getLang();
        Double currentPrice = client.getInvest();

        switch (lang) {
            case KR:
                msg.append("변경하신 거래소의 화폐단위가 변경되어,\n");
                msg.append("설정하신 투자금액/목표가를 환율에 맞추어 변동하였습니다.\n");
                if (currentPrice != null) {
                    msg.append("투자금액 : " + numberFormatter.toInvestAmountStr(currentPrice, marketId) + " -> " + numberFormatter.toInvestAmountStr(changePrice, changeMarketId) + "\n");
                }
                for (int i = 0; i < currentTargetPrices.size(); i++) {
                    msg.append("목표가격 #" + numberFormatter.toNumberStr(i + 1, 2) + ": " + numberFormatter.toMoneyStr(currentTargetPrices.get(i), marketId) + " -> " + numberFormatter.toMoneyStr(changedTargetPrices.get(i), changeMarketId) + "\n");
                }
                break;
            case EN:
                msg.append("* The currency unit of the exchange has been changed,\n");
                msg.append("the investment amount / target price you set has been changed to match the exchange rate.\n");
                msg.append("\n");
                if (currentPrice != null) {
                    msg.append("Investment amount : " + numberFormatter.toInvestAmountStr(currentPrice, marketId) + " -> " + numberFormatter.toInvestAmountStr(changePrice, changeMarketId) + "\n");
                }
                for (int i = 0; i < currentTargetPrices.size(); i++) {
                    msg.append("Target Price #" + numberFormatter.toNumberStr(i + 1, 2) + ": " + numberFormatter.toMoneyStr(currentTargetPrices.get(i), marketId) + " -> " + numberFormatter.toMoneyStr(changedTargetPrices.get(i), changeMarketId) + "\n");
                }
                break;
        }

        return msg.toString();
    }


    /*****************************/
    /**** Target Price Message ***/
    /*****************************/
    public String showTargetList(Lang lang, Market marketId, List<ClientTargetDto> targets) {
        StringBuilder msg = new StringBuilder();

        ClientTargetDto target;
        switch (lang) {
            case KR:
                msg.append("설정된 목표가격은 다음과 같습니다.\n");
                msg.append("-------------\n");
                if (targets.size() == 0) {
                    msg.append("설정된 목표가격이 없습니다\n");
                }

                for (int i = 0; i < targets.size(); i++) {
                    target = targets.get(i);
                    msg.append(numberFormatter.toNumberStr(i + 1, 2) + "# " + numberFormatter.toMoneyStr(target.getPrice(), marketId) + " " + target.getFocus().kr() + "\n");
                }
                break;
            case EN:
                msg.append("Current Target Setting.\n");
                msg.append("-------------\n");
                if (targets.size() == 0) {
                    msg.append("There are no set target prices.\n");
                }
                for (int i = 0; i < targets.size(); i++) {
                    target = targets.get(i);
                    msg.append(numberFormatter.toNumberStr(i + 1, 2) + "# " + numberFormatter.toMoneyStr(target.getPrice(), marketId) + " " + target.getFocus().en() + "\n");
                }
                break;
        }

        return msg.toString();
    }

    public String explainTargetAdd(Lang lang, Market marketId) {
        StringBuilder msg = new StringBuilder();
        String exampleTarget = null;
        if (marketId.isKRW()) {
            exampleTarget = exTargetKR;
        }
        if (marketId.isUSD()) {
            exampleTarget = exTargetUS;
        }

        switch (lang) {
            case KR:
                msg.append("목표가격을 설정해주세요.\n");
                msg.append("목표가격이 되었을 때 알림이 전송됩니다.\n");
                msg.append("목표가격을 위한 가격정보는 1분 주기로 갱신됩니다.\n");
                msg.append("\n");
                msg.append("* 목표가격은 숫자 또는 백분율로 입력해주세요.\n");
                msg.append("* ex) " + exampleTarget + "  : 목표가격 " + numberFormatter.toMoneyStr(Double.parseDouble(exampleTarget), marketId) + "\n");
                msg.append("* ex) " + exTargetRate + "    : 현재가 +" + exTargetRate + "\n");
                msg.append("* ex) -" + exTargetRate + "  : 현재가 -" + exTargetRate + "\n");
                msg.append("\n");
                msg.append("\n");
                msg.append("# 메인으로 돌아가시려면 문자를 입력해주세요.\n");
                break;
            case EN:
                msg.append("Please set Target Price.\n");
                msg.append("Once you reach the target price, you will be notified.\n");
                msg.append("Coin price information is updated every 1 minute.\n");
                msg.append("\n");
                msg.append("* Please enter the target price in numbers or percentages.\n");
                msg.append("* If you enter 0, it is initialized.\n");
                msg.append("* example)  " + exampleTarget + "  : Target price " + numberFormatter.toMoneyStr(Double.parseDouble(exampleTarget), marketId) + "\n");
                msg.append("* example)  " + exTargetRate + "   : Current Price +" + exTargetRate + "\n");
                msg.append("* example)  -" + exTargetRate + "  : Current Prcie -" + exTargetRate + "\n");
                msg.append("\n");
                msg.append("\n");
                msg.append("# To return to main, enter a character.\n");
                break;
        }

        return msg.toString();
    }

    public String explainTargetDel(Lang lang, Market marketId) {
        StringBuilder msg = new StringBuilder();
        switch (lang) {
            case KR:
                msg.append("삭제할 목표가를 선택해주세요.\n");
                break;
            case EN:
                msg.append("Please select Target you want to delete \n");
                break;
        }

        return msg.toString();
    }

    public String msgCompleteDelTarget(double price, Market marketId, Lang lang) {
        StringBuilder msg = new StringBuilder();
        switch (lang) {
            case KR:
                msg.append(numberFormatter.toMoneyStr(price, marketId) + " 목표가격이 삭제 되었습니다 \n");
                break;
            case EN:
                msg.append(numberFormatter.toMoneyStr(price, marketId) + " Target Deleted\n");
                break;
        }

        return msg.toString();
    }

    public String warningTargetPriceAddPercent(Lang lang) {
        StringBuilder msg = new StringBuilder("");
        switch (lang) {
            case KR:
                msg.append("목표가격 백분율을 -100% 이하로 설정 할 수 없습니다.\n");
                break;
            case EN:
                msg.append("You can not set the target price percentage below -100%.\n");
                break;
        }
        return msg.toString();
    }

    public String warningTargetPriceAddFormat(Lang lang) {
        StringBuilder msg = new StringBuilder("");
        switch (lang) {
            case KR:
                msg.append("목표가격을 숫자 또는 백분율로 입력해주세요.\n");
                break;
            case EN:
                msg.append("Please enter target price as a number or percentage.\n");
                break;
        }
        return msg.toString();
    }

    public String warningTargetPriceDelFormat(Lang lang) {
        StringBuilder msg = new StringBuilder("");
        switch (lang) {
            case KR:
                msg.append("목표가격을 정확히 입력해주세요.\n");
                break;
            case EN:
                msg.append("Please enter correct Target Price.\n");
                break;
        }
        return msg.toString();
    }

    public String warningAlreadyTarget(Lang lang) {
        StringBuilder msg = new StringBuilder("");
        switch (lang) {
            case KR:
                msg.append("이미 설정된 목표가격 입니다.\n");
                break;
            case EN:
                msg.append("Target price already set.\n");
                break;
        }
        return msg.toString();
    }


    public String msgTargetPriceDeleted(Lang lang) {
        StringBuilder msg = new StringBuilder("");
        switch (lang) {
            case KR:
                msg.append("해당 목표가격이 삭제되었습니다.\n");
                break;
            case EN:
                msg.append("The Target been deleted.\n");
                break;
        }
        return msg.toString();
    }

    public String msgTargetPriceSetResult(double TargetPrice, double currentValue, Market marketId, Lang lang) {
        StringBuilder msg = new StringBuilder();
        switch (lang) {
            case KR:
                msg.append("목표가격 " + numberFormatter.toMoneyStr(TargetPrice, marketId) + "으로 설정되었습니다.\n");
                msg.append("------------------------\n");
                msg.append("목표가격 : " + numberFormatter.toMoneyStr(TargetPrice, marketId) + "\n");
                msg.append("현재가격 : " + numberFormatter.toMoneyStr(currentValue, marketId) + "\n");
                msg.append("가격차이 : " + numberFormatter.toSignMoneyStr(TargetPrice - currentValue, marketId) + "(" + numberFormatter.toSignPercentStr(TargetPrice, currentValue) + " )\n");
                break;
            case EN:
                msg.append("The target price is set at " + numberFormatter.toMoneyStr(TargetPrice, marketId) + ".\n");
                msg.append("------------------------\n");
                msg.append("Target Price       : " + numberFormatter.toMoneyStr(TargetPrice, marketId) + "\n");
                msg.append("Current Price      : " + numberFormatter.toMoneyStr(currentValue, marketId) + "\n");
                msg.append("Price difference : " + numberFormatter.toSignMoneyStr(TargetPrice - currentValue, marketId) + " (" + numberFormatter.toSignPercentStr(TargetPrice, currentValue) + " )\n");
                break;
        }
        return msg.toString();
    }

    public String msgTargetPriceNotify(double currentValue, double price, Market marketId, Lang lang) {
        StringBuilder msg = new StringBuilder();
        switch (lang) {
            case KR:
                msg.append("목표가격에 도달하였습니다!\n");
                msg.append("목표가격 : " + numberFormatter.toMoneyStr(price, marketId) + "\n");
                msg.append("현재가격 : " + numberFormatter.toMoneyStr(currentValue, marketId) + "\n");
                break;
            case EN:
                msg.append("Target price reached!\n");
                msg.append("Traget Price : " + numberFormatter.toMoneyStr(price, marketId) + "\n");
                msg.append("Current Price : " + numberFormatter.toMoneyStr(currentValue, marketId) + "\n");
                break;
        }
        return msg.toString();
    }

    /******************************/
    /** Set Daily Notification **/
    /******************************/
    public String explainSetDayloop(Lang lang) {
        StringBuilder msg = new StringBuilder();
        switch (lang) {
            case KR:
                msg.append("일일 알림 주기를 선택해주세요.\n");
                msg.append("선택 하신 일일주기로 알림이 전송됩니다.\n");
                break;
            case EN:
                msg.append("Please select daily notifications cycle.\n");
                msg.append("Coin Price information will be sent according to the cycle.\n");
                break;
        }
        return msg.toString();
    }

    public String msgDayloopSet(int dayloop, Lang lang) {
        StringBuilder msg = new StringBuilder("");
        switch (lang) {
            case KR:
                msg.append("일일 알림이 매 " + dayloop + " 일주기로 전송됩니다.\n");
                break;
            case EN:
                msg.append("Daily notifications are sent every " + dayloop + " days.\n");
                break;
        }
        return msg.toString();
    }

    public String msgDayloopNoSet(Lang lang) {
        StringBuilder msg = new StringBuilder("");
        switch (lang) {
            case KR:
                msg.append("일일 알림 주기가 설정 되지 않았습니다.\n");
                break;
            case EN:
                msg.append("Daily notifications cycle is not set.\n");
                break;
        }
        return msg.toString();
    }

    public String msgDayloopStop(Lang lang) {
        StringBuilder msg = new StringBuilder("");
        switch (lang) {
            case KR:
                msg.append("일일 알림이 전송되지 않습니다.\n");
                break;
            case EN:
                msg.append("Daily notifications are not sent.\n");
                break;
        }
        return msg.toString();
    }


    /*******************************/
    /** Set Hourly Notification **/
    /*******************************/
    public String explainSetTimeloop(Lang lang) {
        StringBuilder msg = new StringBuilder();
        switch (lang) {
            case KR:
                msg.append("시간 알림 주기를 선택해주세요.\n");
                msg.append("선택 하신 시간주기로 알림이 전송됩니다.\n");
                break;
            case EN:
                msg.append("Please select hourly notifications cycle.\n");
                msg.append("Coin Price information will be sent according to the cycle.\n");
                break;
        }
        return msg.toString();
    }

    public String msgTimeloopSet(int timeloop, Lang lang) {
        StringBuilder msg = new StringBuilder("");
        switch (lang) {
            case KR:
                msg.append("시간 알림이 " + timeloop + " 시간 주기로 전송됩니다.\n");
                break;
            case EN:
                msg.append("Houly notifications are sent every " + timeloop + " hours.\n");
                break;
        }
        return msg.toString();
    }

    public String msgTimeloopNoSet(Lang lang) {
        StringBuilder msg = new StringBuilder("");
        switch (lang) {
            case KR:
                msg.append("시간알림 주기가 설정 되지 않았습니다.\n");
                break;
            case EN:
                msg.append("Hourly notifications cycle is not set.\n");
                break;
        }
        return msg.toString();
    }


    public String msgTimeloopStop(Lang lang) {
        StringBuilder msg = new StringBuilder("");
        switch (lang) {
            case KR:
                msg.append("시간 알림이 전송되지 않습니다.\n");
                break;
            case EN:
                msg.append("Hourly notifications are not sent.\n");
                break;
        }
        return msg.toString();
    }

    /*********************/
    /** Show setting *****/
    /*********************/


    /****************************/
    /** Stop all notifications **/
    /****************************/
    public String explainStop(Lang lang) {
        StringBuilder msg = new StringBuilder();
        switch (lang) {
            case KR:
                msg.append("모든 알림(일일알림, 시간알림, 목표가알림)을 중지하시겠습니까?\n");
                msg.append("\n");
                msg.append("★ 필독!\n");
                msg.append("1. 모든알림이 중지되더라도 공지사항은 전송됩니다.\n");
                msg.append("2. 모든알림이 중지되더라도 버튼을 통해 코인관련정보를 받을 수 있습니다.\n");
                msg.append("3. 서비스를 완전히 중지하시려면 대화방을 삭제해주세요!\n");
                break;
            case EN:
                msg.append("Are you sure you want to stop all notifications (daily, hourly , target price notifications )?\n");
                msg.append("\n");
                msg.append("★  Must read!\n");
                msg.append("1. Even if all notifications have been stopped, you will continue to receive notification of service usage.\n");
                msg.append("2. Even if all notifications have been stopped, you received coin information using menu.\n");
                msg.append("3. If you want to stop completry this service, Please block bot.\n");
                break;
        }

        return msg.toString();
    }

    public String msgStopAllNotice(Lang lang) {
        StringBuilder msg = new StringBuilder("");
        switch (lang) {
            case KR:
                msg.append(myCoinId + " 모든알림(시간알림, 일일알림, 목표가격알림)이 중지되었습니다.\n");
                break;
            case EN:
                msg.append("All notifications (daily, hourly , target price notifications ) be stoped.\n");
                break;
        }
        return msg.toString();
    }

    /***********************/
    /** Explain Coin List **/
    /***********************/
    public String explainCoinList(List<CoinInfoDto> coinInfos, Lang lang) {
        StringBuilder msg = new StringBuilder();
        CoinInfoDto coinInfo = null;
        int coinInfosLen = coinInfos.size();

        switch (lang) {
            case KR:
                msg.append("링크를 클릭 하시면,\n");
                msg.append("해당 코인알리미 봇으로 이동합니다.\n");

                msg.append("-----------------------\n");
                for (int i = 0; i < coinInfosLen; i++) {
                    coinInfo = coinInfos.get(i);
                    msg.append(coinInfo.getCoinId() + " [" + coinInfo.getCoinId().kr() + "] \n");
                    msg.append(coinInfo.getChatAddr() + "\n");
                    msg.append("\n");
                }
                msg.append("\n");
                break;
            case EN:
                msg.append("Click on the link to go to other Coin Noticer.\n");
                msg.append("-----------------------\n");
                for (int i = 0; i < coinInfosLen; i++) {
                    coinInfo = coinInfos.get(i);
                    msg.append(coinInfo.getCoinId() + " [" + coinInfo.getCoinId().en() + "] \n");
                    msg.append(coinInfo.getChatAddr() + "\n");
                    msg.append("\n");
                }
                msg.append("\n");
                break;
        }


        return msg.toString();
    }

    /***********/
    /** Help  **/
    /***********/


    /*******************************/
    /** Send message to developer **/
    /*******************************/
    public String explainSendSuggest(Lang lang) {
        StringBuilder msg = new StringBuilder();
        switch (lang) {
            case KR:
                msg.append("개발자에게 내용이 전송되어집니다.\n");
                msg.append("내용을 입력해주세요.\n");
                msg.append("\n");
                msg.append("\n");
                msg.append("# 메인으로 돌아가시려면 " + SendMessageCommand.OUT.getCommand(lang) + " 를 입력해주세요.\n");
                break;
            case EN:
                msg.append("Please enter message.\n");
                msg.append("A message is sent to the developer.\n");
                msg.append("\n");
                msg.append("\n");
                msg.append("# To return to main, enter " + SendMessageCommand.OUT.getCommand(lang) + "\n");
                break;
        }
        return msg.toString();
    }

    public String msgThankyouSuggest(Lang lang) {
        StringBuilder msg = new StringBuilder();
        switch (lang) {
            case KR:
                msg.append("의견 감사드립니다.\n");
                msg.append("성투하세요^^!\n");
                break;
            case EN:
                msg.append("Thank you for your suggest.\n");
                msg.append("You will succeed in your investment :)!\n");
                break;
        }
        return msg.toString();
    }


    /***************************/
    /*** Sponsoring Message ****/
    /***************************/
    public String explainSupport(Lang lang) {
        StringBuilder msg = new StringBuilder();
        String url = "https://toon.at/donate/dev-cglee";
        switch (lang) {
            case KR:
                msg.append("안녕하세요. 개발자 CGLEE 입니다.\n");
                msg.append("본 서비스는 무료 서비스 임을 다시 한번 알려드리며,\n");
                msg.append("절대로! 후원하지 않는다하여 사용자 여러분에게 불이익을 제공하지 않습니다.^^\n");
                msg.append("\n");
                msg.append("<a href='");
                msg.append(url);
                msg.append("'>★ 후원하러가기!</a>");
                msg.append("\n");
                msg.append("\n");
                msg.append("감사합니다.\n");
                break;

            case EN:
                msg.append("Hi. I'm developer CGLEE\n");
                msg.append("Never! I don't offer disadvantages to users by not sponsoring. :D\n");
                msg.append("\n");
                msg.append("<a href='");
                msg.append(url);
                msg.append("'>★ Go to Sponsoring!</a>");
                msg.append("\n");
                msg.append("\n");
                msg.append("Thank you for sponsoring.\n");
                break;
        }
        return msg.toString();
    }


    /*********************/
    /*** Language Set  ***/
    /*********************/
    public String explainSetLanguage(Lang lang) {
        return "Please select a language.";
    }

    public String msgSetLanguageSuccess(Lang lang) {
        StringBuilder msg = new StringBuilder("");
        switch (lang) {
            case KR:
                msg.append("언어가 한국어로 변경되었습니다.\n");
                break;
            case EN:
                msg.append("Language changed to English.\n");
                break;
        }
        return msg.toString();
    }


    /*********************/
    /** Time Adjust  *****/
    /*********************/
    public String explainTimeAdjust(Lang lang) {
        StringBuilder msg = new StringBuilder();
        msg.append("한국분이시라면 별도의 시차조절을 필요로하지 않습니다.^^  <- for korean\n");
        msg.append("\n");
        msg.append("Please enter the current time for accurate time notification.\n");
        msg.append("because the time differs for each country.\n");
        msg.append("\n");
        msg.append("* Please enter in the following format.\n");
        msg.append("* if you entered 0, time adjust initialized.\n");
        msg.append("* example) 0 : init time adjust\n");
        msg.append("* example) " + TimeStamper.getDateBefore() + " 23:00 \n");
        msg.append("* example) " + TimeStamper.getDate() + " 00:33 \n");
        msg.append("* example) " + TimeStamper.getDate() + " 14:30 \n");
        msg.append("\n");
        msg.append("\n");
        msg.append("# To return to main, enter a character.\n");
        return msg.toString();
    }

    public String warningTimeAdjustFormat(Lang lang) {
        return "Please type according to the format.\n";
    }

    public String msgTimeAdjustSuccess(Date date) {
        StringBuilder msg = new StringBuilder();
        msg.append("Time adjustment succeeded.\n");
        msg.append("Current Time : " + TimeStamper.getDateTime(date) + "\n");
        return msg.toString();
    }

//	public String msgToPreference() {
//		StringBuilder msg = new StringBuilder("");
//		msg.append("\n# Changed to Preference menu\n");
//		return msg.toString();
//	}

    /**********************************/
    /** Daily Notification Message ***/
    /**********************************/
    public String msgSendDailyMessage(UserDto client, TimelyInfoDto coinCurrent, TimelyInfoDto coinBefore, JSONObject coinCurrentMoney, JSONObject coinBeforeMoney) {
        long currentVolume = coinCurrent.getVolume();
        long beforeVolume = coinBefore.getVolume();
        double currentHigh = coinCurrent.getHigh();
        double beforeHigh = coinBefore.getHigh();
        double currentLow = coinCurrent.getLow();
        double beforeLow = coinBefore.getLow();
        double currentLast = coinCurrent.getLast();
        double beforeLast = coinBefore.getLast();
        double currentHighBTC = 0;
        double beforeHighBTC = 0;
        double currentLowBTC = 0;
        double beforeLowBTC = 0;
        double currentLastBTC = 0;
        double beforeLastBTC = 0;

        Market marketId = client.getMarket();
        Lang lang = client.getLang();
        long localTime = client.getLocaltime();
        String date = TimeStamper.getDateTime(localTime);
        int dayLoop = client.getDayloopAlert();

        StringBuilder msg = new StringBuilder();
        msg.append(date + "\n");

        switch (lang) {
            case KR:
                String dayLoopStr = "";
                switch (dayLoop) {
                    case 1:
                        dayLoopStr = "하루";
                        break;
                    case 2:
                        dayLoopStr = "이틀";
                        break;
                    case 3:
                        dayLoopStr = "삼일";
                        break;
                    case 4:
                        dayLoopStr = "사일";
                        break;
                    case 5:
                        dayLoopStr = "오일";
                        break;
                    case 6:
                        dayLoopStr = "육일";
                        break;
                    case 7:
                        dayLoopStr = "한주";
                        break;
                }

                if (inBtcs.get(marketId)) {
                    currentHighBTC = currentHigh;
                    beforeHighBTC = beforeHigh;
                    currentLowBTC = currentLow;
                    beforeLowBTC = beforeLow;
                    currentLastBTC = currentLast;
                    beforeLastBTC = beforeLast;

                    currentLast = coinCurrentMoney.getDouble("last");
                    currentHigh = coinCurrentMoney.getDouble("high");
                    currentLow = coinCurrentMoney.getDouble("low");
                    beforeLast = coinBeforeMoney.getDouble("last");
                    beforeHigh = coinBeforeMoney.getDouble("high");
                    beforeLow = coinBeforeMoney.getDouble("low");

                    msg.append("---------------------\n");
                    msg.append("금일의 거래량 : " + numberFormatter.toVolumeStr(currentVolume) + " \n");
                    msg.append(dayLoopStr + "전 거래량 : " + numberFormatter.toVolumeStr(beforeVolume) + " \n");
                    msg.append("거래량의 차이 : " + numberFormatter.toSignVolumeStr(currentVolume - beforeVolume) + " (" + numberFormatter.toSignPercentStr(currentVolume, beforeVolume) + ")\n");
                    msg.append("\n");

                    msg.append("금일의 상한가 : " + numberFormatter.toMoneyStr(currentHigh, marketId) + " [" + numberFormatter.toBTCStr(currentHighBTC) + "]\n");
                    msg.append(dayLoopStr + "전 상한가 : " + numberFormatter.toMoneyStr(beforeHigh, marketId) + " [" + numberFormatter.toBTCStr(beforeHighBTC) + "]\n");
                    msg.append("상한가의 차이 : " + numberFormatter.toSignMoneyStr(currentHigh - beforeHigh, marketId) + " (" + numberFormatter.toSignPercentStr(currentHigh, beforeHigh) + ")\n");
                    msg.append("\n");

                    msg.append("금일의 하한가 : " + numberFormatter.toMoneyStr(currentLow, marketId) + " [" + numberFormatter.toBTCStr(currentLowBTC) + "]\n");
                    msg.append(dayLoopStr + "전 하한가 : " + numberFormatter.toMoneyStr(beforeLow, marketId) + " [" + numberFormatter.toBTCStr(beforeLowBTC) + "]\n");
                    msg.append("하한가의 차이 : " + numberFormatter.toSignMoneyStr(currentLow - beforeLow, marketId) + " (" + numberFormatter.toSignPercentStr(currentLow, beforeLow) + ")\n");
                    msg.append("\n");

                    msg.append("금일의 종가 : " + numberFormatter.toMoneyStr(currentLast, marketId) + " [" + numberFormatter.toBTCStr(currentLastBTC) + "]\n");
                    msg.append(dayLoopStr + "전 종가 : " + numberFormatter.toMoneyStr(beforeLast, marketId) + " [" + numberFormatter.toBTCStr(beforeLastBTC) + "]\n");
                    msg.append("종가의 차이 : " + numberFormatter.toSignMoneyStr(currentLast - beforeLast, marketId) + " (" + numberFormatter.toSignPercentStr(currentLast, beforeLast) + ")\n");
                    msg.append("\n");
                } else {
                    msg.append("---------------------\n");
                    msg.append("금일의 거래량 : " + numberFormatter.toVolumeStr(currentVolume) + " \n");
                    msg.append(dayLoopStr + "전 거래량 : " + numberFormatter.toVolumeStr(beforeVolume) + " \n");
                    msg.append("거래량의 차이 : " + numberFormatter.toSignVolumeStr(currentVolume - beforeVolume) + " (" + numberFormatter.toSignPercentStr(currentVolume, beforeVolume) + ")\n");
                    msg.append("\n");

                    msg.append("금일의 상한가 : " + numberFormatter.toMoneyStr(currentHigh, marketId) + "\n");
                    msg.append(dayLoopStr + "전 상한가 : " + numberFormatter.toMoneyStr(beforeHigh, marketId) + "\n");
                    msg.append("상한가의 차이 : " + numberFormatter.toSignMoneyStr(currentHigh - beforeHigh, marketId) + " (" + numberFormatter.toSignPercentStr(currentHigh, beforeHigh) + ")\n");
                    msg.append("\n");

                    msg.append("금일의 하한가 : " + numberFormatter.toMoneyStr(currentLow, marketId) + "\n");
                    msg.append(dayLoopStr + "전 하한가 : " + numberFormatter.toMoneyStr(beforeLow, marketId) + "\n");
                    msg.append("하한가의 차이 : " + numberFormatter.toSignMoneyStr(currentLow - beforeLow, marketId) + " (" + numberFormatter.toSignPercentStr(currentLow, beforeLow) + ")\n");
                    msg.append("\n");


                    msg.append("금일의 종가 : " + numberFormatter.toMoneyStr(currentLast, marketId) + "\n");
                    msg.append(dayLoopStr + "전 종가 : " + numberFormatter.toMoneyStr(beforeLast, marketId) + "\n");
                    msg.append("종가의 차이 : " + numberFormatter.toSignMoneyStr(currentLast - beforeLast, marketId) + " (" + numberFormatter.toSignPercentStr(currentLast, beforeLast) + ")\n");
                    msg.append("\n");
                }
                break;

            case EN:
                if (inBtcs.get(marketId)) {
                    currentHighBTC = currentHigh;
                    beforeHighBTC = beforeHigh;
                    currentLowBTC = currentLow;
                    beforeLowBTC = beforeLow;
                    currentLastBTC = currentLast;
                    beforeLastBTC = beforeLast;

                    currentLast = coinCurrentMoney.getDouble("last");
                    currentHigh = coinCurrentMoney.getDouble("high");
                    currentLow = coinCurrentMoney.getDouble("low");
                    beforeLast = coinBeforeMoney.getDouble("last");
                    beforeHigh = coinBeforeMoney.getDouble("high");
                    beforeLow = coinBeforeMoney.getDouble("low");

                    msg.append("---------------------\n");
                    msg.append("Volume at today : " + numberFormatter.toVolumeStr(currentVolume) + " \n");
                    msg.append("Volume before " + dayLoop + " day : " + numberFormatter.toVolumeStr(beforeVolume) + " \n");
                    msg.append("Volume difference : " + numberFormatter.toSignVolumeStr(currentVolume - beforeVolume) + " (" + numberFormatter.toSignPercentStr(currentVolume, beforeVolume) + ")\n");
                    msg.append("\n");
                    msg.append("High at Today : " + numberFormatter.toMoneyStr(currentHigh, marketId) + " [" + numberFormatter.toBTCStr(currentHighBTC) + "]\n");
                    msg.append("High before " + dayLoop + " day : " + numberFormatter.toMoneyStr(beforeHigh, marketId) + " [" + numberFormatter.toBTCStr(beforeHighBTC) + "]\n");
                    msg.append("High difference : " + numberFormatter.toSignMoneyStr(currentHigh - beforeHigh, marketId) + " (" + numberFormatter.toSignPercentStr(currentHigh, beforeHigh) + ")\n");
                    msg.append("\n");
                    msg.append("Low at Today : " + numberFormatter.toMoneyStr(currentLow, marketId) + " [" + numberFormatter.toBTCStr(currentLowBTC) + "]\n");
                    msg.append("Low before " + dayLoop + " day : " + numberFormatter.toMoneyStr(beforeLow, marketId) + " [" + numberFormatter.toBTCStr(beforeLowBTC) + "]\n");
                    msg.append("Low difference : " + numberFormatter.toSignMoneyStr(currentLow - beforeLow, marketId) + " (" + numberFormatter.toSignPercentStr(currentLow, beforeLow) + ")\n");
                    msg.append("\n");
                    msg.append("Last at Today : " + numberFormatter.toMoneyStr(currentLast, marketId) + " [" + numberFormatter.toBTCStr(currentLastBTC) + "]\n");
                    msg.append("Last before " + dayLoop + " day : " + numberFormatter.toMoneyStr(beforeLast, marketId) + " [" + numberFormatter.toBTCStr(beforeLastBTC) + "]\n");
                    msg.append("Last difference : " + numberFormatter.toSignMoneyStr(currentLast - beforeLast, marketId) + " (" + numberFormatter.toSignPercentStr(currentLast, beforeLast) + ")\n");
                    msg.append("\n");
                } else {
                    msg.append("---------------------\n");
                    msg.append("Volume at today : " + numberFormatter.toVolumeStr(currentVolume) + " \n");
                    msg.append("Volume before " + dayLoop + " day : " + numberFormatter.toVolumeStr(beforeVolume) + " \n");
                    msg.append("Volume difference : " + numberFormatter.toSignVolumeStr(currentVolume - beforeVolume) + " (" + numberFormatter.toSignPercentStr(currentVolume, beforeVolume) + ")\n");
                    msg.append("\n");
                    msg.append("High at Today : " + numberFormatter.toMoneyStr(currentHigh, marketId) + "\n");
                    msg.append("High before " + dayLoop + " day : " + numberFormatter.toMoneyStr(beforeHigh, marketId) + "\n");
                    msg.append("High difference : " + numberFormatter.toSignMoneyStr(currentHigh - beforeHigh, marketId) + " (" + numberFormatter.toSignPercentStr(currentHigh, beforeHigh) + ")\n");
                    msg.append("\n");
                    msg.append("Low at Today : " + numberFormatter.toMoneyStr(currentLow, marketId) + "\n");
                    msg.append("Low before " + dayLoop + " day : " + numberFormatter.toMoneyStr(beforeLow, marketId) + "\n");
                    msg.append("Low difference : " + numberFormatter.toSignMoneyStr(currentLow - beforeLow, marketId) + " (" + numberFormatter.toSignPercentStr(currentLow, beforeLow) + ")\n");
                    msg.append("\n");
                    msg.append("Last at Today : " + numberFormatter.toMoneyStr(currentLast, marketId) + "\n");
                    msg.append("Last before " + dayLoop + " day : " + numberFormatter.toMoneyStr(beforeLast, marketId) + "\n");
                    msg.append("Last difference : " + numberFormatter.toSignMoneyStr(currentLast - beforeLast, marketId) + " (" + numberFormatter.toSignPercentStr(currentLast, beforeLast) + ")\n");
                    msg.append("\n");
                }
                break;
        }

        return msg.toString();
    }

    /**********************************/
    /** Timely Notification Message ***/
    /**********************************/
    public String msgSendTimelyMessage(UserDto client, TimelyInfoDto coinCurrent, TimelyInfoDto coinBefore, JSONObject coinCurrentMoney, JSONObject coinBeforeMoney) {
        StringBuilder msg = new StringBuilder();
        Market marketId = client.getMarket();
        Lang lang = client.getLang();
        int timeLoop = client.getTimeloopAlert();
        long localTime = client.getLocaltime();
        String date = TimeStamper.getDateTime(localTime);

        double currentValue = coinCurrent.getLast();
        double beforeValue = coinBefore.getLast();

        switch (lang) {
            case KR:
                msg.append("현재시각: " + date + "\n");
                if (!coinCurrent.getResult().equals("success")) {
                    String currentErrorMsg = coinCurrent.getErrorMsg();
                    String currentErrorCode = coinCurrent.getErrorCode();
                    msg.append("에러발생: " + currentErrorMsg + "\n");
                    msg.append("에러코드: " + currentErrorCode + "\n");

                    if (inBtcs.get(marketId)) {
                        double beforeBTC = beforeValue;
                        double beforeMoney = coinBeforeMoney.getDouble("last");

                        msg.append(timeLoop + " 시간 전: " + numberFormatter.toMoneyStr(beforeMoney, marketId) + " 원 [" + numberFormatter.toBTCStr(beforeBTC) + " BTC]\n");
                    } else {
                        msg.append(timeLoop + " 시간 전: " + numberFormatter.toMoneyStr(beforeValue, marketId) + " 원\n");
                    }
                } else {
                    if (inBtcs.get(marketId)) {
                        double currentBTC = currentValue;
                        double beforeBTC = beforeValue;
                        double currentMoney = coinCurrentMoney.getDouble("last");
                        double beforeMoney = coinBeforeMoney.getDouble("last");

                        msg.append("현재가격: " + numberFormatter.toMoneyStr(currentMoney, marketId) + " [" + numberFormatter.toBTCStr(currentBTC) + "]\n");
                        msg.append(timeLoop + " 시간 전: " + numberFormatter.toMoneyStr(beforeMoney, marketId) + " [" + numberFormatter.toBTCStr(beforeBTC) + "]\n");
                        msg.append("가격차이: " + numberFormatter.toSignMoneyStr(currentMoney - beforeMoney, marketId) + " (" + numberFormatter.toSignPercentStr(currentMoney, beforeMoney) + ")\n");
                    } else {
                        msg.append("현재가격: " + numberFormatter.toMoneyStr(currentValue, marketId) + "\n");
                        msg.append(timeLoop + " 시간 전: " + numberFormatter.toMoneyStr(beforeValue, marketId) + "\n");
                        msg.append("가격차이: " + numberFormatter.toSignMoneyStr(currentValue - beforeValue, marketId) + " (" + numberFormatter.toSignPercentStr(currentValue, beforeValue) + ")\n");
                    }
                }
                break;

            case EN:
                msg.append("Current Time: " + date + "\n");
                if (!coinCurrent.getResult().equals("success")) {
                    String currentErrorMsg = coinCurrent.getErrorMsg();
                    String currentErrorCode = coinCurrent.getErrorCode();
                    msg.append("Error Msg : " + currentErrorMsg + "\n");
                    msg.append("Error Code: " + currentErrorCode + "\n");

                    if (inBtcs.get(marketId)) {
                        double beforeBTC = beforeValue;
                        double beforeMoney = coinBeforeMoney.getDouble("last");

                        msg.append("Coin Price before " + timeLoop + " hour : " + numberFormatter.toMoneyStr(beforeMoney, marketId) + " [" + numberFormatter.toBTCStr(beforeBTC) + "]\n");
                    } else {
                        msg.append("Coin Price before " + timeLoop + " hour : " + numberFormatter.toMoneyStr(beforeValue, marketId) + "\n");
                    }
                } else {
                    if (inBtcs.get(marketId)) {
                        double currentBTC = currentValue;
                        double beforeBTC = beforeValue;
                        double currentMoney = coinCurrentMoney.getDouble("last");
                        double beforeMoney = coinBeforeMoney.getDouble("last");

                        msg.append("Coin Price at Current Time : " + numberFormatter.toMoneyStr(currentMoney, marketId) + " [" + numberFormatter.toBTCStr(currentBTC) + "]\n");
                        msg.append("Coin Price before " + timeLoop + " hour : " + numberFormatter.toMoneyStr(beforeMoney, marketId) + " [" + numberFormatter.toBTCStr(beforeBTC) + "]\n");
                        msg.append("Coin Price Difference : " + numberFormatter.toSignMoneyStr(currentMoney - beforeMoney, marketId) + " (" + numberFormatter.toSignPercentStr(currentMoney, beforeMoney) + ")\n");
                    } else {
                        msg.append("Coin Price at Current Time : " + numberFormatter.toMoneyStr(currentValue, marketId) + "\n");
                        msg.append("Coin Price before " + timeLoop + " hour : " + numberFormatter.toMoneyStr(beforeValue, marketId) + "\n");
                        msg.append("Coin Price Difference : " + numberFormatter.toSignMoneyStr(currentValue - beforeValue, marketId) + " (" + numberFormatter.toSignPercentStr(currentValue, beforeValue) + ")\n");
                    }
                }
                break;
        }

        return msg.toString();
    }


}
