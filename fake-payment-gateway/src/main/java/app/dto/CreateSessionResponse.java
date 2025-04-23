package app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateSessionResponse {
    private String token;
    private boolean succeeded;
}
