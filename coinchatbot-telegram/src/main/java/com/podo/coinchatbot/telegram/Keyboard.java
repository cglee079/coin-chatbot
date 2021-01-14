package com.podo.coinchatbot.telegram;

import com.podo.coinchatbot.core.Lang;
import com.podo.coinchatbot.core.Market;
import com.podo.coinchatbot.telegram.command.MarketCommand;
import com.podo.coinchatbot.telegram.keyboard.ConfirmStopKeyboard;
import com.podo.coinchatbot.telegram.keyboard.DeleteTargetAlertKeyboard;
import com.podo.coinchatbot.telegram.keyboard.MainEnKeyboard;
import com.podo.coinchatbot.telegram.keyboard.MainKrKeyboard;
import com.podo.coinchatbot.telegram.keyboard.PreferenceKeyboard;
import com.podo.coinchatbot.telegram.keyboard.SetDayloopAlertKeyboard;
import com.podo.coinchatbot.telegram.keyboard.SetLanguageKeyboard;
import com.podo.coinchatbot.telegram.keyboard.SetMarketKeyboard;
import com.podo.coinchatbot.telegram.keyboard.SetTargetAlertKeyboard;
import com.podo.coinchatbot.telegram.keyboard.SetTimeloopAlertKeyboard;
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
    private static final Map<Lang, ReplyKeyboardMarkup> LANG_TO_MAIN_KEYBOARD;
    private static final Map<Lang, SetDayloopAlertKeyboard> LANG_TO_SET_DAYLOOP_ALERT_KEYBOARD;
    private static final Map<Lang, SetTimeloopAlertKeyboard> LANG_TO_SET_TIMELOOP_ALERT_KEYBOARD;
    private static final Map<Lang, SetLanguageKeyboard> LANG_TO_SET_LANGUAGE_KEYBOARD;
    private static final Map<Lang, ConfirmStopKeyboard> LANG_TO_CONFIRM_STOP_KEYBOARD;
    private static final Map<Lang, PreferenceKeyboard> LANG_TO_PREFERENCE_KEYBOARD;
    private static final Map<Lang, SetTargetAlertKeyboard> LANG_TO_SET_TARGET_ALERT_KEYBOARD;

    static {

        DEFAULT_KEYBOARD = new ReplyKeyboardRemove();
        LANG_TO_MAIN_KEYBOARD = new EnumMap<>(Lang.class);
        LANG_TO_SET_DAYLOOP_ALERT_KEYBOARD = new EnumMap<>(Lang.class);
        LANG_TO_SET_TIMELOOP_ALERT_KEYBOARD = new EnumMap<>(Lang.class);
        LANG_TO_SET_LANGUAGE_KEYBOARD = new EnumMap<>(Lang.class);
        LANG_TO_CONFIRM_STOP_KEYBOARD = new EnumMap<>(Lang.class);
        LANG_TO_PREFERENCE_KEYBOARD = new EnumMap<>(Lang.class);
        LANG_TO_SET_TARGET_ALERT_KEYBOARD = new EnumMap<>(Lang.class);

        LANG_TO_MAIN_KEYBOARD.put(Lang.KR, new MainKrKeyboard(Lang.KR));
        LANG_TO_MAIN_KEYBOARD.put(Lang.EN, new MainEnKeyboard(Lang.EN));

        LANG_TO_SET_DAYLOOP_ALERT_KEYBOARD.put(Lang.KR, new SetDayloopAlertKeyboard(Lang.KR));
        LANG_TO_SET_DAYLOOP_ALERT_KEYBOARD.put(Lang.EN, new SetDayloopAlertKeyboard(Lang.EN));

        LANG_TO_SET_TIMELOOP_ALERT_KEYBOARD.put(Lang.KR, new SetTimeloopAlertKeyboard(Lang.KR));
        LANG_TO_SET_TIMELOOP_ALERT_KEYBOARD.put(Lang.EN, new SetTimeloopAlertKeyboard(Lang.EN));

        LANG_TO_SET_LANGUAGE_KEYBOARD.put(Lang.KR, new SetLanguageKeyboard(Lang.KR));
        LANG_TO_SET_LANGUAGE_KEYBOARD.put(Lang.EN, new SetLanguageKeyboard(Lang.EN));

        LANG_TO_CONFIRM_STOP_KEYBOARD.put(Lang.KR, new ConfirmStopKeyboard(Lang.KR));
        LANG_TO_CONFIRM_STOP_KEYBOARD.put(Lang.EN, new ConfirmStopKeyboard(Lang.EN));

        LANG_TO_PREFERENCE_KEYBOARD.put(Lang.KR, new PreferenceKeyboard(Lang.KR));
        LANG_TO_PREFERENCE_KEYBOARD.put(Lang.EN, new PreferenceKeyboard(Lang.EN));

        LANG_TO_SET_TARGET_ALERT_KEYBOARD.put(Lang.KR, new SetTargetAlertKeyboard(Lang.KR));
        LANG_TO_SET_TARGET_ALERT_KEYBOARD.put(Lang.EN, new SetTargetAlertKeyboard(Lang.EN));
    }

    public static ReplyKeyboardRemove defaultKeyboard() {
        return DEFAULT_KEYBOARD;
    }

    public static ReplyKeyboardRemove defaultKeyboard(Lang lang) {
        return DEFAULT_KEYBOARD;
    }

    public static ReplyKeyboardMarkup mainKeyboard(Lang lang) {
        return LANG_TO_MAIN_KEYBOARD.get(lang);
    }

    public static SetDayloopAlertKeyboard setDayloopAlertKeyboard(Lang lang) {
        return LANG_TO_SET_DAYLOOP_ALERT_KEYBOARD.get(lang);
    }

    public static SetTimeloopAlertKeyboard setTimeloopAlertKeyboard(Lang lang) {
        return LANG_TO_SET_TIMELOOP_ALERT_KEYBOARD.get(lang);
    }

    public static SetMarketKeyboard setMarketKeyboard(Lang lang, List<Market> enabledMarketIds) {

            final List<MarketCommand> enabledMarketCommands = enabledMarketIds.stream()
                    .map(MarketCommand::from)
                    .collect(Collectors.toList());

        return new SetMarketKeyboard(enabledMarketCommands, lang);
    }

    public static SetLanguageKeyboard setLanguageKeyboard(Lang lang) {
        return LANG_TO_SET_LANGUAGE_KEYBOARD.get(lang);
    }

    public static ConfirmStopKeyboard confirmStopKeyboard(Lang lang) {
        return LANG_TO_CONFIRM_STOP_KEYBOARD.get(lang);
    }

    public static PreferenceKeyboard preferenceKeyboard(Lang lang) {
        return LANG_TO_PREFERENCE_KEYBOARD.get(lang);
    }

    public static SetTargetAlertKeyboard setTargetKeyboard(Lang lang) {
        return LANG_TO_SET_TARGET_ALERT_KEYBOARD.get(lang);
    }

    public static ReplyKeyboard deleteTargetKeyboard(List<String> targetCommands, Lang lang) {
        return new DeleteTargetAlertKeyboard(targetCommands, lang);
    }

}
