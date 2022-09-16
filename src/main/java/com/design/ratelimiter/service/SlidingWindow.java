package com.design.ratelimiter.service;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

public class SlidingWindow {

    private final int requestLimit;
    private final int windowTimeInSec;
    private final Queue<Long> requestTimeStamps;

    public SlidingWindow(int requestLimit, int windowTimeInSec) {
        this.requestLimit = requestLimit;
        this.windowTimeInSec = windowTimeInSec;
        this.requestTimeStamps = new ConcurrentLinkedDeque<>();
    }

    public int getRemainingRequests() {
        return requestLimit - requestTimeStamps.size();
    }

    public int getRequestWaitTime() {
        long currentTimeStamp = System.currentTimeMillis() / 1000;
        int initialElapsedTime = (int) (currentTimeStamp - requestTimeStamps.peek());
        return initialElapsedTime > windowTimeInSec ? 0 : windowTimeInSec - initialElapsedTime;
    }

    public boolean isServiceCallAllowed() {
        long currentTimeStamp = System.currentTimeMillis() / 1000;
        evictOlderRequestTimeStamps(currentTimeStamp);
        if (requestTimeStamps.size() >= this.requestLimit) {
            return false;
        }
        requestTimeStamps.add(currentTimeStamp);
        return true;
    }

    public void evictOlderRequestTimeStamps(long currentTimeStamp) {
        while (!requestTimeStamps.isEmpty() &&
                (currentTimeStamp - requestTimeStamps.peek() > windowTimeInSec)) {
            requestTimeStamps.remove();
        }
    }

}
