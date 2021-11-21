package com.podo.coinchatbot.property;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BotConfig {
    private String token;
    private String username;
    private String url;
    private Boolean enabled;
}
