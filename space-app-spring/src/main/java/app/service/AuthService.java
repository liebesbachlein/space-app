package app.service;

import app.dto.request.RefreshTokenRequest;
import app.entity.RefreshToken;
import app.entity.User;
import app.dto.response.AuthTokenResponse;
import app.dto.request.LoginRequest;
import app.dto.request.RegisterRequest;
import app.service.jwt.JwtUtil;
import liquibase.datatype.core.UUIDType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
public class AuthService {
    private final UserService userService;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthService(UserService userService, RefreshTokenService refreshTokenService,
                       AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.refreshTokenService = refreshTokenService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public AuthTokenResponse registerUser(RegisterRequest request) {
        User user = User.builder()
                .id(null)
                .email(request.getEmail())
                .name(request.getName())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(User.Role.ROLE_USER)
                .build();
        userService.create(user);
        String accessToken = JwtUtil.generateToken(user);
        RefreshToken refreshToken = refreshTokenService.generate(user);

        return new AuthTokenResponse(accessToken, refreshToken.getId());
    }

    @Transactional
    public AuthTokenResponse login(LoginRequest request) throws AuthenticationException {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userService.getByEmail(request.getEmail());
        String accessToken = JwtUtil.generateToken(user);
        RefreshToken refreshToken = refreshTokenService.generate(user);

        return new AuthTokenResponse(accessToken, refreshToken.getId());
    }

    @Transactional
    public AuthTokenResponse refresh(RefreshTokenRequest request) throws AuthenticationException {
        RefreshToken refreshToken = refreshTokenService.getAndRefresh(request.getRefreshToken());
        String accessToken = JwtUtil.generateToken(refreshToken.getUser());
        return new AuthTokenResponse(accessToken, refreshToken.getId());
    }

    @Transactional
    public void logout(UUID refreshTokenId) throws AuthenticationException {
        refreshTokenService.revoke(refreshTokenId);
    }

    @Deprecated
    @Transactional
    public AuthTokenResponse registerAdmin(RegisterRequest request) {
        User user = User.builder()
                .id(null)
                .email(request.getEmail())
                .name(request.getName())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(User.Role.ROLE_ADMIN)
                .build();
        userService.create(user);
        String accessToken = JwtUtil.generateToken(user);
        RefreshToken refreshToken = refreshTokenService.generate(user);

        return new AuthTokenResponse(accessToken, refreshToken.getId());
    }
}
