package com.podo.coinchatbot.telegram.message;

import com.podo.coinchatbot.core.Lang;
import com.podo.coinchatbot.telegram.exception.InvalidLanguageException;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CommonMessage {

    public static String warningWaitSecond(Lang lang) {
        switch (lang) {
            case KR:
                return "잠시 후 다시 보내주세요.\n"
            case EN:
                return "Please send message again after a while.\n";
        }

        throw new InvalidLanguageException();
    }

}
