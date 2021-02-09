package com.podo.coinchatbot.app.telegram.menu.handler.main.message;

import com.podo.coinchatbot.app.domain.dto.CoinInformationDto;
import com.podo.coinchatbot.app.domain.dto.UserTargetAlarmDto;
import com.podo.coinchatbot.app.model.CoinMeta;
import com.podo.coinchatbot.app.telegram.CoinFormatter;
import com.podo.coinchatbot.app.telegram.command.MainCommand;
import com.podo.coinchatbot.app.telegram.command.SuggestMessageCommand;
import com.podo.coinchatbot.app.telegram.exception.InvalidUserLanguageException;
import com.podo.coinchatbot.core.Language;
import com.podo.coinchatbot.core.Market;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.util.List;

@UtilityClass
public class MainCommonMessage {

    public static String explainMarketConfig(Language language) {
        StringBuilder message = new StringBuilder();
        switch (language) {
            case KR:
                message.append("거래중인 거래소를 설정해주세요.\n");
                message.append("모든 정보는 설정 거래소 기준으로 전송됩니다.\n");
                break;
            case EN:
                message.append("Please select an market.\n");
                message.append("All information will be sent on the market you selected.\n");
                break;
            default:
                throw new InvalidUserLanguageException();
        }
        return message.toString();
    }

    public static String targetAlarms(Language language, Market market, List<UserTargetAlarmDto> targets, CoinFormatter coinFormatter) {
        StringBuilder message = new StringBuilder();

        switch (language) {
            case KR:
                message.append("설정된 목표가격은 다음과 같습니다.\n");
                message.append("-------------\n");
                if (targets.isEmpty()) {
                    message.append("설정된 목표가격이 없습니다\n");
                }

                for (UserTargetAlarmDto target : targets) {
                    message.append("# " + coinFormatter.toMoneyStr(target.getTargetPrice(), market) + " " + target.getTargetFocus().kr() + "\n");
                }

                break;
            case EN:
                message.append("Current Target Setting.\n");
                message.append("-------------\n");
                if (targets.isEmpty()) {
                    message.append("There are no set target prices.\n");
                }
                for (UserTargetAlarmDto target : targets) {
                    message.append("# " + coinFormatter.toMoneyStr(target.getTargetPrice(), market) + " " + target.getTargetFocus().en() + "\n");
                }
                break;
            default:
                throw new InvalidUserLanguageException();
        }

        return message.toString();
    }

    public static String explainInvestConfig(Language language, Market market, CoinMeta coinMeta) {
        CoinFormatter coinFormatter = coinMeta.getCoinFormatter();
        CoinMeta.Example example = coinMeta.getExample();
        BigDecimal exampleInvest = null;

        if (market.isKRW()) {
            exampleInvest = example.getInvestKRW();
        }

        if (market.isUSD()) {
            exampleInvest = example.getInvestUSD();
        }

        StringBuilder message = new StringBuilder();
        switch (language) {
            case KR:
                message.append("투자금액을 입력해주세요.\n");
                message.append("투자금액과 코인개수를 입력하시면 손익금을 확인 하실 수 있습니다.\n");
                message.append("\n");
                message.append("* 투자금액은 숫자로만 입력해주세요.\n");
                message.append("* 0을 입력하시면 초기화됩니다.\n");
                message.append("* ex) " + 0 + " : 초기화\n");
                message.append("* ex) " + exampleInvest + " : 투자금액 " + coinFormatter.toInvestAmountStr(exampleInvest, market) + " 설정\n");
                message.append("\n");
                message.append("\n");
                message.append("# 메인으로 돌아가시려면 문자를 입력해주세요.\n");
                break;
            case EN:
                message.append("Please enter your investment amount.\n");
                message.append("If you enter the amount of investment and the number of coins, you can see profit and loss.\n");
                message.append("\n");
                message.append("* Please enter the investment amount in numbers only.\n");
                message.append("* If you enter 0, it is initialized.\n");
                message.append("* example) " + 0 + " : Init investment amount\n");
                message.append("* example) " + exampleInvest + " : investment amount " + coinFormatter.toInvestAmountStr(exampleInvest, market) + " set\n");
                message.append("\n");
                message.append("\n");
                message.append("# To return to main, enter a character.\n");
                break;
            default:
                throw new InvalidUserLanguageException();
        }

        return message.toString();
    }

