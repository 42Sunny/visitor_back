package com.ftseoul.visitor.filter;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

@Component
@Slf4j
@RefreshScope
public class WAuthFilter {

    RestTemplate restTemplate = new RestTemplate();

    @Value("${wauth.cookiename}")
    private String cookieName;
    @Value("${wauth.X_AUTH_KEY}")
    private String X_AUTH_KEY;
    @Value("${wauth.statusurl}")
    private String statusUrl;

    public boolean isAuthorized(HttpServletRequest httpServletRequest) {
        if (httpServletRequest.getCookies() == null) {
            log.error("쿠키가 존재하지 않습니다.");
            return false;
        }
        Optional<Cookie> w_auth = Arrays.stream(httpServletRequest.getCookies()).
                filter(cookie -> cookie.getName().equals(cookieName)).findFirst();
        if (w_auth.isPresent()) {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add(HttpHeaders.COOKIE, CookieConverter(w_auth.get()));
            log.info(cookieName + " : " + CookieConverter(w_auth.get()));
            httpHeaders.set("x-42cadet-auth-key", X_AUTH_KEY);
            HttpEntity<String> httpEntity = new HttpEntity<>(null, httpHeaders);
            ResponseEntity<JsonNode> exchange = restTemplate.exchange(statusUrl, HttpMethod.GET, httpEntity, JsonNode.class
            );
            if (exchange.getBody() == null)
                return false;
            return exchange.getBody().get("isAdmin").asBoolean();
        } else {
            log.error("어드민 계정으로 다시 시도해주세요.");
            return false;
        }
    }

    private String CookieConverter(Cookie cookie) {
        return ResponseCookie.from(cookie.getName(), cookie.getValue())
                .domain(cookie.getDomain())
                .path(cookie.getPath())
                .maxAge(cookie.getMaxAge())
                .build()
                .toString();
    }
}
