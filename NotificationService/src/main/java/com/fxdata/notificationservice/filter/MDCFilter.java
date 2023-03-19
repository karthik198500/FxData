package com.fxdata.notificationservice.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@Component
public class MDCFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(MDCFilter.class);

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            UUID uniqueId = UUID.randomUUID();
            MDC.put("requestId", uniqueId.toString());
            logger.debug("Request IP address is {}", servletRequest.getRemoteAddr()+" for requestId "+uniqueId);
            HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
            ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(
                    httpServletResponse
            );
            filterChain.doFilter(servletRequest, responseWrapper);
            responseWrapper.setHeader("requestId", uniqueId.toString());
            responseWrapper.copyBodyToResponse();
            logger.debug("Response header is set with uuid {}", responseWrapper.getHeader("requestId"));
        } finally {
            MDC.clear();
        }
    }
}

