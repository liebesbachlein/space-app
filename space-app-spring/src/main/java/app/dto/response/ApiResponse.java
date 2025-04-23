package app.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse {
    private int status;
    private String message = "";
    private String details = "";

    public ApiResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
