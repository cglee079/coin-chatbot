package com.podo.coinchatbot.app.job.alarm.timelyalarm;

import com.podo.coinchatbot.app.client.CoinEndpointer;
import com.podo.coinchatbot.app.client.CoinEndpointerUtil;
import com.podo.coinchatbot.app.domain.dto.TimelyCoinPriceDto;
import com.podo.coinchatbot.app.domain.dto.UserDto;
import com.podo.coinchatbot.app.domain.model.TimelyCoinPriceStatus;
import com.podo.coinchatbot.app.domain.service.TimelyCoinPriceService;
import com.podo.coinchatbot.app.domain.service.UserService;
import com.podo.coinchatbot.app.property.MarketConfig;
import com.podo.coinchatbot.app.telegram.CoinFormatter;
import com.podo.coinchatbot.app.telegram.TelegramMessageSender;
import com.podo.coinchatbot.app.telegram.exception.InvalidUserLanguageException;
import com.podo.coinchatbot.app.telegram.model.MessageVo;
import com.podo.coinchatbot.app.telegram.model.SendMessageVo;
import com.podo.coinchatbot.app.util.DateTimeUtil;
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
public class HourlyAlarmExecutor {

    private static final Logger LOGGER = LoggerFactory.getLogger("ALARM_LOGGER");

    private final UserService userService;
    private final TimelyCoinPriceService timelyCoinPriceService;
    private final CoinEndpointer coinEndpointer;
    private final Map<Coin, CoinFormatter> coinToCoinFormatters;
    private final Map<Coin, TelegramMessageSender> coinToTelegramMessageSenders;
    private final ExecutorService executorService = Executors.newFixedThreadPool(3);

    public void sendHourlyAlarm(Coin coin, MarketConfig marketConfig, Integer timeloop, LocalDateTime now) {
        Market market = marketConfig.getMarket();
        List<UserDto> users = userService.getForTimeloopAlarm(coin, market, timeloop);
        CoinFormatter coinFormatter = coinToCoinFormatters.get(coin);
        TelegramMessageSender telegramMessageSender = coinToTelegramMessageSenders.get(coin);

        if (users.isEmpty()) {
            return;
        }

        TimelyCoinPriceDto currentHourCoinPrice = timelyCoinPriceService.getByCoinAndMarketAndDateTime(coin, market, now);
        TimelyCoinPriceDto beforeHourCoinPrice = timelyCoinPriceService.getByCoinAndMarketAndDateTime(coin, market, now.minusHours(timeloop));

        if (marketConfig.getIsBtc()) {
            BigDecimal coinCurrentMoney = CoinEndpointerUtil.btcToMoney(coinEndpointer, market, currentHourCoinPrice.getLastPrice());
            BigDecimal coinBeforeMoney = CoinEndpointerUtil.btcToMoney(coinEndpointer, market, beforeHourCoinPrice.getLastPrice());

            for (UserDto user : users) {
                String message = createMessageHourlyAlarmInBtc(user, currentHourCoinPrice, beforeHourCoinPrice.getLastPrice(), coinCurrentMoney, coinBeforeMoney, coinFormatter, now);
                sendAlarm(coin, user, message, telegramMessageSender);
            }
            return;
        }

        for (UserDto user : users) {
            String message = createMessageHourlyAlarm(user, currentHourCoinPrice, beforeHourCoinPrice.getLastPrice(), coinFormatter, now);
            sendAlarm(coin, user, message, telegramMessageSender);
        }
    }

    private void sendAlarm(Coin coin, UserDto user, String message, TelegramMessageSender telegramMessageSender) {
        executorService.submit(() -> {
            ThreadLocalContext.init("hourly-alarm");
            ThreadLocalContext.put("coin", coin);
            try {
                telegramMessageSender.sendAlarm(SendMessageVo.create(new MessageVo(user.getTelegramId(), user.getChatId()), message, null));
            } catch (Exception e) {
                ThreadLocalContext.putException(e);
            } finally {
                LOGGER.info("", StructuredArguments.value("context", ThreadLocalContext.toLog()));
                ThreadLocalContext.removeAll();
            }
        });
    }

