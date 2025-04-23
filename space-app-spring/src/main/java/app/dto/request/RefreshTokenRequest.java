package app.dto.request;

import jakarta.validation.constraints.NotNull;
import liquibase.datatype.core.UUIDType;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RefreshTokenRequest {
    @NotNull
    UUID refreshToken;
}
