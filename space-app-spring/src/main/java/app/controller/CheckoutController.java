package app.controller;

import app.dto.request.ProcessCheckoutRequest;
import app.dto.response.ApiResponse;
import app.entity.User;
import app.service.CheckoutService;
import app.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping
public class CheckoutController {
    private final CheckoutService checkoutService;
    private final UserService userService;

    @Autowired
    public CheckoutController(CheckoutService checkoutService, UserService userService) {
        this.checkoutService = checkoutService;
        this.userService = userService;
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/api/checkout")
    public ResponseEntity<?> processCheckout(
            @RequestBody @Valid ProcessCheckoutRequest request) throws ExecutionException, InterruptedException {
        User user = userService.getCurrentUser();
        CompletableFuture<ApiResponse> response = checkoutService.processCheckout(user, request);
        CompletableFuture.allOf(response).join();
        return response.get().getStatus() == 200 ? ResponseEntity.ok(response.get()) :
             ResponseEntity.unprocessableEntity().body(response.get());
    }
}
