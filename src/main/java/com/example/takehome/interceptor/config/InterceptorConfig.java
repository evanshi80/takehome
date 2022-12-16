package com.example.takehome.interceptor.config;

import com.example.takehome.interceptor.RateLimitInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by:   Yufan Shi
 * Date:         Dec.15 2022
 * Description:  InterceptorConfig class is to configure the interceptor
 * to intercept the request and response.
 */
@Configuration
public class InterceptorConfig extends WebMvcConfigurerAdapter {
    private RateLimitInterceptor rateLimitInterceptor;

    // inject the RateLimitInterceptor‘s instance
    @Autowired
    public InterceptorConfig(RateLimitInterceptor rateLimitInterceptor) {
        this.rateLimitInterceptor = rateLimitInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // add the interceptor to the registry‘s interceptor list
        registry.addInterceptor(rateLimitInterceptor);
    }
}

