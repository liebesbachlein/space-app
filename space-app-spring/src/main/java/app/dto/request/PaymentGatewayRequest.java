package app.dto.request;

import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentGatewayRequest {
    String email;

    String token;

    BigDecimal amount;

    String cvc;

    @Override
    public String toString() {
        return "{" +
                "\"email\":\"" + email + "\"" +
                ", \"token\":\"" + token + "\"" +
                ", \"amount\":" + amount +
                ", \"cvc\":\"" + cvc + "\"" +
                "}";
    }
}
