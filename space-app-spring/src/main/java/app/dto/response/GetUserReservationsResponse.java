package app.dto.response;

import app.entity.Reservation;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.apache.commons.collections4.comparators.ComparatorChain;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GetUserReservationsResponse {
    List<Reservation> reservations;

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Reservation {
        String id;
        String spaceName;
        LocalDate date;
        LocalTime hour;

        public static Reservation mapFromReservationEntity(app.entity.Reservation entity) {
            return Reservation.builder()
                    .id(entity.getId())
                    .spaceName(entity.getSpace().getName())
                    .date(entity.getDate())
                    .hour(entity.getHour())
                    .build();
        }
    }
}
