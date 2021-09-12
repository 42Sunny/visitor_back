package com.ftseoul.visitor.filter;

import com.ftseoul.visitor.util.MultiReadableHttpServletRequest;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Map;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class MultiReadableHttpServletRequestFilter implements Filter {

    public void init(FilterConfig filterConfig) {

    }


    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {
        MultiReadableHttpServletRequest multiRequest = new MultiReadableHttpServletRequest((HttpServletRequest) request);
        chain.doFilter(multiRequest, response);
    }

    public void destroy() {

    }
}
