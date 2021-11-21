package com.podo.coinchatbot.log;

import com.podo.coinchatbot.util.DateTimeUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class ThreadLocalContext {

    private static ThreadLocal<Integer> sendMessageIndex = new ThreadLocal<>();
    private static ThreadLocal<Integer> exceptionIndex = new ThreadLocal<>();
    private static ThreadLocal<Map<String, Object>> values = new ThreadLocal<>();

    public static void put(String key, Object value) {
        values.get().put(key, value);
    }

    public static void removeAll() {
        values.set(new HashMap<>());
    }

    public static Map<String, Object> toLog() {
        return values.get();
    }

    public static void init(String type) {
        Map<String, Object> valuesInit = new HashMap<>();
        valuesInit.put("id", type + "-" + UUID.randomUUID());
        valuesInit.put("type", type);
        values.set(valuesInit);
        exceptionIndex.set(0);
        sendMessageIndex.set(0);
    }

    public static void putException(Exception e) {
        values.get().put("exception.message-" + exceptionIndex.get(), e.getMessage());
        values.get().put("exception.stackTrace-" + exceptionIndex.get(), Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.joining("\n")));
        exceptionIndex.set(exceptionIndex.get() + 1);
    }

    public static void putTelegramMessageSend(String message) {
        values.get().put("telegram.message.send.text-" + sendMessageIndex.get(), message);
        sendMessageIndex.set(sendMessageIndex.get() + 1);
    }

    public static String id() {
        return String.valueOf(values.get().get("id"));
    }
}
