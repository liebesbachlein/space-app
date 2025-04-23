package app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import app.entity.Reservation;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, String> {

    @NativeQuery(value = "SELECT * FROM reservation WHERE space_id = ?1")
    List<Reservation> findAllBySpace(int spaceId);

    @NativeQuery(value = """
            SELECT * FROM reservation
            WHERE owner_id = ?1
            ORDER BY
            date DESC, 
            space_id ASC, 
            hour DESC
            """)
    List<Reservation> findAllByOwner(Long ownerId);

    @NativeQuery(value = """
            SELECT * FROM reservation
            WHERE owner_id = ?1
            AND date < ?2
            ORDER BY
            date DESC, 
            space_id ASC, 
            hour DESC
            """)
    List<Reservation> findAllByOwnerBeforeDateExclusive(Long ownerId, LocalDate date);

    @NativeQuery(value = """
            SELECT * FROM reservation
            WHERE owner_id = ?1
            AND date >= ?2
            ORDER BY 
            date DESC, 
            space_id ASC, 
            hour DESC
            """)
    List<Reservation> findAllByOwnerAfterDateInclusive(Long ownerId, LocalDate date);

    @NativeQuery(value = """
            SELECT * FROM reservation
            WHERE space_id = ?1
            AND date = ?2 AND hour = ?3
            ORDER BY 
            date DESC, 
            space_id ASC, 
            hour DESC
            """)
    List<Reservation> findAllBySpaceAndDateAndHour(
            int spaceId,
            LocalDate date,
            LocalTime hour);

    @NativeQuery(value = """
            SELECT * FROM reservation
            WHERE space_id = ?1
            AND date = ?2
            ORDER BY 
            date DESC, 
            space_id ASC, 
            hour DESC
            """)
    List<Reservation> findAllBySpaceAndDate(
            int spaceId,
            LocalDate date);

    @NativeQuery(value = """
            SELECT * FROM reservation
            WHERE owner_id = ?1
            AND space_id = ?2 AND date = ?3
            ORDER BY 
            date DESC, 
            space_id ASC, 
            hour DESC
            """)
    List<Reservation> findAllByOwnerAndSpaceAndDate(
            Long ownerId,
            int spaceId,
            LocalDate date);
}

