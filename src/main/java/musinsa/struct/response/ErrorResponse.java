package musinsa.struct.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ToString
public class ErrorResponse {
    private HttpStatus status;

    private String error;

    public static ResponseEntity<ErrorResponse> of(HttpStatus status, String error) {
        return ResponseEntity.status(status).body(new ErrorResponse(status, error));
    }
}