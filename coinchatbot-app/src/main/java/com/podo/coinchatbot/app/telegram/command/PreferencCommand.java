package com.podo.coinchatbot.app.telegram.command;


import com.podo.coinchatbot.core.Language;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PreferencCommand implements CommandEnum {
    NULL("", ""),
    LANGUAGE_CONFIG("Language", "Language"),
    LOCALTIME_CONFIG("Time adjust(시차조절)", "Time adjust"),
    OUT("OUT", "OUT");

    private final String kr;
    private final String en;

    @Override
    public String kr() {
        return kr;
    }

    @Override
    public String en() {
        return en;
    }

    public static PreferencCommand from(Language language, String str) {
        return CommandEnum.from(values(), language, str, PreferencCommand.NULL);
    }

}