    private String createMessageHourlyAlarmInBtc(UserDto user, TimelyCoinPriceDto currentTimelyCoinPrice, BigDecimal beforeBTC, BigDecimal currentMoney, BigDecimal beforeMoney, CoinFormatter coinFormatter, LocalDateTime now) {
        StringBuilder message = new StringBuilder();
        Integer timeloop = user.getTimeloopAlarm();
        Market market = user.getMarket();
        BigDecimal currentBTC = currentTimelyCoinPrice.getLastPrice();
        LocalDateTime userLocalDateTime = DateTimeUtil.longToLocalDateTime(DateTimeUtil.dateTimeToLong(now) + user.getTimeDifference());

        switch (user.getLanguage()) {
            case KR:
                message.append("현재시각: " + DateTimeUtil.toDateTimeString(userLocalDateTime) + "\n");
                if (currentTimelyCoinPrice.getStatus().equals(TimelyCoinPriceStatus.ERROR)) {
                    message.append("에러발생: " + currentTimelyCoinPrice.getAdditionalInfo() + "\n");
                    message.append(timeloop + " 시간 전: " + coinFormatter.toMoneyStr(beforeMoney, market) + " 원 [" + coinFormatter.toBTCStr(beforeBTC) + "]\n");
                } else {
                    message.append("현재가격: " + coinFormatter.toMoneyStr(currentMoney, market) + " [" + coinFormatter.toBTCStr(currentBTC) + "]\n");
                    message.append(timeloop + " 시간 전: " + coinFormatter.toMoneyStr(beforeMoney, market) + " [" + coinFormatter.toBTCStr(beforeBTC) + "]\n");
                    message.append("가격차이: " + coinFormatter.toSignMoneyStr(currentMoney.subtract(beforeMoney), market) + " (" + coinFormatter.toSignPercentStr(currentMoney, beforeMoney) + ")\n");
                }
                break;
            case EN:
                message.append("Current Time: " + DateTimeUtil.toDateTimeString(now) + "\n");
                if (currentTimelyCoinPrice.getStatus().equals(TimelyCoinPriceStatus.ERROR)) {
                    message.append("Error Message: " + currentTimelyCoinPrice.getAdditionalInfo() + "\n");
                    message.append("Coin Price before " + timeloop + " hour : " + coinFormatter.toMoneyStr(beforeMoney, market) + " [" + coinFormatter.toBTCStr(beforeBTC) + "]\n");
                } else {
                    message.append("Coin Price at Current Time : " + coinFormatter.toMoneyStr(currentMoney, market) + " [" + coinFormatter.toBTCStr(currentBTC) + "]\n");
                    message.append("Coin Price before " + timeloop + " hour : " + coinFormatter.toMoneyStr(beforeMoney, market) + " [" + coinFormatter.toBTCStr(beforeBTC) + "]\n");
                    message.append("Coin Price Difference : " + coinFormatter.toSignMoneyStr(currentMoney.subtract(beforeMoney), market) + " (" + coinFormatter.toSignPercentStr(currentMoney, beforeMoney) + ")\n");
                }
                break;
            default:
                throw new InvalidUserLanguageException();
        }

        return message.toString();
    }

    private String createMessageHourlyAlarm(UserDto user, TimelyCoinPriceDto currentTimelyPrice, BigDecimal beforeMoney, CoinFormatter coinFormatter, LocalDateTime now) {
        StringBuilder message = new StringBuilder();
        Market market = user.getMarket();
        Integer timeloop = user.getTimeloopAlarm();
        BigDecimal currentMoney = currentTimelyPrice.getLastPrice();
        LocalDateTime userLocalDateTime = DateTimeUtil.longToLocalDateTime(DateTimeUtil.dateTimeToLong(now) + user.getTimeDifference());

        switch (user.getLanguage()) {
            case KR:
                message.append("현재시각: " + DateTimeUtil.toDateTimeString(userLocalDateTime) + "\n");
                if (currentTimelyPrice.getStatus().equals(TimelyCoinPriceStatus.ERROR)) {
                    message.append("에러발생: " + currentTimelyPrice.getAdditionalInfo() + "\n");
                    message.append(timeloop + " 시간 전: " + coinFormatter.toMoneyStr(beforeMoney, market) + " 원\n");
                } else {
                    message.append("현재가격: " + coinFormatter.toMoneyStr(currentMoney, market) + "\n");
                    message.append(timeloop + " 시간 전: " + coinFormatter.toMoneyStr(beforeMoney, market) + "\n");
                    message.append("가격차이: " + coinFormatter.toSignMoneyStr(currentMoney.subtract(beforeMoney), market) + " (" + coinFormatter.toSignPercentStr(currentMoney, beforeMoney) + ")\n");
                }
                break;
            case EN:
                message.append("Current Time: " + DateTimeUtil.toDateTimeString(now) + "\n");
                if (currentTimelyPrice.getStatus().equals(TimelyCoinPriceStatus.ERROR)) {
                    message.append("Error Message: " + currentTimelyPrice.getAdditionalInfo() + "\n");
                    message.append("Coin Price before " + timeloop + " hour : " + coinFormatter.toMoneyStr(beforeMoney, market) + "\n");
                } else {
                    message.append("Coin Price at Current Time : " + coinFormatter.toMoneyStr(currentMoney, market) + "\n");
                    message.append("Coin Price before " + timeloop + " hour : " + coinFormatter.toMoneyStr(beforeMoney, market) + "\n");
                    message.append("Coin Price Difference : " + coinFormatter.toSignMoneyStr(currentMoney.subtract(beforeMoney), market) + " (" + coinFormatter.toSignPercentStr(currentMoney, beforeMoney) + ")\n");
                }
                break;
            default:
                throw new InvalidUserLanguageException();
        }

        return message.toString();
    }
}
