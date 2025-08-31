package co.com.pragma.api.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ErrorResponse {
    private String code;
    private String message;
    private List<FieldErrorDTO> errors;
}
