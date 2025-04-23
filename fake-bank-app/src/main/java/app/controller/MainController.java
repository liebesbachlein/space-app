package app.controller;

import app.dto.*;
import app.service.AccountService;
import app.util.validation.exception.AccountNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api")
@Valid
@Validated
public class MainController {
    final private AccountService accountService;

    public MainController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    public String hello() {
        return "Fake Bank API. Yes, we store your credentials, but only in an encrypted form";
    }

    @PostMapping("/withdraw")
    public void withdrawAmount(@RequestBody @Valid WithdrawAmountRequest request) {
        accountService.withdraw(request);
    }

    @PostMapping("/account")
    public CreateAccountResponse createAccount(@RequestBody @Valid CreateAccountRequest request) {
        return accountService.createAccount(request);
    }

    @GetMapping("/account/balance/id/{accountId}")
    public GetAccountBalanceResponse getAccountBalanceById(
            @PathVariable("accountId") @NotNull Long accountId) {
        return accountService.getBalance(accountId);
    }

    @GetMapping("/account/balance/email/{email}")
    public GetAccountBalanceResponse getAccountBalanceByEmail(
            @PathVariable("email") @NotNull String email) {

        return accountService.getBalanceByEmail(email);
    }
}
