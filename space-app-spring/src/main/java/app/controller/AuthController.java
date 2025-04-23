package app.controller;

import app.dto.response.AuthTokenResponse;
import app.dto.request.RefreshTokenRequest;
import app.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import app.dto.request.LoginRequest;
import app.dto.request.RegisterRequest;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication")
public class AuthController {
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "User authorization")
    @PostMapping("/login")
    public AuthTokenResponse login(@RequestBody @Valid LoginRequest request) {

        return authService.login(request);
    }

    @Operation(summary = "User registration")
    @PostMapping("/register")
    public AuthTokenResponse registerUser(@RequestBody @Valid RegisterRequest request) {
        return authService.registerUser(request);
    }

    @Operation(summary = "User/Admin logout")
    @PostMapping("/logout")
    public void registerUser(@RequestBody @Valid RefreshTokenRequest request) {
        authService.logout(request.getRefreshToken());
    }

    @PostMapping("/refresh-token")
    public AuthTokenResponse refreshToken(
            @RequestBody @Valid RefreshTokenRequest request
    ) {
        return authService.refresh(request);
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public void authAdmin() {}

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    public void authUser() {}
}
