package com.example.takehome.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * In order to not overwhelm the server,
 * add a rate limit of 5 requests per sec for unauthenticated users
 * and 20 requests per second for authenticated users.
 */
@Component
@Slf4j
public class RateLimitInterceptor implements HandlerInterceptor {

    private Map<String, AtomicInteger> requestCount = new ConcurrentHashMap<>();
    private Map<String, Long> lastRequestTime = new ConcurrentHashMap<>();

    // Get the value from application.properties,default value is 5
    @Value("${rate.limit.unauthenticated:5}")
    private int unauthenticatedRateLimit;
    // Get the value from application.properties,default value is 20
    @Value("${rate.limit.authenticated:20}")
    private int authenticatedRateLimit;

    // Implement the preHandle method of HandlerInterceptor
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        // Get the IP address of the client
        String ipAddress = request.getRemoteAddr();
        //  Get the current time
        long currentTime = System.currentTimeMillis();
        // Get the last request time
        long lastRequest = lastRequestTime.getOrDefault(ipAddress, 0L);

        // New Second starts
        if (currentTime - lastRequest > 1000) {
            requestCount.put(ipAddress, new AtomicInteger(0));
            log.info("New second for ip address: " + ipAddress);
        }
        // Get the request count for the current second
        AtomicInteger count = requestCount.get(ipAddress);

        // Simple way to check if the user is authenticated,
        // if so, use the authenticated rate limit,
        // otherwise use the unauthenticated rate limit
        // Remember to use real authentication in production
        int limit = request.getHeader("Authorization") == null ?
                unauthenticatedRateLimit : authenticatedRateLimit;
        log.info("Rate limit is: " + limit);
        // Check if the request count exceeds the limit
        // if so, return false to reject the request
        // otherwise, increment the request count and return true to accept the request
        if (count.get() < limit) {
            log.info("Request count not exceed the limit. Accepting request from ip address: " + ipAddress);
            count.incrementAndGet();
            lastRequestTime.put(ipAddress, currentTime);
            return true;
        } else {
            log.info("Request count exceed the limit. Rejecting request from ip address: " + ipAddress);
            // Set the response status code to 429
            response.setStatus(429);
            return false;
        }
    }
}