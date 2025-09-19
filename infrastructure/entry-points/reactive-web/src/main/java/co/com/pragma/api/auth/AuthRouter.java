package co.com.pragma.api.auth;

import co.com.pragma.api.auth.dto.AuthRequestDTO;
import co.com.pragma.model.constants.ApiPaths;
import co.com.pragma.model.constants.SwaggerConstants;
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
    @RouterOperation(path = ApiPaths.AUTH_BASE, beanClass = AuthHandler.class, beanMethod = "login", method = POST,
        operation = @Operation( operationId = SwaggerConstants.AUTH_PATH_OPERATION_ID, summary = SwaggerConstants.AUTH_PATH_OPERATION_SUMMARY, description = SwaggerConstants.AUTH_PATH_OPERATION_DESCRIPTION,
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody( description = SwaggerConstants.AUTH_PATH_REQUEST_BODY_DESCRIPTION, required = true, content = @Content(schema = @Schema(implementation = AuthRequestDTO.class), examples = { @ExampleObject( name = SwaggerConstants.AUTH_TEXT_VALID_LOGIN, value = SwaggerConstants.AUTH_EXAMPLE_VALID_LOGIN)})),
            responses = {
                @ApiResponse(responseCode = SwaggerConstants.CREATE_CODE, description = SwaggerConstants.AUTH_SUCCESS_MESSAGE, content = @Content(schema = @Schema(implementation = ApiResponse.class), examples = {@ExampleObject(name = SwaggerConstants.AUTH_TEXT_VALID_LOGIN, value = SwaggerConstants.AUTH_EXAMPLE_VALID_LOGIN)})),
                @ApiResponse(responseCode = SwaggerConstants.VALIDATION_CODE, description = SwaggerConstants.AUTH_VALIDATION_MESSAGE, content = @Content(schema = @Schema(implementation = ApiResponse.class), examples = {@ExampleObject(name = SwaggerConstants.AUTH_VALIDATION_MESSAGE, value = SwaggerConstants.AUTH_EXAMPLE_VALIDATION_ERROR)})),
                @ApiResponse(responseCode = SwaggerConstants.UNAUTHORIZED_CODE, description = SwaggerConstants.AUTH_INVALID_CREDENTIALS_MESSAGE, content = @Content(schema = @Schema(implementation = ApiResponse.class), examples = {@ExampleObject(name = SwaggerConstants.AUTH_TEXT_ERROR_AUTH, value = SwaggerConstants.AUTH_EXAMPLE_ERROR_AUTH)}))
            }
        )
    )
    public RouterFunction<ServerResponse> loginRoute(AuthHandler handler) {
        return route(POST(ApiPaths.AUTH_BASE), handler::login);
    }
}
