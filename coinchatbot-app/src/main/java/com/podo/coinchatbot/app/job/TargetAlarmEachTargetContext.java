package com.podo.coinchatbot.app.job;

import com.podo.coinchatbot.app.util.DateTimeUtil;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TargetAlarmEachTargetContext {

    private static ThreadLocal<Integer> sendMessageIndex = new ThreadLocal<>();
    private static ThreadLocal<Map<String, Object>> values = new ThreadLocal<>();

    public static void put(String key, Object value) {
        values.get().put(key, value);
    }

    public static void putDateTime(String key, LocalDateTime localDateTime) {
        values.get().put(key, DateTimeUtil.toFullContextString(localDateTime));
    }

    public static void removeAll() {
        values.set(new HashMap<>());
    }

    public static Map<String, Object> toLog() {
        return values.get();
    }

    public static void init() {
        Map<String, Object> valuesInit = new HashMap<>();
        valuesInit.put("id", "target-alarm-each-target" + UUID.randomUUID());
        valuesInit.put("type", "target-alarm-each-target");
        values.set(valuesInit);
        sendMessageIndex.set(0);
    }

    public static void putSendMessage(String message) {
        values.get().put("sendMessage-" + sendMessageIndex.get(), message);
        sendMessageIndex.set(sendMessageIndex.get() + 1);
    }

}
