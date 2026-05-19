package com.bekzhanuly.courseplatform.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

/**
 * Request/Response Logging Filter
 * Logs all incoming HTTP requests with method, URI, status and duration
 * Author: Bekzhanuly Nurmukhamed
 */
@Component
@Order(1)
@Slf4j
public class BekzhanulYNurmukhamedRequestLoggingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest   = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String requestId = UUID.randomUUID().toString().substring(0, 8);
        long startTime   = System.currentTimeMillis();

        String method = httpRequest.getMethod();
        String uri    = httpRequest.getRequestURI();
        String query  = httpRequest.getQueryString();
        String ip     = getClientIp(httpRequest);

        log.info("[REQ-{}] --> {} {}{} | IP: {}",
                requestId,
                method,
                uri,
                query != null ? "?" + query : "",
                ip);

        try {
            chain.doFilter(request, response);
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            int status    = httpResponse.getStatus();

            if (status >= 500) {
                log.error("[REQ-{}] <-- {} {} | Status: {} | {}ms",
                        requestId, method, uri, status, duration);
            } else if (status >= 400) {
                log.warn("[REQ-{}] <-- {} {} | Status: {} | {}ms",
                        requestId, method, uri, status, duration);
            } else {
                log.info("[REQ-{}] <-- {} {} | Status: {} | {}ms",
                        requestId, method, uri, status, duration);
            }
        }
    }

    private String getClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
