package com.podo.coinchatbot.external;

import com.podo.coinchatbot.external.model.ApiCallResult;
import com.podo.coinchatbot.util.DateTimeUtil;
import com.podo.coinchatbot.util.JsonUtil;
import com.podo.coinchatbot.log.InstanceContext;
import lombok.experimental.UtilityClass;
import net.logstash.logback.argument.StructuredArguments;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class ApiCaller {


    private static final Logger LOGGER = LoggerFactory.getLogger("EXTERNAL_LOGGER");
    public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36";

    public static ApiCallResult callGetApi(String url) {
        InstanceContext clientContext = new InstanceContext("api-client");

        HttpMethod method = HttpMethod.GET;
        clientContext.put("client.request.at", DateTimeUtil.toFullContextString(LocalDateTime.now()));
        clientContext.put("client.request", getRequest(url, method));

        RestTemplate restTemplate = restTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("user-agent", USER_AGENT);

        try {
            ResponseEntity<String> exchange = restTemplate.exchange(url, method, new HttpEntity<>("", headers), String.class);
            clientContext.put("client.response", getResponse(exchange));
            return new ApiCallResult(true, "", exchange.getBody());
        } catch (Exception e) {
            clientContext.putException(e);
            return new ApiCallResult(false, e.getMessage());
        } finally {
            LOGGER.info("", StructuredArguments.value("context", clientContext.toLog()));
        }
    }

    private static Map<String, Object> getResponse(ResponseEntity<String> exchange) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", exchange.getStatusCodeValue());
        response.put("headers", JsonUtil.toJSON(exchange.getHeaders()));
        return response;
    }

    @NotNull
    private static Map<String, Object> getRequest(String url, HttpMethod method) {
        Map<String, Object> request = new HashMap<>();
        request.put("url", url);
        request.put("method", method.toString());
        request.put("queryString", getQueryString(url));
        request.put("host", getHost(url));
        request.put("headers.userAgent", USER_AGENT);
        return request;
    }

    private static String getHost(String url) {
        return UriComponentsBuilder.fromUriString(url).build().getHost();
    }

    public static String getQueryString(String url) {
        return UriComponentsBuilder.fromUriString(url).build().getQuery();
    }

    private static RestTemplate restTemplate() {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setConnectTimeout(6 * 1000);
        factory.setReadTimeout(6 * 1000);
        return new RestTemplate(factory);
    }

}

