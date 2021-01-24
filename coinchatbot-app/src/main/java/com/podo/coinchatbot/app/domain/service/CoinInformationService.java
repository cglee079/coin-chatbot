package com.podo.coinchatbot.app.domain.service;

import com.podo.coinchatbot.core.Coin;
import com.podo.coinchatbot.app.config.CoinProperties;
import com.podo.coinchatbot.app.domain.dto.CoinInformationDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CoinInformationService {

    private final List<CoinInformationDto> coinInformations;

    public CoinInformationService(CoinProperties coinProperties) {
        coinInformations = coinProperties.getProperties()
                .stream()
                .filter(coinProperty -> coinProperty.getBotConfig().getEnabled())
                .map(property -> new CoinInformationDto(Coin.valueOf(property.getId()), property.getBotConfig().getUrl()))
                .collect(Collectors.toList());
    }

    public List<CoinInformationDto> getAll() {
        return coinInformations;
    }
}
