package com.podo.coinchatbot.app.client;

import com.google.common.base.Strings;
import com.podo.coinchatbot.app.client.model.ApiCallResult;
import lombok.experimental.UtilityClass;
import net.logstash.logback.argument.StructuredArgument;
import net.logstash.logback.argument.StructuredArguments;
import org.glassfish.grizzly.http.Method;
import org.glassfish.jersey.server.Uri;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URL;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@UtilityClass
public class ApiCaller {

    private static final Logger LOGGER = LoggerFactory.getLogger("CLIENT_LOGGER");

    public static ApiCallResult callGetApi(String url) {
        ClientContext clientContext = new ClientContext();

        HashMap<String, Object> request = new HashMap<>();
        request.put("url", url);
        request.put("method", Method.GET.toString());
        request.put("queryString", getQueryString(url));
        request.put("host", getHost(url));

        clientContext.put("request", request);

        RestTemplate restTemplate = new RestTemplate();

        try {
            String responseBody = restTemplate.getForObject(url, String.class);
            clientContext.put("response", responseBody);
            return new ApiCallResult(true, "", responseBody);
        } catch (Exception e) {
            clientContext.putException(e);
            return new ApiCallResult(false, e.getMessage());
        } finally {
            LOGGER.info("", StructuredArguments.value("context", clientContext.toLog()));
        }
    }

    private static String getHost(String url) {
        return UriComponentsBuilder.fromUriString(url).build().getHost();
    }

    public static String getQueryString(String url) {
        return UriComponentsBuilder.fromUriString(url).build().getQuery();
    }

}

