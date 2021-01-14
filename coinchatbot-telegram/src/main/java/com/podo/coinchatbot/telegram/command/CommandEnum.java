package com.podo.coinchatbot.telegram.command;


import com.podo.coinchatbot.core.Lang;

public interface CommandEnum {
    String kr();

    String en();

    static <E extends Enum<E> & CommandEnum> E from(E[] values, Lang lang, String str, E defalutValue) {
        E rs = null;

        if (lang == null) {
            return null;
        }

        switch (lang) {
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

    default String getCommand(Lang lang) {
        String cmd = "";
        switch (lang) {
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
