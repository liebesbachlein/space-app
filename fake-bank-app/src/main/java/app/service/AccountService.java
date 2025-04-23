package app.service;

import app.dto.CreateAccountRequest;
import app.dto.CreateAccountResponse;
import app.dto.GetAccountBalanceResponse;
import app.dto.WithdrawAmountRequest;
import app.entity.Account;
import app.repository.AccountRepository;
import app.util.validation.exception.AccountNotFoundException;
import app.util.validation.exception.NonUniquePropertyException;
import app.util.validation.exception.NotEnoughFundsException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

@Slf4j
@Service
@Transactional
public class AccountService {
    private final AccountRepository accountRepo;
    private final PasswordEncoder encoder;

    public AccountService(AccountRepository accountRepo, PasswordEncoder encoder) {
        this.accountRepo = accountRepo;
        this.encoder = encoder;
    }

    @Transactional
    public BigDecimal withdraw(WithdrawAmountRequest request)
            throws AccountNotFoundException, NotEnoughFundsException, BadCredentialsException {
        Account account = auth(request.getEmail(), request.getCvc());
        if (account.getBalance().compareTo(request.getAmount()) >= 0) {
            log.info("Withdrawing " + request.getAmount() + " from " + request.getEmail());
            account.setBalance(account.getBalance().subtract(request.getAmount()));
        } else {
            throw new NotEnoughFundsException("Account " +
                    account.getId()
                    + " attempted to withdraw amount, that was larger than the available balance");
        }
        return request.getAmount();
    }

    public Account auth(String email, String cvc)
            throws AccountNotFoundException, BadCredentialsException {
        Account account = accountRepo.findByEmail(email)
                .orElseThrow(() -> new AccountNotFoundException("Account with email " +
                        email + " not found"));
        if (encoder.matches(cvc, account.getCvc())) {
            return account;
        } else throw new BadCredentialsException("Account with email " +
                email + " provided wrong credentials");
    }

    public CreateAccountResponse createAccount(CreateAccountRequest request) {
        if (accountRepo.existsByEmail(request.getEmail()))
            throw new NonUniquePropertyException("Account with email " +
                    request.getEmail() + " already exists");

        Account account = new Account();
        account.setEmail(request.getEmail());
        account.setBalance(request.getBalance());
        account.setCvc(encoder.encode(request.getCvc()));
        accountRepo.save(account);

        return CreateAccountResponse.mapFromAccount(account);
    }

    public GetAccountBalanceResponse getBalance(Long accountId) {
        Account account = accountRepo.findById(accountId).orElseThrow(
                () -> new AccountNotFoundException("Account with id " +
                        accountId + " not found"));
        return new GetAccountBalanceResponse(account.getBalance());
    }

    public void deleteAccountByEmail(String email) {
        accountRepo.deleteByEmail(email);
    }

    public GetAccountBalanceResponse getBalanceByEmail(String email) {
        Account account = accountRepo.findByEmail(email).orElseThrow(
                () -> new AccountNotFoundException("Account with email " +
                        email + " not found"));
        return new GetAccountBalanceResponse(account.getBalance());
    }
}
