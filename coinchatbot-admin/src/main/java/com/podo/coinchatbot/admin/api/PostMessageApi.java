package com.podo.coinchatbot.admin.api;

import com.podo.coinchatbot.admin.dto.MessageSendRequest;
import com.podo.coinchatbot.admin.service.PostMessageService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class PostMessageApi {

    private final PostMessageService postMessageService;

    @ResponseBody
    @PostMapping(value = "/api/messages/post")
    public JSONObject doPostMessage(@RequestBody MessageSendRequest messageSendRequest){
        return postMessageService.send(messageSendRequest);
    }

}
