package com.podo.coinchatbot.app.job;

import com.podo.coinchatbot.app.client.market.MarketApiClient;
import com.podo.coinchatbot.app.util.DateTimeUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class MarketRefreshContext {

    private Map<String, Object> values = new HashMap<>();
    private Integer exceptionIndex = 0;

    public MarketRefreshContext() {
        values.put("id", "market-refresh-job-" + UUID.randomUUID());
        values.put("type", "market-refresh-job");
    }

    public Map<String, Object> toLog() {
        return values;
    }

    public void putException(Exception e) {
        values.put("exceptionMessage-" + exceptionIndex, e.getMessage());
        values.put("stackTrace-" + exceptionIndex, Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.joining("\n")));
        exceptionIndex++;
    }

    public void put(String key, Object value) {
        values.put(key, value);
    }

    public String getId() {
        return (String) values.get("id");
    }

    public void putDateTime(String key, LocalDateTime value) {
        values.put(key, DateTimeUtil.toFullContextString(value));
    }
}
