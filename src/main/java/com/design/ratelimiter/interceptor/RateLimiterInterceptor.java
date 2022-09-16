package com.design.ratelimiter.interceptor;

import com.design.ratelimiter.dao.RateLimiterDao;
import com.design.ratelimiter.service.SlidingWindow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class RateLimiterInterceptor implements HandlerInterceptor {

    private static final String HEADER_USER_ID = "User-Id";
    private static final String HEADER_LIMIT_REMAINING = "X-Rate-Limit-Remaining";
    private static final String HEADER_RETRY_AFTER = "X-Rate-Limit-Retry-After-Seconds";
    private static final String TOO_MANY_REQUEST = "Too many request, Please try after some time.";

    @Autowired
    private RateLimiterDao rateLimiterDao;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {

        String userId = request.getHeader(HEADER_USER_ID);
        if (ObjectUtils.isEmpty(userId)) {
            response.sendError(HttpStatus.BAD_REQUEST.value(), "Missing Request Header: " + HEADER_USER_ID);
            return false;
        }
        SlidingWindow slidingWindow = rateLimiterDao.getDataById(userId);
        if (!slidingWindow.isServiceCallAllowed()) {
            int waitTime = slidingWindow.getRequestWaitTime();
            response.addHeader(HEADER_RETRY_AFTER, String.valueOf(waitTime));
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.sendError(HttpStatus.TOO_MANY_REQUESTS.value(), TOO_MANY_REQUEST);
            return false;
        }
        response.addHeader(HEADER_LIMIT_REMAINING, String.valueOf(slidingWindow.getRemainingRequests()));
        return true;
    }
}
