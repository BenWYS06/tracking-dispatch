package com.sushishop.scheduler;

import com.sushishop.shared.ratelimit.RateLimitService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Periodically drops rate-limit buckets that aren't currently throttling a
 * client, so the in-memory map doesn't keep one entry per IP forever.
 */
@Component
@RequiredArgsConstructor
public class RateLimitCleanupScheduler {

    private final RateLimitService rateLimitService;

    @Scheduled(fixedRate = 5, timeUnit = TimeUnit.MINUTES)
    public void cleanupExpiredBuckets() {
        rateLimitService.cleanupBuckets();
    }
}
