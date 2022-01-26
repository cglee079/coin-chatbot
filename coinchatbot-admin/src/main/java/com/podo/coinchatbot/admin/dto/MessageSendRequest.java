package com.podo.coinchatbot.admin.dto;

import lombok.Getter;

@Getter
public class MessageSendRequest {

    private Long userId;
    private String messageText;

}
