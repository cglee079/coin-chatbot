//package com.podo.coinchatbot.telegram;
//
//import com.podo.coinchatbot.core.Coin;
//import com.podo.coinchatbot.core.Lang;
//import com.podo.coinchatbot.core.Market;
//import com.podo.coinchatbot.telegram.domain.model.UserDto;
//import com.podo.coinchatbot.telegram.model.CoinConfigDto;
//import com.podo.coinchatbot.telegram.domain.model.TimelyInfoDto;
//import com.podo.coinchatbot.telegram.app.CoinFormatter;
//import com.podo.coinchatbot.telegram.util.TimeStamper;
//import org.json.JSONObject;
//
//import java.util.HashMap;
//
//public class MessageMaker {
//    private Coin myCoinId;
//    private CoinFormatter coinFormatter;
//    private String version;
//    private String exInvestKR;
//    private String exInvestUS;
//    private String exCoinCnt;
//    private String exTargetKR;
//    private String exTargetUS;
//    private String exTargetRate;
//    private HashMap<Market, Boolean> inBtcs;
//
//    public MessageMaker(Coin myCoinId, CoinConfigDto config, CoinFormatter nts, HashMap<Market, Boolean> inBtcs) {
//        this.myCoinId = myCoinId;
//        this.inBtcs = inBtcs;
//        version = config.getVersion();
//        exInvestKR = config.getExInvestKRW();
//        exInvestUS = config.getExInvestUSD();
//        exCoinCnt = config.getExCoinCnt();
//        exTargetKR = config.getExTargetKRW();
//        exTargetUS = config.getExTargetUSD();
//        exTargetRate = config.getExTargetRate();
//
//        this.coinFormatter = nts;
//
//    }
//
//
//    /********************/
//    /** Common Message **/
//    /********************/
//    public String warningNeedToStart(Lang lang) {
//        StringBuilder msg = new StringBuilder("");
//        switch (lang) {
//            case KR:
//                msg.append("알림을 먼저 시작해주세요. \n 명령어 /start  <<< 클릭!.\n");
//                break;
//            case EN:
//                msg.append("Please start this service first.\n /start <<< click here.\n");
//                break;
//        }
//        return msg.toString();
//    }
//
//
//
//
//
//
//    /***************************/
//    /** Happy Line Message *****/
//    /***************************/
//    public String warningHappyLineFormat(Lang lang) {
//        StringBuilder msg = new StringBuilder("");
//        switch (lang) {
//            case KR:
//                msg.append("코인개수는 숫자로만 입력해주세요.\n");
//                break;
//            case EN:
//                msg.append("Please enter the number of coins only in numbers.\n");
//                break;
//        }
//        return msg.toString();
//    }
//
//
//
//
//    /*********************************/
//    /** Set investment Price Message**/
//    /********************************/
//
//
//    public String warningPriceFormat(Lang lang) {
//        StringBuilder msg = new StringBuilder("");
//        switch (lang) {
//            case KR:
//                msg.append("투자금액은 숫자로만 입력해주세요.\n");
//                break;
//            case EN:
//                msg.append("Please enter the investment amount only in numbers.\n");
//                break;
//        }
//        return msg.toString();
//    }
//
//
//    /***************************/
//    /** Set Coin Count Message**/
//    /***************************/
//
//
//    /************************/
//    /** Set Market Message **/
//    /************************/
//
//
//
//
//
//
//
//    /*****************************/
//    /**** Target Price Message ***/
//
//
//
//
//
//
//
//
//    public String warningTargetPriceAddFormat(Lang lang) {
//        StringBuilder msg = new StringBuilder("");
//        switch (lang) {
//            case KR:
//                msg.append("목표가격을 숫자 또는 백분율로 입력해주세요.\n");
//                break;
//            case EN:
//                msg.append("Please enter target price as a number or percentage.\n");
//                break;
//        }
//        return msg.toString();
//    }
//
//    public String warningTargetPriceDelFormat(Lang lang) {
//        StringBuilder msg = new StringBuilder("");
//        switch (lang) {
//            case KR:
//                msg.append("목표가격을 정확히 입력해주세요.\n");
//                break;
//            case EN:
//                msg.append("Please enter correct Target Price.\n");
//                break;
//        }
//        return msg.toString();
//    }
//
//    public String warningAlreadyTarget(Lang lang) {
//        StringBuilder msg = new StringBuilder("");
//        switch (lang) {
//            case KR:
//                msg.append("이미 설정된 목표가격 입니다.\n");
//                break;
//            case EN:
//                msg.append("Target price already set.\n");
//                break;
//        }
//        return msg.toString();
//    }
//
//
//    public String msgTargetPriceDeleted(Lang lang) {
//        StringBuilder msg = new StringBuilder("");
//        switch (lang) {
//            case KR:
//                msg.append("해당 목표가격이 삭제되었습니다.\n");
//                break;
//            case EN:
//                msg.append("The Target been deleted.\n");
//                break;
//        }
//        return msg.toString();
//    }
//
//
//
//    public String msgTargetPriceNotify(double currentValue, double price, Market marketId, Lang lang) {
//        StringBuilder msg = new StringBuilder();
//        switch (lang) {
//            case KR:
//                msg.append("목표가격에 도달하였습니다!\n");
//                msg.append("목표가격 : " + coinFormatter.toMoneyStr(price, marketId) + "\n");
//                msg.append("현재가격 : " + coinFormatter.toMoneyStr(currentValue, marketId) + "\n");
//                break;
//            case EN:
//                msg.append("Target price reached!\n");
//                msg.append("Traget Price : " + coinFormatter.toMoneyStr(price, marketId) + "\n");
//                msg.append("Current Price : " + coinFormatter.toMoneyStr(currentValue, marketId) + "\n");
//                break;
//        }
//        return msg.toString();
//    }
//
//    /******************************/
//    /** Set Daily Notification **/
//    /******************************/
//
//
//
//
//
//
//
//
//
//
//
//
//    /***********************/
//    /** Explain Coin List **/
//    /***********************/
//
//
//
//
//    /***************************/
//    /*** Sponsoring Message ****/
//    /***************************/
//
//
//
//    /*********************/
//    /*** Language Set  ***/
//    /*********************/
//
//
//
//    /*********************/
//    /** Time Adjust  *****/
//    /*********************/
//
//
//    public String warningTimeAdjustFormat(Lang lang) {
//        return "Please type according to the format.\n";
//    }
//
//
////	public String msgToPreference() {
////		StringBuilder msg = new StringBuilder("");
////		msg.append("\n# Changed to Preference menu\n");
////		return msg.toString();
////	}
//
//    /**********************************/
//    /** Daily Notification Message ***/
//    /**********************************/
//    public String msgSendDailyMessage(UserDto client, TimelyInfoDto coinCurrent, TimelyInfoDto coinBefore, JSONObject coinCurrentMoney, JSONObject coinBeforeMoney) {
//        long currentVolume = coinCurrent.getVolume();
//        long beforeVolume = coinBefore.getVolume();
//        double currentHigh = coinCurrent.getHigh();
//        double beforeHigh = coinBefore.getHigh();
//        double currentLow = coinCurrent.getLow();
//        double beforeLow = coinBefore.getLow();
//        double currentLast = coinCurrent.getLast();
//        double beforeLast = coinBefore.getLast();
//        double currentHighBTC = 0;
//        double beforeHighBTC = 0;
//        double currentLowBTC = 0;
//        double beforeLowBTC = 0;
//        double currentLastBTC = 0;
//        double beforeLastBTC = 0;
//
//        Market marketId = client.getMarket();
//        Lang lang = client.getLang();
//        long localTime = client.getLocaltime();
//        String date = TimeStamper.toDateString(localTime);
//        int dayLoop = client.getDayloopAlert();
//
//        StringBuilder msg = new StringBuilder();
//        msg.append(date + "\n");
//
//        switch (lang) {
//            case KR:
//                String dayLoopStr = "";
//                switch (dayLoop) {
//                    case 1:
//                        dayLoopStr = "하루";
//                        break;
//                    case 2:
//                        dayLoopStr = "이틀";
//                        break;
//                    case 3:
//                        dayLoopStr = "삼일";
//                        break;
//                    case 4:
//                        dayLoopStr = "사일";
//                        break;
//                    case 5:
//                        dayLoopStr = "오일";
//                        break;
//                    case 6:
//                        dayLoopStr = "육일";
//                        break;
//                    case 7:
//                        dayLoopStr = "한주";
//                        break;
//                }
//
//                if (inBtcs.get(marketId)) {
//                    currentHighBTC = currentHigh;
//                    beforeHighBTC = beforeHigh;
//                    currentLowBTC = currentLow;
//                    beforeLowBTC = beforeLow;
//                    currentLastBTC = currentLast;
//                    beforeLastBTC = beforeLast;
//
//                    currentLast = coinCurrentMoney.getDouble("last");
//                    currentHigh = coinCurrentMoney.getDouble("high");
//                    currentLow = coinCurrentMoney.getDouble("low");
//                    beforeLast = coinBeforeMoney.getDouble("last");
//                    beforeHigh = coinBeforeMoney.getDouble("high");
//                    beforeLow = coinBeforeMoney.getDouble("low");
//
//                    msg.append("---------------------\n");
//                    msg.append("금일의 거래량 : " + coinFormatter.toVolumeStr(currentVolume) + " \n");
//                    msg.append(dayLoopStr + "전 거래량 : " + coinFormatter.toVolumeStr(beforeVolume) + " \n");
//                    msg.append("거래량의 차이 : " + coinFormatter.toSignVolumeStr(currentVolume - beforeVolume) + " (" + coinFormatter.toSignPercentStr(currentVolume, beforeVolume) + ")\n");
//                    msg.append("\n");
//
//                    msg.append("금일의 상한가 : " + coinFormatter.toMoneyStr(currentHigh, marketId) + " [" + coinFormatter.toBTCStr(currentHighBTC) + "]\n");
//                    msg.append(dayLoopStr + "전 상한가 : " + coinFormatter.toMoneyStr(beforeHigh, marketId) + " [" + coinFormatter.toBTCStr(beforeHighBTC) + "]\n");
//                    msg.append("상한가의 차이 : " + coinFormatter.toSignMoneyStr(currentHigh - beforeHigh, marketId) + " (" + coinFormatter.toSignPercentStr(currentHigh, beforeHigh) + ")\n");
//                    msg.append("\n");
//
//                    msg.append("금일의 하한가 : " + coinFormatter.toMoneyStr(currentLow, marketId) + " [" + coinFormatter.toBTCStr(currentLowBTC) + "]\n");
//                    msg.append(dayLoopStr + "전 하한가 : " + coinFormatter.toMoneyStr(beforeLow, marketId) + " [" + coinFormatter.toBTCStr(beforeLowBTC) + "]\n");
//                    msg.append("하한가의 차이 : " + coinFormatter.toSignMoneyStr(currentLow - beforeLow, marketId) + " (" + coinFormatter.toSignPercentStr(currentLow, beforeLow) + ")\n");
//                    msg.append("\n");
//
//                    msg.append("금일의 종가 : " + coinFormatter.toMoneyStr(currentLast, marketId) + " [" + coinFormatter.toBTCStr(currentLastBTC) + "]\n");
//                    msg.append(dayLoopStr + "전 종가 : " + coinFormatter.toMoneyStr(beforeLast, marketId) + " [" + coinFormatter.toBTCStr(beforeLastBTC) + "]\n");
//                    msg.append("종가의 차이 : " + coinFormatter.toSignMoneyStr(currentLast - beforeLast, marketId) + " (" + coinFormatter.toSignPercentStr(currentLast, beforeLast) + ")\n");
//                    msg.append("\n");
//                } else {
//                    msg.append("---------------------\n");
//                    msg.append("금일의 거래량 : " + coinFormatter.toVolumeStr(currentVolume) + " \n");
//                    msg.append(dayLoopStr + "전 거래량 : " + coinFormatter.toVolumeStr(beforeVolume) + " \n");
//                    msg.append("거래량의 차이 : " + coinFormatter.toSignVolumeStr(currentVolume - beforeVolume) + " (" + coinFormatter.toSignPercentStr(currentVolume, beforeVolume) + ")\n");
//                    msg.append("\n");
//
//                    msg.append("금일의 상한가 : " + coinFormatter.toMoneyStr(currentHigh, marketId) + "\n");
//                    msg.append(dayLoopStr + "전 상한가 : " + coinFormatter.toMoneyStr(beforeHigh, marketId) + "\n");
//                    msg.append("상한가의 차이 : " + coinFormatter.toSignMoneyStr(currentHigh - beforeHigh, marketId) + " (" + coinFormatter.toSignPercentStr(currentHigh, beforeHigh) + ")\n");
//                    msg.append("\n");
//
//                    msg.append("금일의 하한가 : " + coinFormatter.toMoneyStr(currentLow, marketId) + "\n");
//                    msg.append(dayLoopStr + "전 하한가 : " + coinFormatter.toMoneyStr(beforeLow, marketId) + "\n");
//                    msg.append("하한가의 차이 : " + coinFormatter.toSignMoneyStr(currentLow - beforeLow, marketId) + " (" + coinFormatter.toSignPercentStr(currentLow, beforeLow) + ")\n");
//                    msg.append("\n");
//
//
//                    msg.append("금일의 종가 : " + coinFormatter.toMoneyStr(currentLast, marketId) + "\n");
//                    msg.append(dayLoopStr + "전 종가 : " + coinFormatter.toMoneyStr(beforeLast, marketId) + "\n");
//                    msg.append("종가의 차이 : " + coinFormatter.toSignMoneyStr(currentLast - beforeLast, marketId) + " (" + coinFormatter.toSignPercentStr(currentLast, beforeLast) + ")\n");
//                    msg.append("\n");
//                }
//                break;
//
//            case EN:
//                if (inBtcs.get(marketId)) {
//                    currentHighBTC = currentHigh;
//                    beforeHighBTC = beforeHigh;
//                    currentLowBTC = currentLow;
//                    beforeLowBTC = beforeLow;
//                    currentLastBTC = currentLast;
//                    beforeLastBTC = beforeLast;
//
//                    currentLast = coinCurrentMoney.getDouble("last");
//                    currentHigh = coinCurrentMoney.getDouble("high");
//                    currentLow = coinCurrentMoney.getDouble("low");
//                    beforeLast = coinBeforeMoney.getDouble("last");
//                    beforeHigh = coinBeforeMoney.getDouble("high");
//                    beforeLow = coinBeforeMoney.getDouble("low");
//
//                    msg.append("---------------------\n");
//                    msg.append("Volume at today : " + coinFormatter.toVolumeStr(currentVolume) + " \n");
//                    msg.append("Volume before " + dayLoop + " day : " + coinFormatter.toVolumeStr(beforeVolume) + " \n");
//                    msg.append("Volume difference : " + coinFormatter.toSignVolumeStr(currentVolume - beforeVolume) + " (" + coinFormatter.toSignPercentStr(currentVolume, beforeVolume) + ")\n");
//                    msg.append("\n");
//                    msg.append("High at Today : " + coinFormatter.toMoneyStr(currentHigh, marketId) + " [" + coinFormatter.toBTCStr(currentHighBTC) + "]\n");
//                    msg.append("High before " + dayLoop + " day : " + coinFormatter.toMoneyStr(beforeHigh, marketId) + " [" + coinFormatter.toBTCStr(beforeHighBTC) + "]\n");
//                    msg.append("High difference : " + coinFormatter.toSignMoneyStr(currentHigh - beforeHigh, marketId) + " (" + coinFormatter.toSignPercentStr(currentHigh, beforeHigh) + ")\n");
//                    msg.append("\n");
//                    msg.append("Low at Today : " + coinFormatter.toMoneyStr(currentLow, marketId) + " [" + coinFormatter.toBTCStr(currentLowBTC) + "]\n");
//                    msg.append("Low before " + dayLoop + " day : " + coinFormatter.toMoneyStr(beforeLow, marketId) + " [" + coinFormatter.toBTCStr(beforeLowBTC) + "]\n");
//                    msg.append("Low difference : " + coinFormatter.toSignMoneyStr(currentLow - beforeLow, marketId) + " (" + coinFormatter.toSignPercentStr(currentLow, beforeLow) + ")\n");
//                    msg.append("\n");
//                    msg.append("Last at Today : " + coinFormatter.toMoneyStr(currentLast, marketId) + " [" + coinFormatter.toBTCStr(currentLastBTC) + "]\n");
//                    msg.append("Last before " + dayLoop + " day : " + coinFormatter.toMoneyStr(beforeLast, marketId) + " [" + coinFormatter.toBTCStr(beforeLastBTC) + "]\n");
//                    msg.append("Last difference : " + coinFormatter.toSignMoneyStr(currentLast - beforeLast, marketId) + " (" + coinFormatter.toSignPercentStr(currentLast, beforeLast) + ")\n");
//                    msg.append("\n");
//                } else {
//                    msg.append("---------------------\n");
//                    msg.append("Volume at today : " + coinFormatter.toVolumeStr(currentVolume) + " \n");
//                    msg.append("Volume before " + dayLoop + " day : " + coinFormatter.toVolumeStr(beforeVolume) + " \n");
//                    msg.append("Volume difference : " + coinFormatter.toSignVolumeStr(currentVolume - beforeVolume) + " (" + coinFormatter.toSignPercentStr(currentVolume, beforeVolume) + ")\n");
//                    msg.append("\n");
//                    msg.append("High at Today : " + coinFormatter.toMoneyStr(currentHigh, marketId) + "\n");
//                    msg.append("High before " + dayLoop + " day : " + coinFormatter.toMoneyStr(beforeHigh, marketId) + "\n");
//                    msg.append("High difference : " + coinFormatter.toSignMoneyStr(currentHigh - beforeHigh, marketId) + " (" + coinFormatter.toSignPercentStr(currentHigh, beforeHigh) + ")\n");
//                    msg.append("\n");
//                    msg.append("Low at Today : " + coinFormatter.toMoneyStr(currentLow, marketId) + "\n");
//                    msg.append("Low before " + dayLoop + " day : " + coinFormatter.toMoneyStr(beforeLow, marketId) + "\n");
//                    msg.append("Low difference : " + coinFormatter.toSignMoneyStr(currentLow - beforeLow, marketId) + " (" + coinFormatter.toSignPercentStr(currentLow, beforeLow) + ")\n");
//                    msg.append("\n");
//                    msg.append("Last at Today : " + coinFormatter.toMoneyStr(currentLast, marketId) + "\n");
//                    msg.append("Last before " + dayLoop + " day : " + coinFormatter.toMoneyStr(beforeLast, marketId) + "\n");
//                    msg.append("Last difference : " + coinFormatter.toSignMoneyStr(currentLast - beforeLast, marketId) + " (" + coinFormatter.toSignPercentStr(currentLast, beforeLast) + ")\n");
//                    msg.append("\n");
//                }
//                break;
//        }
//
//        return msg.toString();
//    }
//
//    /**********************************/
//    /** Timely Notification Message ***/
//    /**********************************/
//    public String msgSendTimelyMessage(UserDto client, TimelyInfoDto coinCurrent, TimelyInfoDto coinBefore, JSONObject coinCurrentMoney, JSONObject coinBeforeMoney) {
//        StringBuilder msg = new StringBuilder();
//        Market marketId = client.getMarket();
//        Lang lang = client.getLang();
//        int timeLoop = client.getTimeloopAlert();
//        long localTime = client.getLocaltime();
//        String date = TimeStamper.toDateString(localTime);
//
//        double currentValue = coinCurrent.getLast();
//        double beforeValue = coinBefore.getLast();
//
//        switch (lang) {
//            case KR:
//                msg.append("현재시각: " + date + "\n");
//                if (!coinCurrent.getResult().equals("success")) {
//                    String currentErrorMsg = coinCurrent.getErrorMsg();
//                    String currentErrorCode = coinCurrent.getErrorCode();
//                    msg.append("에러발생: " + currentErrorMsg + "\n");
//                    msg.append("에러코드: " + currentErrorCode + "\n");
//
//                    if (inBtcs.get(marketId)) {
//                        double beforeBTC = beforeValue;
//                        double beforeMoney = coinBeforeMoney.getDouble("last");
//
//                        msg.append(timeLoop + " 시간 전: " + coinFormatter.toMoneyStr(beforeMoney, marketId) + " 원 [" + coinFormatter.toBTCStr(beforeBTC) + " BTC]\n");
//                    } else {
//                        msg.append(timeLoop + " 시간 전: " + coinFormatter.toMoneyStr(beforeValue, marketId) + " 원\n");
//                    }
//                } else {
//                    if (inBtcs.get(marketId)) {
//                        double currentBTC = currentValue;
//                        double beforeBTC = beforeValue;
//                        double currentMoney = coinCurrentMoney.getDouble("last");
//                        double beforeMoney = coinBeforeMoney.getDouble("last");
//
//                        msg.append("현재가격: " + coinFormatter.toMoneyStr(currentMoney, marketId) + " [" + coinFormatter.toBTCStr(currentBTC) + "]\n");
//                        msg.append(timeLoop + " 시간 전: " + coinFormatter.toMoneyStr(beforeMoney, marketId) + " [" + coinFormatter.toBTCStr(beforeBTC) + "]\n");
//                        msg.append("가격차이: " + coinFormatter.toSignMoneyStr(currentMoney - beforeMoney, marketId) + " (" + coinFormatter.toSignPercentStr(currentMoney, beforeMoney) + ")\n");
//                    } else {
//                        msg.append("현재가격: " + coinFormatter.toMoneyStr(currentValue, marketId) + "\n");
//                        msg.append(timeLoop + " 시간 전: " + coinFormatter.toMoneyStr(beforeValue, marketId) + "\n");
//                        msg.append("가격차이: " + coinFormatter.toSignMoneyStr(currentValue - beforeValue, marketId) + " (" + coinFormatter.toSignPercentStr(currentValue, beforeValue) + ")\n");
//                    }
//                }
//                break;
//
//            case EN:
//                msg.append("Current Time: " + date + "\n");
//                if (!coinCurrent.getResult().equals("success")) {
//                    String currentErrorMsg = coinCurrent.getErrorMsg();
//                    String currentErrorCode = coinCurrent.getErrorCode();
//                    msg.append("Error Msg : " + currentErrorMsg + "\n");
//                    msg.append("Error Code: " + currentErrorCode + "\n");
//
//                    if (inBtcs.get(marketId)) {
//                        double beforeBTC = beforeValue;
//                        double beforeMoney = coinBeforeMoney.getDouble("last");
//
//                        msg.append("Coin Price before " + timeLoop + " hour : " + coinFormatter.toMoneyStr(beforeMoney, marketId) + " [" + coinFormatter.toBTCStr(beforeBTC) + "]\n");
//                    } else {
//                        msg.append("Coin Price before " + timeLoop + " hour : " + coinFormatter.toMoneyStr(beforeValue, marketId) + "\n");
//                    }
//                } else {
//                    if (inBtcs.get(marketId)) {
//                        double currentBTC = currentValue;
//                        double beforeBTC = beforeValue;
//                        double currentMoney = coinCurrentMoney.getDouble("last");
//                        double beforeMoney = coinBeforeMoney.getDouble("last");
//
//                        msg.append("Coin Price at Current Time : " + coinFormatter.toMoneyStr(currentMoney, marketId) + " [" + coinFormatter.toBTCStr(currentBTC) + "]\n");
//                        msg.append("Coin Price before " + timeLoop + " hour : " + coinFormatter.toMoneyStr(beforeMoney, marketId) + " [" + coinFormatter.toBTCStr(beforeBTC) + "]\n");
//                        msg.append("Coin Price Difference : " + coinFormatter.toSignMoneyStr(currentMoney - beforeMoney, marketId) + " (" + coinFormatter.toSignPercentStr(currentMoney, beforeMoney) + ")\n");
//                    } else {
//                        msg.append("Coin Price at Current Time : " + coinFormatter.toMoneyStr(currentValue, marketId) + "\n");
//                        msg.append("Coin Price before " + timeLoop + " hour : " + coinFormatter.toMoneyStr(beforeValue, marketId) + "\n");
//                        msg.append("Coin Price Difference : " + coinFormatter.toSignMoneyStr(currentValue - beforeValue, marketId) + " (" + coinFormatter.toSignPercentStr(currentValue, beforeValue) + ")\n");
//                    }
//                }
//                break;
//        }
//
//        return msg.toString();
//    }
//
//
//}
