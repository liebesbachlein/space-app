package app.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
    private int status;
    private String message = "";
    private String details = "";

    public ErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
