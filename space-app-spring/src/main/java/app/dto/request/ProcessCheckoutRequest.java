package app.dto.request;

import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@AllArgsConstructor
public class ProcessCheckoutRequest {
    @NotNull(message = "Reservation date is a required field")
    @FutureOrPresent(message = "Reservation cannot be created on past dates")
    LocalDate date;

    @NotNull(message = "Reservation time is a required field")
    LocalTime hour;

    @NotNull(message = "Space id of reservation is a required field")
    @Min(value = 1, message = "Space id must be a positive nonzero integer")
    int spaceId;

    @NotNull(message = "Email cannot be null")
    @Email(message = "Invalid email")
    String email;

    @NotNull(message = "Cvc cannot be null")
    @Pattern(regexp = "^[0-9]{3}$", message = "Invalid cvc")
    String cvc;
}
