package com.podo.coinchatbot.app.domain.service;

import com.podo.coinchatbot.app.domain.dto.CoinInformationDto;
import com.podo.coinchatbot.property.CoinConfigs;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CoinInformationService {

    private final List<CoinInformationDto> coinInformations;

    public CoinInformationService(CoinConfigs coinConfigs) {
        coinInformations = coinConfigs.getProperties()
                .stream()
                .filter(coinProperty -> coinProperty.getBotConfig().getEnabled())
                .map(property -> new CoinInformationDto(property.getCoin(), property.getBotConfig().getUrl()))
                .collect(Collectors.toList());
    }

    public List<CoinInformationDto> getAll() {
        return coinInformations;
    }
}
