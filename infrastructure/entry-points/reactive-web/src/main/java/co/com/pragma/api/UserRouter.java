package co.com.pragma.api;

import co.com.pragma.api.dto.UserRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
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
                description = "Datos del nuevo usuario",
                required = true,
                content = @Content(
                    schema = @Schema(implementation = UserRequestDTO.class),
                    examples = {
                        @ExampleObject(
                            name = "Usuario válido",
                            value = """
                            {
                                  "documentNumber": "123456789",
                                  "name": "Santiago",
                                  "lastName": "Borrero",
                                  "birthDate": "1995-05-10",
                                  "address": "Calle 123",
                                  "phone": "3001234567",
                                  "email": "santi@correo.com",
                                  "baseSalary": "3500000"
                            }
                            """
                        )
                    }
                )
            ),
            responses = {
                @ApiResponse(
                    responseCode = "201",
                    description = "Usuario registrado exitosamente",
                    content = @Content(
                        schema = @Schema(implementation = ApiResponse.class),
                        examples = {
                            @ExampleObject(
                                name = "Respuesta exitosa",
                                value = """
                                    {
                                        "code": "201.01",
                                        "message": "Usuario registrado exitosamente",
                                        "data": {
                                        "name": "Santiago",
                                        "lastName": "Borrero",
                                        "birthDate": "1995-05-10",
                                        "address": "Calle 123",
                                        "phone": "3001234567",
                                        "email": "santi@correo.com",
                                        "baseSalary": 3500000
                                    }
                                }
                                """
                            )
                        }
                    )
                ),
                @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos",
                    content = @Content(
                        schema = @Schema(implementation = ApiResponse.class),
                        examples = {
                            @ExampleObject(
                                name = "Error de validación",
                                value = """
                                {
                                    "code": "400.01",
                                    "message": "Datos inválidos.",
                                    "errors": [
                                        {
                                            "field": "name",
                                            "description": "El campo nombre es obligatorio"
                                        },
                                        {
                                            "field": "email",
                                            "description": "El correo tiene formato inválido"
                                        }
                                    ]
                                }
                                """
                            )
                        }
                    )
                ),
                @ApiResponse(
                    responseCode = "409",
                    description = "Correo ya registrado",
                    content = @Content(
                        schema = @Schema(implementation = ApiResponse.class),
                        examples = {
                            @ExampleObject(
                                name = "Conflicto por duplicado",
                                value = """
                                    {
                                      "code": "409.01",
                                      "message": "El correo ya se encuentra registrado"
                                    }
                                """
                            )
                        }
                    )
                ),
                @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content(
                        schema = @Schema(implementation = ApiResponse.class),
                        examples = {
                            @ExampleObject(
                                name = "Error interno",
                                value = """
                                    {
                                        "code": "500.01",
                                        "message": "Error interno del servidor"
                                    }
                                    """
                            )
                        }
                    )
                )
            }
        )
    )

    public RouterFunction<ServerResponse> userRoutes(UserHandler handler) {
        return route(POST("/api/v1/usuarios"), handler::registerUser);
    }
}
