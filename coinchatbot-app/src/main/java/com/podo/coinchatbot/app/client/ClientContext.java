package com.podo.coinchatbot.app.client;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class ClientContext {

    private Map<String, Object> values = new HashMap<>();

    public ClientContext() {
        this.values.put("id", "api-client-" + UUID.randomUUID());
        this.values.put("type", "api-client");
    }

    public void put(String key, Object value) {
        this.values.put(key, value);
    }

    public void putException(Exception e) {
        values.put("exceptionMessage", e.getMessage());
        values.put("stackTrace" , Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.joining("\n")));
    }

    public Map<String, Object> toLog() {
        return values;
    }
}
