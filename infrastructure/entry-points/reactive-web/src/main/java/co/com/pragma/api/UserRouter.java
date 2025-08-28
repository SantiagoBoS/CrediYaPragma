package co.com.pragma.api;

import co.com.pragma.api.dto.UserRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class UserRouter {
    @Bean
    @RouterOperation(
        path = "/api/v1/usuarios",
        beanClass = UserHandler.class,
        beanMethod = "registerUser",
        method = POST,
        operation = @Operation(
            operationId = "registerUser",
            summary = "Registrar un nuevo usuario",
            description = "Permite registrar un nuevo solicitante proporcionando sus datos personales",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                content = @Content(schema = @Schema(implementation = UserRequestDTO.class))
            ),
            responses = {
                @ApiResponse(
                    responseCode = "201",
                    description = "Usuario registrado exitosamente",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
                ),
                @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
                ),
                @ApiResponse(
                    responseCode = "409",
                    description = "Correo ya registrado",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
                ),
                @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
                )
            }
        )
    )
    public RouterFunction<ServerResponse> userRoutes(UserHandler handler) {
        return route(POST("/api/v1/usuarios"), handler::registerUser);
    }
}
