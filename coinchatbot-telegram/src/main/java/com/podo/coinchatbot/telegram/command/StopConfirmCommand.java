package com.podo.coinchatbot.telegram.command;


import com.podo.coinchatbot.core.Lang;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StopConfirmCommand implements CommandEnum {
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

    public static StopConfirmCommand from(Lang lang, String str) {
        return CommandEnum.from(values(), lang, str, StopConfirmCommand.NULL);
    }

}
