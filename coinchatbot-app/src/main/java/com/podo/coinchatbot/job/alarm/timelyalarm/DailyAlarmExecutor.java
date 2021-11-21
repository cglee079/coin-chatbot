package com.podo.coinchatbot.job.alarm.timelyalarm;

import com.podo.coinchatbot.external.CoinEndpointer;
import com.podo.coinchatbot.external.CoinEndpointerUtil;
import com.podo.coinchatbot.external.model.CoinResponse;
import com.podo.coinchatbot.app.domain.dto.TimelyCoinPriceDto;
import com.podo.coinchatbot.app.domain.dto.UserDto;
import com.podo.coinchatbot.app.domain.service.TimelyCoinPriceService;
import com.podo.coinchatbot.app.domain.service.UserService;
import com.podo.coinchatbot.property.MarketConfig;
import com.podo.coinchatbot.telegram.CoinFormatter;
import com.podo.coinchatbot.telegram.TelegramMessageSender;
import com.podo.coinchatbot.telegram.model.MessageVo;
import com.podo.coinchatbot.telegram.model.SendMessageVo;
import com.podo.coinchatbot.util.DateTimeUtil;
import com.podo.coinchatbot.core.Coin;
import com.podo.coinchatbot.core.Market;
import com.podo.coinchatbot.log.ThreadLocalContext;
import lombok.RequiredArgsConstructor;
import net.logstash.logback.argument.StructuredArguments;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RequiredArgsConstructor
@Component
public class DailyAlarmExecutor {

    private static final Logger LOGGER = LoggerFactory.getLogger("ALARM_LOGGER");

    private final UserService userService;
    private final TimelyCoinPriceService timelyCoinPriceService;
    private final CoinEndpointer coinEndpointer;
    private final Map<Coin, CoinFormatter> coinToCoinFormatters;
    private final Map<Coin, TelegramMessageSender> coinToTelegramMessageSenders;
    private final ExecutorService executorService = Executors.newFixedThreadPool(3);

    public void sendDailyAlarm(Coin coin, MarketConfig marketConfig, Integer dayloop, LocalDateTime now) {
        Market market = marketConfig.getMarket();
        List<UserDto> users = userService.getForDayloopAlarm(coin, market, dayloop, now);
        CoinFormatter coinFormatter = coinToCoinFormatters.get(coin);
        TelegramMessageSender telegramMessageSender = coinToTelegramMessageSenders.get(coin);

        if (users.isEmpty()) {
            return;
        }

        TimelyCoinPriceDto currentDayCoinPrice = timelyCoinPriceService.getByCoinAndMarketAndDateTime(coin, market, now);
        TimelyCoinPriceDto beforeDaysCoinPrice = timelyCoinPriceService.getByCoinAndMarketAndDateTime(coin, market, now.minusDays(dayloop));

        if (marketConfig.getIsBtc()) {
            CoinResponse bitcoinResponse = CoinEndpointerUtil.getCoin(coinEndpointer, Coin.BTC, market);

            for (UserDto user : users) {
                String message = createMessageDailyAlarmInBtc(user, currentDayCoinPrice, beforeDaysCoinPrice, bitcoinResponse.getCurrentPrice(), coinFormatter, now);
                sendAlarm(coin, user, message, telegramMessageSender);
            }
            return;
        }

        for (UserDto user : users) {
            String message = createMessageDailyAlarm(user, currentDayCoinPrice, beforeDaysCoinPrice, coinFormatter, now);
            sendAlarm(coin, user, message, telegramMessageSender);
        }
    }

    private void sendAlarm(Coin coin, UserDto user, String message, TelegramMessageSender telegramMessageSender) {
        executorService.submit(() -> {
            ThreadLocalContext.init("daily-alarm");
            ThreadLocalContext.put("coin.id", coin);
            try {
                telegramMessageSender.sendAlarm(SendMessageVo.create(new MessageVo(user.getTelegramId(), user.getChatId()), message, null));
            } catch (Exception e) {
                userService.increaseErrorCount(user.getId());
                ThreadLocalContext.putException(e);
            } finally {
                LOGGER.info("", StructuredArguments.value("context", ThreadLocalContext.toLog()));
                ThreadLocalContext.removeAll();
            }
        });
    }

