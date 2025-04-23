package app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class BankRequestBody {
    String email;

    BigDecimal amount;

    String cvc;

    @Override
    public String toString() {
        return "{" +
                "\"email\":\"" + email + "\"" +
                ", \"amount\":" + amount +
                ", \"cvc\":\"" + cvc + "\"" +
                "}";
    }
}
