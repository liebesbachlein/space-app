package app.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateReservationFormResponse {
    Space selectedSpace;
    List<Space> spaces;
    List<Reservation> userReservationsOnDate;
    List<Reservation> spaceReservationsOnDate;

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Space {
        int id;
        String name;
        BigDecimal price;
        String typeName;

        public static Space mapFromSpaceEntity(app.entity.Space entity) {
            return Space.builder()
                    .id(entity.getId())
                    .name(entity.getName())
                    .price(entity.getPrice())
                    .typeName(entity.getType().getName())
                    .build();
        }
    }

    @Data
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Reservation {
        LocalTime hour;

        public static Reservation mapFromReservationEntity(app.entity.Reservation entity) {
            return new Reservation(entity.getHour());
        }
    }
}
