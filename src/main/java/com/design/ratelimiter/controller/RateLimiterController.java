package com.design.ratelimiter.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/rate-limiter")
public class RateLimiterController {

    @GetMapping(value = "/test")
    public ResponseEntity restLimiterTest() {
        return ResponseEntity.ok("Application is accessible.");
    }

}
