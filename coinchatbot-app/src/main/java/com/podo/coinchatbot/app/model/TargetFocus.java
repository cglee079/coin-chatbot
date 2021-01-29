package com.podo.coinchatbot.app.model;

import com.podo.coinchatbot.core.Language;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum TargetFocus {
    UP("이상", "MORE"),
    DOWN("이하", "LESS");

    private final String kr;
    private final String en;

    public String kr() {
        return kr;
    }

    public String en() {
        return en;
    }

    public String getStr(Language language) {
        switch (language) {
            case KR:
                return kr;
            case EN:
                return en;
        }

        return "";
    }

}
