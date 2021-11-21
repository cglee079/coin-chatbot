package com.podo.coinchatbot.telegram.message;

import com.podo.coinchatbot.telegram.exception.InvalidUserLanguageException;
import com.podo.coinchatbot.core.Language;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CommonMessage {

    public static String toMain(Language language) {
        switch (language) {
            case KR:
                return "\n# 메인화면으로 이동합니다.\n";
            case EN:
                return "\n# Changed to main menu.\n";
            default:
                throw new InvalidUserLanguageException();
        }
    }

    public static String warningWaitSecond(Language language) {
        switch (language) {
            case KR:
                return "잠시 후 다시 보내주세요.\n";
            case EN:
                return "Please send message again after a while.\n";
        }

        throw new InvalidUserLanguageException();
    }

}
