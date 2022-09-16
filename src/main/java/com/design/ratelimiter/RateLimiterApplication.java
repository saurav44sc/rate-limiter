package com.design.ratelimiter;

import com.design.ratelimiter.interceptor.RateLimiterInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class RateLimiterApplication implements WebMvcConfigurer {

    @Autowired
    @Lazy
    private RateLimiterInterceptor interceptor;

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptor).addPathPatterns("/api/rate-limiter/**");
    }

    public static void main(String[] args) {
        SpringApplication.run(RateLimiterApplication.class, args);
    }

}