    private String createMessageDailyAlarmInBtc(UserDto user, TimelyCoinPriceDto currentDayCoinPrice, TimelyCoinPriceDto beforeDaysCoinPrice, BigDecimal currentBitcoinPrice, CoinFormatter coinFormatter, LocalDateTime now) {
        BigDecimal currentVolume = currentDayCoinPrice.getVolume();
        BigDecimal currentHighBTC = currentDayCoinPrice.getHighPrice();
        BigDecimal currentLowBTC = currentDayCoinPrice.getLowPrice();
        BigDecimal currentLastBTC = currentDayCoinPrice.getLastPrice();

        BigDecimal beforeVolume = beforeDaysCoinPrice.getVolume();
        BigDecimal beforeLowBTC = beforeDaysCoinPrice.getLowPrice();
        BigDecimal beforeHighBTC = beforeDaysCoinPrice.getHighPrice();
        BigDecimal beforeLastBTC = beforeDaysCoinPrice.getLastPrice();

        BigDecimal currentLastMoney = currentLastBTC.multiply(currentBitcoinPrice);
        BigDecimal currentHighMoney = currentHighBTC.multiply(currentBitcoinPrice);
        BigDecimal currentLowMoney = currentLowBTC.multiply(currentBitcoinPrice);
        BigDecimal beforeLastMoney = beforeLastBTC.multiply(currentBitcoinPrice);
        BigDecimal beforeHighMoney = beforeHighBTC.multiply(currentBitcoinPrice);
        BigDecimal beforeLowMoney = beforeLowBTC.multiply(currentBitcoinPrice);

        Integer dayloop = user.getDayloopAlarm();
        Market market = user.getMarket();
        LocalDateTime userLocalDateTime = DateTimeUtil.longToLocalDateTime(DateTimeUtil.dateTimeToLong(now) + user.getTimeDifference());

        StringBuilder message = new StringBuilder();
        message.append(DateTimeUtil.toDateTimeString(userLocalDateTime) + "\n");

        switch (user.getLanguage()) {
            case KR:
                message.append("---------------------\n");
                message.append("금일의 거래량 : " + coinFormatter.toVolumeStr(currentVolume) + " \n");
                message.append(dayloop + "일전 거래량 : " + coinFormatter.toVolumeStr(beforeVolume) + " \n");
                message.append("거래량의 차이 : " + coinFormatter.toSignVolumeStr(currentVolume.subtract(beforeVolume)) + " (" + coinFormatter.toSignPercentStr(currentVolume, beforeVolume) + ")\n");
                message.append("\n");

                message.append("금일의 상한가 : " + coinFormatter.toMoneyStr(currentHighMoney, market) + " [" + coinFormatter.toBTCStr(currentHighBTC) + "]\n");
                message.append(dayloop + "일전 상한가 : " + coinFormatter.toMoneyStr(beforeHighMoney, market) + " [" + coinFormatter.toBTCStr(beforeHighBTC) + "]\n");
                message.append("상한가의 차이 : " + coinFormatter.toSignMoneyStr(currentHighMoney.subtract(beforeHighMoney), market) + " (" + coinFormatter.toSignPercentStr(currentHighMoney, beforeHighMoney) + ")\n");
                message.append("\n");

                message.append("금일의 하한가 : " + coinFormatter.toMoneyStr(currentLowMoney, market) + " [" + coinFormatter.toBTCStr(currentLowBTC) + "]\n");
                message.append(dayloop + "일전 하한가 : " + coinFormatter.toMoneyStr(beforeLowMoney, market) + " [" + coinFormatter.toBTCStr(beforeLowBTC) + "]\n");
                message.append("하한가의 차이 : " + coinFormatter.toSignMoneyStr(currentLowMoney.subtract(beforeLowMoney), market) + " (" + coinFormatter.toSignPercentStr(currentLowMoney, beforeLowMoney) + ")\n");
                message.append("\n");

                message.append("금일의 종가 : " + coinFormatter.toMoneyStr(currentLastMoney, market) + " [" + coinFormatter.toBTCStr(currentLastBTC) + "]\n");
                message.append(dayloop + "일전 종가 : " + coinFormatter.toMoneyStr(beforeLastMoney, market) + " [" + coinFormatter.toBTCStr(beforeLastBTC) + "]\n");
                message.append("종가의 차이 : " + coinFormatter.toSignMoneyStr(currentLastMoney.subtract(beforeLastMoney), market) + " (" + coinFormatter.toSignPercentStr(currentLastMoney, beforeLastMoney) + ")\n");
                message.append("\n");


            case EN:
                message.append("---------------------\n");
                message.append("Volume at today : " + coinFormatter.toVolumeStr(currentVolume) + " \n");
                message.append("Volume before " + dayloop + " day : " + coinFormatter.toVolumeStr(beforeVolume) + " \n");
                message.append("Volume difference : " + coinFormatter.toSignVolumeStr(currentVolume.subtract(beforeVolume)) + " (" + coinFormatter.toSignPercentStr(currentVolume, beforeVolume) + ")\n");
                message.append("\n");
                message.append("High at Today : " + coinFormatter.toMoneyStr(currentHighMoney, market) + " [" + coinFormatter.toBTCStr(currentHighBTC) + "]\n");
                message.append("High before " + dayloop + " day : " + coinFormatter.toMoneyStr(beforeHighMoney, market) + " [" + coinFormatter.toBTCStr(beforeHighBTC) + "]\n");
                message.append("High difference : " + coinFormatter.toSignMoneyStr(currentHighMoney.subtract(beforeHighMoney), market) + " (" + coinFormatter.toSignPercentStr(currentHighMoney, beforeHighMoney) + ")\n");
                message.append("\n");
                message.append("Low at Today : " + coinFormatter.toMoneyStr(currentLowMoney, market) + " [" + coinFormatter.toBTCStr(currentLowBTC) + "]\n");
                message.append("Low before " + dayloop + " day : " + coinFormatter.toMoneyStr(beforeLowMoney, market) + " [" + coinFormatter.toBTCStr(beforeLowBTC) + "]\n");
                message.append("Low difference : " + coinFormatter.toSignMoneyStr(currentLowMoney.subtract(beforeLowMoney), market) + " (" + coinFormatter.toSignPercentStr(currentLowMoney, beforeLowMoney) + ")\n");
                message.append("\n");
                message.append("Last at Today : " + coinFormatter.toMoneyStr(currentLastMoney, market) + " [" + coinFormatter.toBTCStr(currentLastBTC) + "]\n");
                message.append("Last before " + dayloop + " day : " + coinFormatter.toMoneyStr(beforeLastMoney, market) + " [" + coinFormatter.toBTCStr(beforeLastBTC) + "]\n");
                message.append("Last difference : " + coinFormatter.toSignMoneyStr(currentLastMoney.subtract(beforeLastMoney), market) + " (" + coinFormatter.toSignPercentStr(currentLastMoney, beforeLastMoney) + ")\n");
                message.append("\n");
                break;
        }

        return message.toString();
    }

