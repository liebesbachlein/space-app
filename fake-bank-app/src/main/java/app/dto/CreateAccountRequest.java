package app.dto;

import app.util.validation.AmountConstraint;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@AllArgsConstructor
@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateAccountRequest {
    @NotNull(message = "Email cannot be null")
    @Email(message = "Invalid email")
    String email;

    @AmountConstraint(message = "Invalid balance")
    @DecimalMin(value = "0", message = "Amount must be positive")
    @DecimalMax(value = "9999999999", message = "balance cannot be larger than 9999999999")
    BigDecimal balance;

    @NotNull(message = "Cvc cannot be null")
    @Pattern(regexp = "^[0-9]{3}$", message = "Invalid cvc")
    String cvc;
}
