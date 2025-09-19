package co.com.pragma.api.user;

import co.com.pragma.api.user.dto.UserDTO;
import co.com.pragma.model.constants.SwaggerConstants;
import co.com.pragma.model.constants.ApiPaths;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Configuration
public class UserRouter {
    @Bean
    @RouterOperation( path = ApiPaths.USER_BASE, beanClass = UserHandler.class, beanMethod = SwaggerConstants.USER_PATH_OPERATION_ID, method = POST,
        operation = @Operation( operationId = SwaggerConstants.USER_PATH_OPERATION_ID,  summary = SwaggerConstants.USER_PATH_OPERATION_SUMMARY,  description = SwaggerConstants.USER_PATH_OPERATION_DESCRIPTION,
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody( description = SwaggerConstants.USER_PATH_REQUEST_BODY_DESCRIPTION, required = true, content = @Content( schema = @Schema(implementation = UserDTO.class), examples = { @ExampleObject( name = SwaggerConstants.USER_TEXT_VALID_USER, value = SwaggerConstants.USER_EXAMPLE_VALID_USER)})),
            responses = {
                @ApiResponse( responseCode = SwaggerConstants.CREATE_CODE, description = SwaggerConstants.USER_CREATE_MESSAGE, content = @Content( schema = @Schema(implementation = ApiResponse.class), examples = { @ExampleObject(name = SwaggerConstants.TEXT_VALIDATION_CORRECT, value = SwaggerConstants.USER_EXAMPLE_VALIDATION_CORRECT )})),
                @ApiResponse( responseCode = SwaggerConstants.VALIDATION_CODE,  description = SwaggerConstants.VALIDATION_MESSAGE, content = @Content( schema = @Schema(implementation = ApiResponse.class), examples = {@ExampleObject( name = SwaggerConstants.USER_TEXT_ERROR_VALIDATION, value = SwaggerConstants.USER_EXAMPLE_ERROR_VALIDATION)})),
                @ApiResponse( responseCode = SwaggerConstants.CONFLICT_CODE,  description = SwaggerConstants.USER_CONFLICT_MESSAGE, content = @Content( schema = @Schema(implementation = ApiResponse.class), examples = { @ExampleObject( name = SwaggerConstants.USER_TEXT_VALIDATION_DUPLICATE, value = SwaggerConstants.USER_EXAMPLE_VALIDATION_DUPLICATE)})),
                @ApiResponse( responseCode = SwaggerConstants.INTERNAL_ERROR_CODE, description = SwaggerConstants.INTERNAL_ERROR_MESSAGE, content = @Content( schema = @Schema(implementation = ApiResponse.class), examples = { @ExampleObject( name = SwaggerConstants.USER_TEXT_INTERNAL_ERROR,  value = SwaggerConstants.USER_EXAMPLE_INTERNAL_ERROR)}))
            }
        )
    )

    public RouterFunction<ServerResponse> userRoutes(UserHandler handler) {
        return RouterFunctions
                .route(RequestPredicates.POST(ApiPaths.USER_BASE), handler::registerUser)
                .andRoute(RequestPredicates.GET(ApiPaths.USER_BY_DOCUMENT), handler::getUserByDocument);
    }
}
