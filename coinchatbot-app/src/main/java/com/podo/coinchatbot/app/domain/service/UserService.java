package com.podo.coinchatbot.app.domain.service;

import com.podo.coinchatbot.app.domain.dto.UserDto;
import com.podo.coinchatbot.app.domain.exception.InvalidUserException;
import com.podo.coinchatbot.app.domain.model.User;
import com.podo.coinchatbot.app.domain.model.UserStatus;
import com.podo.coinchatbot.app.domain.repository.UserRepository;
import com.podo.coinchatbot.app.model.Menu;
import com.podo.coinchatbot.util.DateTimeUtil;
import com.podo.coinchatbot.util.NumberUtil;
import com.podo.coinchatbot.core.Coin;
import com.podo.coinchatbot.core.Language;
import com.podo.coinchatbot.core.Market;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public synchronized void createNewUser(Coin coin, Integer telegramId, Long chatId, String username, Market market, LocalDateTime messageSendAt) {
        User user = User.builder()
                .coin(coin)
                .telegramId(telegramId)
                .chatId(chatId)
                .username(username)
                .invest(null)
                .coinCount(null)
                .dayloopAlarm(0)
                .timeloopAlarm(0)
                .timeDifference(0L)
                .market(market)
                .menuStatus(Menu.MAIN)
                .messageSendAt(messageSendAt)
                .language(Language.KR)
                .errorCount(0)
                .userStatus(UserStatus.ALIVE)
                .build();

        userRepository.save(user);
    }

    @Transactional
    public void updateMessageSendAt(Long userId, LocalDateTime messageReceiveAt) {
        User user = findByUserId(userId);
        user.updateMessageSendAt(messageReceiveAt);
    }

    @Transactional
    public void updateMenuStatus(Long userId, Menu menuStatus) {
        User user = findByUserId(userId);
        user.moveMenu(menuStatus);
    }

    @Transactional
    public void updateDayLoop(Long userId, Integer dayloopValue) {
        User user = findByUserId(userId);
        user.changeDayloopAlarm(dayloopValue);
    }

    @Transactional
    public void updateTimeLoop(Long userId, Integer timeloopValue) {
        User user = findByUserId(userId);
        user.changeTimeloopAlarm(timeloopValue);
    }

    @Transactional
    public void updateInvest(Long userId, BigDecimal changedInvest) {
        User user = findByUserId(userId);

        if (NumberUtil.eq(BigDecimal.ZERO, changedInvest)) {
            user.changeInvest(null);
        }

        user.changeInvest(changedInvest);
    }

    @Transactional
    public void updateMarket(Long userId, Market changedMarket) {
        User user = findByUserId(userId);
        user.changeMarket(changedMarket);
    }

    @Transactional
    public void updateCoinCount(Long userId, BigDecimal coinCount) {
        User user = findByUserId(userId);

        if (NumberUtil.eq(BigDecimal.ZERO, coinCount)) {
            user.changeCoinCount(null);
            return;
        }

        user.changeCoinCount(coinCount);
    }

    @Transactional
    public void stopAllAlarm(Long userId) {
        User user = findByUserId(userId);
        user.stopAllAlarm();
    }

    @Transactional
    public void updateLanguage(Long userId, Language changedLanguage) {
        User user = findByUserId(userId);
        user.changeLanguage(changedLanguage);
    }

    @Transactional
    public void updateTimeDifference(Long userId, Long timeDifference) {
        User user = findByUserId(userId);
        user.changeTimeDifference(timeDifference);
    }

    @Transactional
    public void increaseErrorCount(Long userId) {
        User user = findByUserId(userId);
        user.increaseErrorCount();
    }

    private User findByUserId(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        return userOptional.orElseThrow(() -> new InvalidUserException(userId));
    }

    @Transactional(readOnly = true)
    public UserDto getOrNull(Coin coin, Long chatId) {
        Optional<User> userOptional = userRepository.findByCoinAndChatId(coin, chatId);

        return userOptional.map(UserDto::new)
                .orElseGet(() -> null);
    }

    @Transactional(readOnly = true)
    public UserDto get(Coin coin, Long chatId) {
        Optional<User> userOptional = userRepository.findByCoinAndChatId(coin, chatId);
        User user = userOptional.orElseThrow(() -> new InvalidUserException(coin, chatId));

        return new UserDto(user);
    }

    @Transactional(readOnly = true)
    public UserDto getByUserId(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        return userOptional.map(UserDto::new).orElseThrow(() -> new InvalidUserException(userId));
    }

    @Transactional(readOnly = true)
    public List<UserDto> getForTimeloopAlarm(Coin coin, Market market, Integer timeloop) {
        return userRepository.findByCoinAndMarketAndTimeloopAlarm(coin, market, timeloop)
                .stream()
                .map(UserDto::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UserDto> getForDayloopAlarm(Coin coin, Market market, Integer dayLoop, LocalDateTime now) {
        return userRepository.findByCoinAndMarketAndDayloopAlarm(coin, market, dayLoop)
                .stream()
                .filter(c -> {
                    LocalDateTime userLocalDateTime = DateTimeUtil.longToLocalDateTime(DateTimeUtil.dateTimeToLong(now) + c.getTimeDifference());
                    return userLocalDateTime.getHour() == 0;
                })
                .map(UserDto::new)
                .collect(Collectors.toList());

    }
}
