package app.dto;

import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@AllArgsConstructor
public class CreateSessionRequest {
    @NotNull(message = "Email cannot be null")
    @Email(message = "Invalid email")
    String email;

    @NotBlank(message = "Token cannot be blank")
    @Size(min = 16, max = 16)
    String token;

    @DecimalMin(value = "0", message = "Amount must be positive")
    @DecimalMax(value = "9999999999", message = "Amount cannot be larger than 9999999999")
    BigDecimal amount;

    @NotNull(message = "Cvc cannot be null")
    @Pattern(regexp = "^[0-9]{3}$", message = "Invalid cvc")
    String cvc;
}
