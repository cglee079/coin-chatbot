package com.podo.coinchatbot.admin.api;

import com.podo.coinchatbot.admin.service.UserSuggestService;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class UserSuggestApi {

    private final UserSuggestService userSuggestService;

    /* 사용자 제안 보기, 데이터 갱신, Ajax*/
    @ResponseBody
    @RequestMapping(value = "/client-suggests/records", method= RequestMethod.GET)
    public String doClientSuggestList(@RequestParam Map<String,Object> map ){
        List<ClientSuggestVo> clientMsgs = userSuggestService.paging(map);
        int count = userSuggestService.count(map);
        Gson gson = new Gson();
        JSONArray data = new JSONArray(gson.toJson(clientMsgs));

        JSONObject resultMap = new JSONObject();
        resultMap.put("rows", data);
        resultMap.put("total", count);

        return resultMap.toString();
    }

    /* 사용자 제안 삭제, Ajax */
    @ResponseBody
    @RequestMapping(value = "/client-suggests/{seq}", method=RequestMethod.DELETE)
    public String doClientDelete(@PathVariable int seq){
        userSuggestService.delete(seq);
        return new JSONObject().put("result", true).toString();
    }
}
