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

    public static final String HEADER_MAP_MDC = "HEADER_MAP_MDC";

    public static final String BODY_CONTENT_MDC = "BODY_CONTENT_MDC";

    public static final String PARAMETER_MAP_MDC = "PARAMETER_MAP_MDC";

    public static final String REQUEST_URI_MDC = "REQUEST_URI_MDC";

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
            log.info("Failed to convert Json : {}", e.getMessage());
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

}
