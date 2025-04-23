package app.service;

import app.dto.response.CreateReservationFormResponse;
import app.entity.Space;
import app.util.exception.ResourceNotFoundException;
import app.util.exception.auth.UnauthorizedOperationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import app.entity.Reservation;
import app.entity.User;
import app.repository.ReservationRepository;
import app.util.exception.ReservationConflictException;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalTime;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class ReservationService {
    private final UserService userService;
    private final ReservationRepository repo;
    private final SpaceService spaceService;

    @Autowired
    public ReservationService(
            UserService userService, ReservationRepository repo,
            SpaceService spaceService) {
        this.userService = userService;
        this.repo = repo;
        this.spaceService = spaceService;
    }

    @Transactional
    public Reservation create(
            User user, Space space, LocalDate date, LocalTime hour, String token)
            throws ReservationConflictException {
        if (!isSpaceReservable(space.getId(), date, hour))
            throw new ReservationConflictException(
                    "Cannot create reservation for space ID = " + space.getId() +
                    " on " + date + " at " + hour +
                    " due to a time conflict with other reservations");

        Reservation reservation = Reservation.builder()
                .id(token)
                .owner(user)
                .space(space)
                .date(date)
                .hour(hour)
                .createdAt(Instant.now())
                .build();
        return repo.save(reservation);
    }

    public Reservation getById(String reservationId) {
        return repo.findById(reservationId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Reservation with id " +
                                reservationId + " not found"));
    }

    @Transactional
    public void cancel(String reservationId) {
        repo.findById(reservationId).orElseThrow(
                () -> new ResourceNotFoundException("Reservation with id " +
                        reservationId + " not found"));
        repo.deleteById(reservationId);
    }

    @Transactional
    public void cancelWithUserCheck(Long userId, String reservationId) {
        Reservation reservation = repo.findById(reservationId).orElseThrow(
                () -> new ResourceNotFoundException("Reservation with id " +
                        reservationId + " not found"));
        if (reservation.getOwner().getId() != userId)
            throw new UnauthorizedOperationException(
                    "The user with id " + userId +
                            " attempted to cancel another user's reservation with id " +
                            reservationId);
        repo.deleteById(reservationId);
    }

    public List<Reservation> getAllBySpace(int spaceId) {
        return repo.findAllBySpace(spaceId);
    }

    public List<Reservation> getAllBySpaceAndDate(int spaceId, LocalDate date) {
        return repo.findAllBySpaceAndDate(spaceId, date);
    }

    public List<Reservation> getAllByOwner(long ownerId)
            throws ResourceNotFoundException{
        userService.getById(ownerId);
        return repo.findAllByOwner(ownerId);
    }

    public List<Reservation> getAllByOwnerInArchive(long userId)
            throws ResourceNotFoundException {
        userService.getById(userId);
        return repo.findAllByOwnerBeforeDateExclusive(userId, LocalDate.now());
    }

    public List<Reservation> getAllByOwnerInActive(long userId)
            throws ResourceNotFoundException {
        userService.getById(userId);
        return repo.findAllByOwnerAfterDateInclusive(userId, LocalDate.now());
    }

    public List<Reservation> getAllByOwnerAndSpaceOnDate(Long ownerId, int spaceId, LocalDate date) {
        return repo.findAllByOwnerAndSpaceAndDate(ownerId, spaceId, date);
    }

    public boolean isSpaceReservable(
            int spaceId,
            LocalDate date,
            LocalTime hour) throws ResourceNotFoundException {
        spaceService.getById(spaceId);
        List<Reservation> res = repo
                .findAllBySpaceAndDateAndHour(spaceId, date, hour);
        return res.isEmpty();
    }

    public CreateReservationFormResponse createReservationFormResponse(
            User user, Integer spaceId, LocalDate date) {
        var spaces = spaceService.getAll().stream()
                .map(CreateReservationFormResponse.Space::mapFromSpaceEntity)
                .toList();

        var selectedSpace = CreateReservationFormResponse.Space.mapFromSpaceEntity(
                spaceService.getById(spaceId));

        var userReservationsOnDate = getAllByOwnerAndSpaceOnDate
                (user.getId(), spaceId, date).stream()
                .map(CreateReservationFormResponse.Reservation::mapFromReservationEntity)
                .toList();

        var spaceReservationOnDate = getAllBySpaceAndDate(spaceId, date).stream()
                .map(CreateReservationFormResponse.Reservation::mapFromReservationEntity)
                .toList();

        return CreateReservationFormResponse.builder()
                .selectedSpace(selectedSpace)
                .spaces(spaces)
                .userReservationsOnDate(userReservationsOnDate)
                .spaceReservationsOnDate(spaceReservationOnDate)
                .build();
    }

    public CreateReservationFormResponse createBlankReservationFormResponse() {
        var spaces = spaceService.getAll().stream()
                .map(CreateReservationFormResponse.Space::mapFromSpaceEntity)
                .toList();

        return CreateReservationFormResponse.builder()
                .selectedSpace(null)
                .spaces(spaces)
                .userReservationsOnDate(List.of())
                .spaceReservationsOnDate(List.of())
                .build();
    }
}
