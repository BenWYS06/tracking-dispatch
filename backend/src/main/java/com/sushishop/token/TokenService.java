package com.sushishop.token;

import com.sushishop.mail.MailService;
import com.sushishop.shared.enums.TokenType;
import com.sushishop.shared.exception.core.BadRequestException;
import com.sushishop.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Issues and validates one-time tokens for email verification and password reset.
 * Tokens are random UUIDs that expire after {@value #TOKEN_EXPIRATION_HOURS} hour(s).
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TokenService {

    private static final int TOKEN_EXPIRATION_HOURS = 1;

    private final TokenRepository tokenRepository;
    private final MailService mailService;

    /** Creates an email verification token and emails the link to the user. */
    @Transactional
    public Token createVerificationToken(User user) {
        var token = Token.builder()
                .token(UUID.randomUUID().toString())
                .tokenType(TokenType.EMAIL_VERIFICATION)
                .user(user)
                .expiryDate(LocalDateTime.now().plusHours(TOKEN_EXPIRATION_HOURS))
                .build();
        tokenRepository.save(token);
        mailService.sendVerificationEmail(user.getEmail(), token.getToken());
        return token;
    }

    /**
     * Creates a password reset token and emails the link to the user.
     * Any earlier reset tokens for this user are invalidated first.
     */
    @Transactional
    public Token createPasswordResetToken(User user) {
        tokenRepository.invalidateAllByUserAndType(user.getId(), TokenType.PASSWORD_RESET);

        var token = Token.builder()
                .token(UUID.randomUUID().toString())
                .tokenType(TokenType.PASSWORD_RESET)
                .user(user)
                .expiryDate(LocalDateTime.now().plusHours(TOKEN_EXPIRATION_HOURS))
                .build();
        tokenRepository.save(token);
        mailService.sendPasswordResetEmail(user.getEmail(), token.getToken());
        return token;
    }

    /**
     * Looks up a token and checks it is unused, not expired and of the expected type.
     *
     * @throws BadRequestException if any check fails
     */
    @Transactional(readOnly = true)
    public Token validateAndGetToken(String tokenValue, TokenType expectedType) {
        var tokenEntity = tokenRepository.findByToken(tokenValue)
                .orElseThrow(() -> new BadRequestException("Invalid or expired token"));

        // Check the type first so a token of another kind (e.g. a reset token
        // sent to the verify endpoint) doesn't reveal whether it's used/expired.
        if (tokenEntity.getTokenType() != expectedType) {
            throw new BadRequestException("Invalid token type");
        }

        if (tokenEntity.isUsed()) {
            throw new BadRequestException("Token already used");
        }

        if (tokenEntity.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Token expired");
        }

        return tokenEntity;
    }

    @Transactional
    public void invalidateAllByUserAndType(Long userId, TokenType type) {
        tokenRepository.invalidateAllByUserAndType(userId, type);
    }
}