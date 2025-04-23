package app.service;

import app.entity.RefreshToken;
import app.entity.User;
import app.repository.RefreshTokenRepository;
import app.util.exception.auth.InvalidOrExpiredRefreshTokenException;
import liquibase.datatype.core.UUIDType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepo;
    @Value("${refresh-token.expiration-days}")
    private int refreshTokenExpirationDays;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepo) {
        this.refreshTokenRepo = refreshTokenRepo;
    }

    public RefreshToken generate(User user) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setCreatedAt(Instant.now());
        refreshToken.setExpiresAt(Instant.now().plus(Duration.ofDays(refreshTokenExpirationDays)));

        return refreshTokenRepo.save(refreshToken);
    }

    public RefreshToken getAndRefresh(UUID id) throws InvalidOrExpiredRefreshTokenException {
        RefreshToken token =  refreshTokenRepo
                .findByIdAndExpiresAtAfter(id, Instant.now())
                .orElseThrow(() -> new InvalidOrExpiredRefreshTokenException("Invalid or expired refresh token"));
        token.setExpiresAt(Instant.now().plus(Duration.ofDays(refreshTokenExpirationDays)));
        return token;
    }

    public void revoke(UUID id) {
        refreshTokenRepo.deleteById(id);
    }
}