    private String createMessageDailyAlarm(UserDto user, TimelyCoinPriceDto currentDayCoinPrice, TimelyCoinPriceDto beforeDaysCoinPrice, CoinFormatter coinFormatter, LocalDateTime now) {
        BigDecimal currentVolume = currentDayCoinPrice.getVolume();
        BigDecimal currentHigh = currentDayCoinPrice.getHighPrice();
        BigDecimal currentLow = currentDayCoinPrice.getLowPrice();
        BigDecimal currentLast = currentDayCoinPrice.getLastPrice();
        BigDecimal beforeVolume = beforeDaysCoinPrice.getVolume();
        BigDecimal beforeLow = beforeDaysCoinPrice.getLowPrice();
        BigDecimal beforeHigh = beforeDaysCoinPrice.getHighPrice();
        BigDecimal beforeLast = beforeDaysCoinPrice.getLastPrice();
        Integer dayloop = user.getDayloopAlarm();
        Market market = user.getMarket();
        LocalDateTime userLocalDateTime = DateTimeUtil.longToLocalDateTime(DateTimeUtil.dateTimeToLong(now) + user.getTimeDifference());

        StringBuilder message = new StringBuilder();
        message.append(DateTimeUtil.toDateTimeString(userLocalDateTime) + "\n");

        switch (user.getLanguage()) {
            case KR:
                message.append("---------------------\n");
                message.append("금일의 거래량 : " + coinFormatter.toVolumeStr(currentVolume) + " \n");
                message.append(dayloop + "일전 거래량 : " + coinFormatter.toVolumeStr(beforeVolume) + " \n");
                message.append("거래량의 차이 : " + coinFormatter.toSignVolumeStr(currentVolume.subtract(beforeVolume)) + " (" + coinFormatter.toSignPercentStr(currentVolume, beforeVolume) + ")\n");
                message.append("\n");
                message.append("금일의 상한가 : " + coinFormatter.toMoneyStr(currentHigh, market) + "\n");
                message.append(dayloop + "일전 상한가 : " + coinFormatter.toMoneyStr(beforeHigh, market) + "\n");
                message.append("상한가의 차이 : " + coinFormatter.toSignMoneyStr(currentHigh.subtract(beforeHigh), market) + " (" + coinFormatter.toSignPercentStr(currentHigh, beforeHigh) + ")\n");
                message.append("\n");
                message.append("금일의 하한가 : " + coinFormatter.toMoneyStr(currentLow, market) + "\n");
                message.append(dayloop + "일전 하한가 : " + coinFormatter.toMoneyStr(beforeLow, market) + "\n");
                message.append("하한가의 차이 : " + coinFormatter.toSignMoneyStr(currentLow.subtract(beforeLow), market) + " (" + coinFormatter.toSignPercentStr(currentLow, beforeLow) + ")\n");
                message.append("\n");
                message.append("금일의 종가 : " + coinFormatter.toMoneyStr(currentLast, market) + "\n");
                message.append(dayloop + "일전 종가 : " + coinFormatter.toMoneyStr(beforeLast, market) + "\n");
                message.append("종가의 차이 : " + coinFormatter.toSignMoneyStr(currentLast.subtract(beforeLast), market) + " (" + coinFormatter.toSignPercentStr(currentLast, beforeLast) + ")\n");
                message.append("\n");
                break;

            case EN:
                message.append("---------------------\n");
                message.append("Volume at today : " + coinFormatter.toVolumeStr(currentVolume) + " \n");
                message.append("Volume before " + dayloop + " day : " + coinFormatter.toVolumeStr(beforeVolume) + " \n");
                message.append("Volume difference : " + coinFormatter.toSignVolumeStr(currentVolume.subtract(beforeVolume)) + " (" + coinFormatter.toSignPercentStr(currentVolume, beforeVolume) + ")\n");
                message.append("\n");
                message.append("High at Today : " + coinFormatter.toMoneyStr(currentHigh, market) + "\n");
                message.append("High before " + dayloop + " day : " + coinFormatter.toMoneyStr(beforeHigh, market) + "\n");
                message.append("High difference : " + coinFormatter.toSignMoneyStr(currentHigh.subtract(beforeHigh), market) + " (" + coinFormatter.toSignPercentStr(currentHigh, beforeHigh) + ")\n");
                message.append("\n");
                message.append("Low at Today : " + coinFormatter.toMoneyStr(currentLow, market) + "\n");
                message.append("Low before " + dayloop + " day : " + coinFormatter.toMoneyStr(beforeLow, market) + "\n");
                message.append("Low difference : " + coinFormatter.toSignMoneyStr(currentLow.subtract(beforeLow), market) + " (" + coinFormatter.toSignPercentStr(currentLow, beforeLow) + ")\n");
                message.append("\n");
                message.append("Last at Today : " + coinFormatter.toMoneyStr(currentLast, market) + "\n");
                message.append("Last before " + dayloop + " day : " + coinFormatter.toMoneyStr(beforeLast, market) + "\n");
                message.append("Last difference : " + coinFormatter.toSignMoneyStr(currentLast.subtract(beforeLast), market) + " (" + coinFormatter.toSignPercentStr(currentLast, beforeLast) + ")\n");
                message.append("\n");
                break;
        }

        return message.toString();
    }
}
