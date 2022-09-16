package com.design.ratelimiter.dao;

import com.design.ratelimiter.service.SlidingWindow;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimiterDao {

    @Value("${request.limit}")
    private Integer requestLimit;

    @Value("${window.time.in.sec}")
    private Integer windowTimeInSeconds;

    private final Map<String, SlidingWindow> bucket = new ConcurrentHashMap<>();

    public SlidingWindow getDataById(String userId) {
        return bucket.computeIfAbsent(userId, this::resolveData);
    }

    private SlidingWindow resolveData(String userId) {
        if (bucket.containsKey(userId)) {
            return bucket.get(userId);
        }
        return new SlidingWindow(requestLimit, windowTimeInSeconds);
    }

}
