package app.dto.request;

import app.entity.Space;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateReservationRequest {
    @NotNull(message = "Reservation date is a required field")
    @FutureOrPresent(message = "Reservation cannot be created on past dates")
    LocalDate date;

    @NotNull(message = "Reservation time is a required field")
    LocalTime hour;

    @NotNull(message = "Space id of reservation is a required field")
    @Min(value = 1, message = "Space id must be a positive nonzero integer")
    int spaceId;
}
