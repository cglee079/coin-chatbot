package com.podo.coinchatbot.app.job;

import com.podo.coinchatbot.app.client.market.MarketApiClient;
import lombok.RequiredArgsConstructor;
import net.logstash.logback.argument.StructuredArguments;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Component
public class MarketRefreshJob implements Job {

    private final List<MarketApiClient> marketApiClients;
    private static final Logger LOGGER = LoggerFactory.getLogger("JOB_LOGGER");

    @Scheduled(cron = "0 0/1 * * * *")
    @Override
    public void run() {
        MarketRefreshContext marketRefreshContext = new MarketRefreshContext();
        marketRefreshContext.putDateTime("jobStartAt", LocalDateTime.now());

        try {
            for (MarketApiClient marketApiClient : marketApiClients) {
                marketApiClient.refresh();
            }
        } catch (Exception e) {
            marketRefreshContext.putException(e);
        } finally {
            marketRefreshContext.putDateTime("jobEndAt", LocalDateTime.now());
            LOGGER.info("", StructuredArguments.value("context", marketRefreshContext.toLog()));
        }
    }
}
