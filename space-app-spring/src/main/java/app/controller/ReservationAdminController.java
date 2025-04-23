package app.controller;

import app.dto.request.CreateReservationRequest;
import app.dto.response.GetUserReservationsResponse;
import app.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@Tag(name = "Admin operations on reservations")
public class ReservationAdminController {
    private final ReservationService reservationService;

    @Autowired
    public ReservationAdminController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @Operation(summary = "Retrieves all reservations by user id")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/reservations/{userId}")
    public GetUserReservationsResponse getUserReservations(
            @PathVariable("userId")
            @NotNull(message = "User id must be provided")
            @Min(value = 1, message = "User id must be positive non-zero integer")
            Long userId
    ) {
        var list = reservationService.getAllByOwner(userId).stream()
                .map(GetUserReservationsResponse.Reservation::mapFromReservationEntity)
                .toList();
        return new GetUserReservationsResponse(list);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/reservation/{id}")
    public void cancelReservation(
            @PathVariable("id")
            @NotBlank(message = "Reservation id must be provided")
            String id
    ) {
        reservationService.cancel(id);
    }
}
