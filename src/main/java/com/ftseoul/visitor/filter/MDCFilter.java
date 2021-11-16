package com.ftseoul.visitor.filter;

import com.ftseoul.visitor.util.MDCUtil;
import com.ftseoul.visitor.util.RequestWrapper;
import java.io.IOException;
import java.util.UUID;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

@Slf4j
public class MDCFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {
        // MDC 저장
        RequestWrapper requestWrapper = RequestWrapper.of(request);

        MDCUtil.set(MDCUtil.ID, UUID.randomUUID().toString());

        MDCUtil.setJsonValue(MDCUtil.HEADER_MAP_MDC, requestWrapper.headerMap());

        MDCUtil.setJsonValue(MDCUtil.PARAMETER_MAP_MDC, requestWrapper.parameterMap());

        MDCUtil.setJsonValue(MDCUtil.COOKIE_MDC, requestWrapper.getCookies());

        MDCUtil.setJsonValue(MDCUtil.BODY_CONTENT_MDC, requestWrapper.getBodyContents());

        MDCUtil.set(MDCUtil.REQUEST_URI_MDC, requestWrapper.getRequestUri());


        MDCUtil.set(MDCUtil.QUERY_STRING_MDC, requestWrapper.getQueryString());

        // Request Info 로그 출력
        log.info(MDCUtil.generateRequestLog());

        try {
            chain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
    }
}
