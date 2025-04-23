package app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class GetAccountBalanceResponse {
    private BigDecimal balance;
}
