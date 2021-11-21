package com.podo.coinchatbot.telegram.keyboard;

import com.podo.coinchatbot.telegram.command.MarketCommand;
import com.podo.coinchatbot.core.Language;
import com.podo.coinchatbot.core.Market;
import lombok.experimental.UtilityClass;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@UtilityClass
public class Keyboard {

    private static final ReplyKeyboardRemove DEFAULT_KEYBOARD;
    private static final Map<Language, ReplyKeyboardMarkup> LANG_TO_MAIN_KEYBOARD;
    private static final Map<Language, SetDayloopAlertKeyboard> LANG_TO_SET_DAYLOOP_ALERT_KEYBOARD;
    private static final Map<Language, SetTimeloopAlertKeyboard> LANG_TO_SET_TIMELOOP_ALERT_KEYBOARD;
    private static final Map<Language, LanguageConfigKeyboard> LANG_TO_SET_LANGUAGE_KEYBOARD;
    private static final Map<Language, ConfirmStopKeyboard> LANG_TO_CONFIRM_STOP_KEYBOARD;
    private static final Map<Language, PreferenceKeyboard> LANG_TO_PREFERENCE_KEYBOARD;
    private static final Map<Language, TargetAlarmConfigKeyboard> LANG_TO_SET_TARGET_ALERT_KEYBOARD;

    static {

        DEFAULT_KEYBOARD = new ReplyKeyboardRemove();
        LANG_TO_MAIN_KEYBOARD = new EnumMap<>(Language.class);
        LANG_TO_SET_DAYLOOP_ALERT_KEYBOARD = new EnumMap<>(Language.class);
        LANG_TO_SET_TIMELOOP_ALERT_KEYBOARD = new EnumMap<>(Language.class);
        LANG_TO_SET_LANGUAGE_KEYBOARD = new EnumMap<>(Language.class);
        LANG_TO_CONFIRM_STOP_KEYBOARD = new EnumMap<>(Language.class);
        LANG_TO_PREFERENCE_KEYBOARD = new EnumMap<>(Language.class);
        LANG_TO_SET_TARGET_ALERT_KEYBOARD = new EnumMap<>(Language.class);

        LANG_TO_MAIN_KEYBOARD.put(Language.KR, new MainKrKeyboard(Language.KR));
        LANG_TO_MAIN_KEYBOARD.put(Language.EN, new MainEnKeyboard(Language.EN));

        LANG_TO_SET_DAYLOOP_ALERT_KEYBOARD.put(Language.KR, new SetDayloopAlertKeyboard(Language.KR));
        LANG_TO_SET_DAYLOOP_ALERT_KEYBOARD.put(Language.EN, new SetDayloopAlertKeyboard(Language.EN));

        LANG_TO_SET_TIMELOOP_ALERT_KEYBOARD.put(Language.KR, new SetTimeloopAlertKeyboard(Language.KR));
        LANG_TO_SET_TIMELOOP_ALERT_KEYBOARD.put(Language.EN, new SetTimeloopAlertKeyboard(Language.EN));

        LANG_TO_SET_LANGUAGE_KEYBOARD.put(Language.KR, new LanguageConfigKeyboard(Language.KR));
        LANG_TO_SET_LANGUAGE_KEYBOARD.put(Language.EN, new LanguageConfigKeyboard(Language.EN));

        LANG_TO_CONFIRM_STOP_KEYBOARD.put(Language.KR, new ConfirmStopKeyboard(Language.KR));
        LANG_TO_CONFIRM_STOP_KEYBOARD.put(Language.EN, new ConfirmStopKeyboard(Language.EN));

        LANG_TO_PREFERENCE_KEYBOARD.put(Language.KR, new PreferenceKeyboard(Language.KR));
        LANG_TO_PREFERENCE_KEYBOARD.put(Language.EN, new PreferenceKeyboard(Language.EN));

        LANG_TO_SET_TARGET_ALERT_KEYBOARD.put(Language.KR, new TargetAlarmConfigKeyboard(Language.KR));
        LANG_TO_SET_TARGET_ALERT_KEYBOARD.put(Language.EN, new TargetAlarmConfigKeyboard(Language.EN));
    }

    public static ReplyKeyboardRemove defaultKeyboard() {
        return DEFAULT_KEYBOARD;
    }

    public static ReplyKeyboardRemove defaultKeyboard(Language language) {
        return DEFAULT_KEYBOARD;
    }

    public static ReplyKeyboardMarkup mainKeyboard(Language language) {
        return LANG_TO_MAIN_KEYBOARD.get(language);
    }

    public static SetDayloopAlertKeyboard setDayloopAlertKeyboard(Language language) {
        return LANG_TO_SET_DAYLOOP_ALERT_KEYBOARD.get(language);
    }

    public static SetTimeloopAlertKeyboard setTimeloopAlertKeyboard(Language language) {
        return LANG_TO_SET_TIMELOOP_ALERT_KEYBOARD.get(language);
    }

    public static SetMarketKeyboard setMarketKeyboard(Language language, List<Market> enabledMarketIds) {

        final List<MarketCommand> enabledMarketCommands = enabledMarketIds.stream()
                .map(MarketCommand::from)
                .collect(Collectors.toList());

        return new SetMarketKeyboard(enabledMarketCommands, language);
    }

    public static LanguageConfigKeyboard languageConfigKeyboard(Language language) {
        return LANG_TO_SET_LANGUAGE_KEYBOARD.get(language);
    }

    public static ConfirmStopKeyboard confirmStopKeyboard(Language language) {
        return LANG_TO_CONFIRM_STOP_KEYBOARD.get(language);
    }

    public static PreferenceKeyboard preferenceKeyboard(Language language) {
        return LANG_TO_PREFERENCE_KEYBOARD.get(language);
    }

    public static TargetAlarmConfigKeyboard targetAlarmConfigKeyboard(Language language) {
        return LANG_TO_SET_TARGET_ALERT_KEYBOARD.get(language);
    }

    public static ReplyKeyboard deleteTargetKeyboard(List<String> targetCommands, Language language) {
        return new DeleteTargetAlertKeyboard(targetCommands, language);
    }

}
