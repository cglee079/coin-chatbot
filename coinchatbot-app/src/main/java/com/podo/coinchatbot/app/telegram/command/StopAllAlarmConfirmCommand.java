package com.podo.coinchatbot.app.telegram.command;


import com.podo.coinchatbot.core.Language;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StopAllAlarmConfirmCommand implements CommandEnum {
    NULL("", ""),
    YES("예", "Yes"),
    NO("아니오", "No");

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

    public static StopAllAlarmConfirmCommand from(Language language, String str) {
        return CommandEnum.from(values(), language, str, StopAllAlarmConfirmCommand.NULL);
    }

}
