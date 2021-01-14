package com.podo.coinchatbot.telegram.config;


import com.podo.coinchatbot.telegram.menu.MenuHandler;
import com.podo.coinchatbot.telegram.model.Menu;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

@RequiredArgsConstructor
@Configuration
public class TelegramConfig {

    private final List<MenuHandler> menuHandlers;

    @Bean
    public Map<Menu, MenuHandler> menuHandlers() {
        return menuHandlers.stream()
                .collect(toMap(MenuHandler::getHandleMenu, x -> x));
    }

}
