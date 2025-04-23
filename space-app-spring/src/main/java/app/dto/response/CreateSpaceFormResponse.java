package app.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateSpaceFormResponse {
    List<SpaceType> spaceTypes;

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class SpaceType {
        int id;
        String name;

        public static CreateSpaceFormResponse.SpaceType
        mapFromSpaceTypeEntity(app.entity.SpaceType entity) {
            return new CreateSpaceFormResponse.SpaceType(entity.getId(), entity.getName());
        }
    }
}
