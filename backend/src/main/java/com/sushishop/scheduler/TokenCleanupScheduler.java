package com.sushishop.scheduler;

import com.sushishop.token.TokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenCleanupScheduler {

    private final TokenRepository tokenRepository;

    @Scheduled(cron = "0 0 3 * * *")
    @Transactional
    public void cleanupExpiredTokens() {
        // Used and invalidated tokens are never valid again, so expired ones of
        // any state can go; otherwise the table only ever grows.
        int deleted = tokenRepository.deleteAllByExpiryDateBefore(LocalDateTime.now());
        log.info("Cleaned up {} expired tokens", deleted);
    }
}
