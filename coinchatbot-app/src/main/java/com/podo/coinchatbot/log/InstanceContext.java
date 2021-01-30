package com.podo.coinchatbot.log;

import com.podo.coinchatbot.app.util.DateTimeUtil;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
public class InstanceContext {

    private Map<String, Object> values = new HashMap<>();
    private Integer exceptionIndex = 0;

    public InstanceContext(String type) {
        values.put("id", type + "-" + UUID.randomUUID());
        values.put("type", type);
    }

    public Map<String, Object> toLog() {
        return values;
    }

    public void putException(Exception e) {
        values.put("exception.message-" + exceptionIndex, e.getMessage());
        values.put("exception.stackTrace-" + exceptionIndex, Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.joining("\n")));
        exceptionIndex++;
    }

    public void put(String key, Object value) {
        values.put(key, value);
    }

    public String getId() {
        return (String) values.get("id");
    }
}