    public static String explainCoinCountConfig(Language language, CoinMeta coinMeta) {
        CoinMeta.Example example = coinMeta.getExample();
        CoinFormatter coinFormatter = coinMeta.getCoinFormatter();
        BigDecimal exampleCoinCount = example.getCoinCount();

        StringBuilder message = new StringBuilder();
        switch (language) {
            case KR:
                message.append("코인개수를 입력해주세요.\n");
                message.append("투자금액과 코인개수를 입력하시면 손익금을 확인 하실 수 있습니다.\n");
                message.append("\n");
                message.append("* 코인개수는 숫자로만 입력해주세요.\n");
                message.append("* 0을 입력하시면 초기화됩니다.\n");
                message.append("* ex) " + 0 + " : 초기화\n");
                message.append("* ex) " + exampleCoinCount + " : 코인개수 " + coinFormatter.toCoinCntStr(exampleCoinCount, language) + " 설정\n");
                message.append("\n");
                message.append("\n");
                message.append("# 메인으로 돌아가시려면 문자를 입력해주세요.\n");
                break;
            case EN:
                message.append("Please enter your number of coins.\n");
                message.append("If you enter the amount of investment and the number of coins, you can see profit and loss.\n");
                message.append("\n");
                message.append("* Please enter the number of coins in numbers only.\n");
                message.append("* If you enter 0, it is initialized.\n");
                message.append("* example) " + 0 + " : Init the number of coins\n");
                message.append("* example) " + exampleCoinCount + " : the number of coins " + coinFormatter.toCoinCntStr(exampleCoinCount, language) + " set\n");
                message.append("\n");
                message.append("\n");
                message.append("# To return to main, enter a character.\n");
                break;
            default:
                throw new InvalidUserLanguageException();
        }
        return message.toString();
    }

    /*******************************/
    public static String explainSendSuggest(Language language) {
        StringBuilder message = new StringBuilder();
        switch (language) {
            case KR:
                message.append("개발자에게 내용이 전송되어집니다.\n");
                message.append("내용을 입력해주세요.\n");
                message.append("\n");
                message.append("\n");
                message.append("# 메인으로 돌아가시려면 " + SuggestMessageCommand.OUT.getCommand(language) + " 를 입력해주세요.\n");
                break;
            case EN:
                message.append("Please enter message.\n");
                message.append("A message is sent to the developer.\n");
                message.append("\n");
                message.append("\n");
                message.append("# To return to main, enter " + SuggestMessageCommand.OUT.getCommand(language) + "\n");
                break;
            default:
                throw new InvalidUserLanguageException();
        }
        return message.toString();
    }

    public static String explainStopAllAlert(Language language) {
        StringBuilder message = new StringBuilder();
        switch (language) {
            case KR:
                message.append("모든 알림(일일알림, 시간알림, 목표가알림)을 중지하시겠습니까?\n");
                message.append("\n");
                message.append("★ 필독!\n");
                message.append("1. 모든알림이 중지되더라도 공지사항은 전송됩니다.\n");
                message.append("2. 모든알림이 중지되더라도 버튼을 통해 코인관련정보를 받을 수 있습니다.\n");
                message.append("3. 서비스를 완전히 중지하시려면 대화방을 삭제해주세요!\n");
                break;
            case EN:
                message.append("Are you sure you want to stop all notifications (daily, hourly , target price notifications )?\n");
                message.append("\n");
                message.append("★  Must read!\n");
                message.append("1. Even if all notifications have been stopped, you will continue to receive notification of service usage.\n");
                message.append("2. Even if all notifications have been stopped, you received coin information using menu.\n");
                message.append("3. If you want to stop completry this service, Please block bot.\n");
                break;
            default:
                throw new InvalidUserLanguageException();
        }

        return message.toString();
    }

    public static String explainDayloopConfig(Language language) {
        StringBuilder message = new StringBuilder();
        switch (language) {
            case KR:
                message.append("일일 알림 주기를 선택해주세요.\n");
                message.append("선택 하신 일일주기로 알림이 전송됩니다.\n");
                break;
            case EN:
                message.append("Please select daily notifications cycle.\n");
                message.append("Coin Price information will be sent according to the cycle.\n");
                break;
            default:
                throw new InvalidUserLanguageException();
        }
        return message.toString();
    }

    public static String explainTimeloopConfig(Language language) {
        StringBuilder message = new StringBuilder();
        switch (language) {
            case KR:
                message.append("시간 알림 주기를 선택해주세요.\n");
                message.append("선택 하신 시간주기로 알림이 전송됩니다.\n");
                break;
            case EN:
                message.append("Please select hourly notifications cycle.\n");
                message.append("Coin Price information will be sent according to the cycle.\n");
                break;
            default:
                throw new InvalidUserLanguageException();
        }
        return message.toString();
    }

