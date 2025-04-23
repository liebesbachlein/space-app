package app.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GetSpacesResponse {
    List<Space> spaces;
    List<SpaceType> spaceTypes;

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class SpaceType {
        int id;
        String name;

        public static SpaceType
        mapFromSpaceTypeEntity(app.entity.SpaceType entity) {
            return new SpaceType(entity.getId(), entity.getName());
        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Space {
        int id;
        String name;
        BigDecimal price;
        int typeId;
        String typeName;

        public static Space mapFromSpaceEntity(app.entity.Space entity) {
            return Space.builder()
                    .id(entity.getId())
                    .name(entity.getName())
                    .price(entity.getPrice())
                    .typeId(entity.getType().getId())
                    .typeName(entity.getType().getName())
                    .build();
        }
    }
}
