package app.dto.request;

import app.entity.User;
import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginRequest {
    @NotBlank(message = "Email is a required field")
    @Email(message = "Email must have a valid format")
    String email;

    @NotBlank(message = "Password is a required field")
    @Size(min = 5, max = 32, message = "Password must be of of length 5 - 32")
    String password;
}
