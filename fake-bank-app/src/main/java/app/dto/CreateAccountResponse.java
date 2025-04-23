package app.dto;

import app.entity.Account;
import app.util.validation.AmountConstraint;
import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Builder
public class CreateAccountResponse {
    Long id;
    String email;
    BigDecimal balance;
    String cvc;

    public static CreateAccountResponse mapFromAccount(Account account) {
        return CreateAccountResponse.builder()
                .id(account.getId())
                .email(account.getEmail())
                .balance(account.getBalance())
                .cvc(account.getCvc())
                .build();
    }
}
