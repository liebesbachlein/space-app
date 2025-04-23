package app.controller;

import app.dto.request.CreateReservationRequest;
import app.dto.response.CreateReservationFormResponse;
import app.dto.response.GetUserReservationsResponse;
import app.entity.User;
import app.service.ReservationService;
import app.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@Validated
@Valid
public class ReservationUserController {
    private final ReservationService reservationService;
    private final UserService userService;

    @Autowired
    public ReservationUserController(
            ReservationService reservationService, UserService userService) {
        this.reservationService = reservationService;
        this.userService = userService;
    }

   // @PreAuthorize("hasRole('USER')")
    @GetMapping("/reservations/user")
    public GetUserReservationsResponse getUserReservations(
            @RequestParam(name = "selectedTab") String selectedTab
    ) {
        User user = userService.getCurrentUser();
        List<GetUserReservationsResponse.Reservation> list;
        if (selectedTab.equals("archive")) {
            list = reservationService.getAllByOwnerInArchive(user.getId()).stream()
                    .map(GetUserReservationsResponse.Reservation::mapFromReservationEntity)
                    .toList();
        } else {
            list = reservationService.getAllByOwnerInActive(user.getId()).stream()
                    .map(GetUserReservationsResponse.Reservation::mapFromReservationEntity)
                    .toList();
        }
        return new GetUserReservationsResponse(list);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/reservation/form")
    public CreateReservationFormResponse createReservationForm(
            @RequestParam(name = "spaceId", required = false) Integer spaceId,
            @RequestParam(name = "date")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, fallbackPatterns = { "M/d/yy", "dd.MM.yyyy" })
            @FutureOrPresent
            @NotNull
            LocalDate date
            ) {
        User user = userService.getCurrentUser();
        if (spaceId == null) return reservationService.createBlankReservationFormResponse();
        else return reservationService.createReservationFormResponse(
                user, spaceId, date);
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/reservation/{id}")
    public void cancelReservation(
            @PathVariable("id")
            @NotBlank(message = "Reservation id must be provided")
            String id
    ) {
        User user = userService.getCurrentUser();
        reservationService.cancelWithUserCheck(user.getId(), id);
    }
}
