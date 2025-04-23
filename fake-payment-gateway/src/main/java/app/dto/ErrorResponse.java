package app.dto;

import lombok.*;

@Data
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
