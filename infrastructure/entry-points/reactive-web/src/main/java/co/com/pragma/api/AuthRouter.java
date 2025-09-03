package co.com.pragma.api;

import co.com.pragma.api.dto.AuthRequestDTO;
import co.com.pragma.api.exception.AuthUtils;
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
public class AuthRouter {
    @Bean
    @RouterOperation(path = "/api/v1/login", beanClass = AuthHandler.class, beanMethod = "login", method = POST,
        operation = @Operation( operationId = "loginUser", summary = "Autenticar usuario", description = "Permite iniciar sesión proporcionando las credenciales de acceso",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody( description = "Credenciales de acceso del usuario", required = true, content = @Content(schema = @Schema(implementation = AuthRequestDTO.class), examples = { @ExampleObject( name = AuthUtils.TEXT_VALID_LOGIN, value = AuthUtils.EXAMPLE_VALID_LOGIN)})),
            responses = {
                @ApiResponse(responseCode = AuthUtils.SUCCESS_CODE, description = AuthUtils.SUCCESS_MESSAGE, content = @Content(schema = @Schema(implementation = ApiResponse.class), examples = {@ExampleObject(name = AuthUtils.TEXT_VALID_LOGIN, value = AuthUtils.EXAMPLE_VALID_LOGIN)})),
                @ApiResponse(responseCode = AuthUtils.VALIDATION_CODE, description = AuthUtils.VALIDATION_MESSAGE, content = @Content(schema = @Schema(implementation = ApiResponse.class), examples = {@ExampleObject(name = AuthUtils.VALIDATION_MESSAGE, value = AuthUtils.EXAMPLE_VALIDATION_ERROR)})),
                @ApiResponse(responseCode = AuthUtils.INVALID_CREDENTIALS, description = AuthUtils.INVALID_CREDENTIALS_MESSAGE, content = @Content(schema = @Schema(implementation = ApiResponse.class), examples = {@ExampleObject(name = AuthUtils.TEXT_ERROR_AUTH, value = AuthUtils.EXAMPLE_ERROR_AUTH)}))
            }
        )
    )
    public RouterFunction<ServerResponse> loginRoute(AuthHandler handler) {
        return route(POST("/api/v1/login"), handler::login);
    }
}
