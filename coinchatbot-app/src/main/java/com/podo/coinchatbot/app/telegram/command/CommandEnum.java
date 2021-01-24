package com.podo.coinchatbot.app.telegram.command;


import com.podo.coinchatbot.core.Language;

public interface CommandEnum {
    String kr();

    String en();

    static <E extends Enum<E> & CommandEnum> E from(E[] values, Language language, String str, E defalutValue) {
        E rs = null;

        if (language == null) {
            return null;
        }

        switch (language) {
            case KR:
                for (E b : values) {
                    if (b.kr().equalsIgnoreCase(str)) {
                        return b;
                    }
                }
                break;
            case EN:
                for (E b : values) {
                    if (b.en().equalsIgnoreCase(str)) {
                        return b;
                    }
                }
                break;
        }

        if (rs == null) {
            rs = defalutValue;
        }

        return rs;
    }

    default String getCommand(Language language) {
        String cmd = "";
        switch (language) {
            case KR:
                cmd = this.kr();
                break;
            case EN:
                cmd = this.en();
                break;
        }

        return cmd;
    }

}