    public static String explainSupport(Language language) {
        StringBuilder message = new StringBuilder();
        String url = "https://www.buymeacoffee.com/podo";
        switch (language) {
            case KR:
                message.append("<a href='");
                message.append(url);
                message.append("'>★ 후원하러가기!</a>");
                message.append("\n");
                message.append("\n");
                message.append("감사합니다. BY 개발자 podo\n");
                break;

            case EN:
                message.append("<a href='");
                message.append(url);
                message.append("'>★ Go to Sponsoring!</a>");
                message.append("\n");
                message.append("\n");
                message.append("Thank you for sponsoring by developer. podo\n");
                break;

            default:
                throw new InvalidUserLanguageException();
        }
        return message.toString();
    }

    public static String explainCoinList(Language language, List<CoinInformationDto> coinInformations) {
        StringBuilder message = new StringBuilder();

        switch (language) {
            case KR:
                message.append("링크를 클릭 하시면,\n");
                message.append("해당 코인알리미 봇으로 이동합니다.\n");
                message.append("-----------------------\n");

                for (CoinInformationDto coinInformation : coinInformations) {
                    message.append(coinInformation.getCoin() + " [" + coinInformation.getCoin().kr() + "] \n");
                    message.append(coinInformation.getChatUrl() + "\n");
                    message.append("\n");
                }
                message.append("\n");
                break;
            case EN:
                message.append("Click on the link to go to other Coin Noticer.\n");
                message.append("-----------------------\n");
                for (CoinInformationDto coinInformation : coinInformations) {
                    message.append(coinInformation.getCoin() + " [" + coinInformation.getCoin().en() + "] \n");
                    message.append(coinInformation.getChatUrl() + "\n");
                    message.append("\n");
                }
                message.append("\n");
                break;
            default:
                throw new InvalidUserLanguageException();
        }
        return message.toString();
    }

    public static String explainHappyLine(Language language, Market market, CoinMeta coinMeta) {
        CoinMeta.Example example = coinMeta.getExample();
        CoinFormatter coinFormatter = coinMeta.getCoinFormatter();

        StringBuilder message = new StringBuilder();
        BigDecimal exampleTarget = null;
        if (market.isKRW()) {
            exampleTarget = example.getTargetKRW();
        }
        if (market.isUSD()) {
            exampleTarget = example.getTargetUSD();
        }

        switch (language) {
            case KR:
                message.append("원하시는 코인가격을 입력해주세요.\n");
                message.append("희망 손익금을 확인 하실 수 있습니다.\n");
                message.append("\n");
                message.append("* 코인가격은 숫자로 입력해주세요.\n");
                message.append("* ex) " + exampleTarget + "  : 희망 코인가격 " + coinFormatter.toMoneyStr(exampleTarget, market) + "\n");
                message.append("\n");
                message.append("\n");
                message.append("# 메인으로 돌아가시려면 문자를 입력해주세요.\n");
                break;
            case EN:
                message.append("Please enter the desired coin price.\n");
                message.append("if enter your desired coin price,  you can see expected profit and loss.\n");
                message.append("\n");
                message.append("* Please enter the coin price in numbers only.\n");
                message.append("* example) " + exampleTarget + "  : desired coin price " + coinFormatter.toMoneyStr(exampleTarget, market) + " set\n");
                message.append("\n");
                message.append("\n");
                message.append("# To return to main, enter a character.\n");
                break;
            default:
                throw new InvalidUserLanguageException();
        }

        return message.toString();
    }

    public static String pleaseConfigInvest(Language language) {
        switch (language) {
            case KR:
                return "먼저 투자금액을 설정해주세요.\n메뉴에서 '" + MainCommand.INVESET_CONFIG.getCommand(language) + "' 을 클릭해주세요.";
            case EN:
                return "Please set your investment amount first.\nPlease Click '" + MainCommand.INVESET_CONFIG.getCommand(language) + "' on the main menu.";
            default:
                throw new InvalidUserLanguageException();
        }
    }

    public static String pleaseConfigCoinCount(Language language) {
        switch (language) {
            case KR:
                return "먼저 코인개수를 설정해주세요.\n메뉴에서 '" + MainCommand.COINCOUNT_CONFIG.getCommand(language) + "' 을 클릭해주세요.";
            case EN:
                return "Please set the number of coins first.\nPlease Click '" + MainCommand.COINCOUNT_CONFIG.getCommand(language) + "' on the main menu.";
            default:
                throw new InvalidUserLanguageException();
        }
    }


}
