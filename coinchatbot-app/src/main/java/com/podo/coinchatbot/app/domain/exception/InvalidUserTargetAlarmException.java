package com.podo.coinchatbot.app.domain.exception;

public class InvalidUserTargetAlarmException extends RuntimeException {
    public InvalidUserTargetAlarmException(Long id) {
        super("찾을 수 없는 사용자 알림입니다. id : " + id);
    }
}
