package com.ftseoul.visitor.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.slf4j.spi.MDCAdapter;

@Slf4j
public class MDCUtil {
    private static MDCAdapter mdc = MDC.getMDCAdapter();

    private static ObjectMapper objectMapper = new ObjectMapper();

    public static final String ID = "REQUEST_ID";

    public static final String HEADER_MAP_MDC = "HEADER_MAP_MDC";

    public static final String BODY_CONTENT_MDC = "BODY_CONTENT_MDC";

    public static final String PARAMETER_MAP_MDC = "PARAMETER_MAP_MDC";

    public static final String QUERY_STRING_MDC = "QUERY_STRING_MDC";

    public static final String REQUEST_URI_MDC = "REQUEST_URI_MDC";

    public static final String COOKIE_MDC = "COOKIE_MDC";

    public static void set(String key, String value) {
        mdc.put(key, value);
    }

    public static void setJsonValue(String key, Object value) {
        try {
            if (value != null) {
                ObjectMapper mapper = new ObjectMapper();
                String json = mapper.writeValueAsString(value);
                mdc.put(key, json);
            }
        } catch (Exception e) {
            log.warn("Failed to convert Json : {}", e.getMessage());
        }
    }

    public static ObjectMapper getMapper() {
        return MDCUtil.objectMapper;
    }

    public static <T> T fromJson(String jsonStr, Class<T> cls) {
        try {
            return getMapper().readValue(jsonStr, cls);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String toPrettyJson(String json) {
        Object jsonObject = MDCUtil.fromJson(json, Object.class);
        try {
            return getMapper().writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);
        } catch (JsonProcessingException e) {
            log.info("Failed to parse Json : {}", e.getMessage());
        }
        return "";
    }

    public static String get(String key) {
        return mdc.get(key);
    }

    public static String generateRequestLog() {
        StringBuilder sb = new StringBuilder();
        sb.append("Request Info");
        sb.append(" UUID: " + MDCUtil.get(ID));
        sb.append(" Headers : " + MDCUtil.get(HEADER_MAP_MDC));
        sb.append(" Body Content " + MDCUtil.get(BODY_CONTENT_MDC));
        sb.append(" Parameters: " + MDCUtil.get(PARAMETER_MAP_MDC));
        sb.append(" Query String: " + MDCUtil.get(QUERY_STRING_MDC));
        sb.append(" Request URI: " + MDCUtil.get(REQUEST_URI_MDC));
        sb.append(" Cookie: " + MDCUtil.get(COOKIE_MDC));
        return sb.toString();
    }
}
