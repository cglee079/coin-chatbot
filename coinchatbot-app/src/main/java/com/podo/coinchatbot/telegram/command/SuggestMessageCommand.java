package com.podo.coinchatbot.telegram.command;


import com.podo.coinchatbot.core.Language;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SuggestMessageCommand implements CommandEnum {
    NULL("", ""),
    OUT("0", "0");

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

    public static SuggestMessageCommand from(Language language, String str) {
        return CommandEnum.from(values(), language, str, SuggestMessageCommand.NULL);
    }

}
