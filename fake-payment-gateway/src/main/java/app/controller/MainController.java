package app.controller;

import app.dto.CreateSessionRequest;
import app.dto.CreateSessionResponse;
import app.entity.Session;
import app.service.SessionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api")
public class MainController {
    private final SessionService sessionService;

    @Autowired
    public MainController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @GetMapping
    public String hello() {
        return "Fake Payment Gateway";
    }

    @PostMapping("/session")
    public CompletableFuture<ResponseEntity<?>> processPayment(
            @RequestBody @Valid CreateSessionRequest request) {
        CompletableFuture<CreateSessionResponse> response = sessionService.processSession(request);
        return response.thenApply((value) -> {
            if (value.isSucceeded()) return ResponseEntity.ok(value);
            else return ResponseEntity.unprocessableEntity().body(value);
        });
    }
}
