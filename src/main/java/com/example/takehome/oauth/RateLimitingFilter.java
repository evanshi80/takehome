package com.example.takehome.oauth;

import com.google.common.util.concurrent.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@Slf4j
public class RateLimitingFilter extends OncePerRequestFilter {

    private final RateLimiter authenticatedRateLimiter;
    private final RateLimiter unauthenticatedRateLimiter;

    public RateLimitingFilter(double authenticatedRequestsPerSecond, double unauthenticatedRequestsPerSecond) {
        this.authenticatedRateLimiter = RateLimiter.create(authenticatedRequestsPerSecond);
        this.unauthenticatedRateLimiter = RateLimiter.create(unauthenticatedRequestsPerSecond);
        log.info( "RateLimitingFilter: authenticatedRequestsPerSecond: " + authenticatedRequestsPerSecond + " " +
                "unauthenticatedRequestsPerSecond: " + unauthenticatedRequestsPerSecond);
    }


    private RateLimiter getRateLimiter(HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authenticatedRateLimiter;
        }
        return unauthenticatedRateLimiter;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        RateLimiter rateLimiter = getRateLimiter(request);
        log.info("Rate limiter: {}", rateLimiter);
        if (!rateLimiter.tryAcquire()) {
            response.sendError(429, "Too many requests");
            return;
        }
        filterChain.doFilter(request, response);
    }
}
