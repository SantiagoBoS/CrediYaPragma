package co.com.pragma.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Estructura estándar de respuesta de la API")
public class ApiResponse<T> {
    @Schema(description = "Código de negocio definido en AuthUtils", example = "201.01")
    private String code;

    @Schema(description = "Mensaje asociado al código de negocio", example = "Usuario registrado exitosamente")
    private String message;

    @Schema(description = "Objeto de respuesta en caso de éxito")
    private T data;

    @Schema(description = "Lista de errores en caso de validación o conflicto")
    private List<?> errors;
}
