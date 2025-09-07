package co.com.pragma.api.user;

import co.com.pragma.api.user.dto.UserRequestDTO;
import co.com.pragma.api.util.Utils;
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
    @RouterOperation( path = Utils.USER_PATH_API_USERS, beanClass = UserHandler.class, beanMethod = Utils.USER_PATH_OPERATION_ID, method = POST,
        operation = @Operation( operationId = Utils.USER_PATH_OPERATION_ID,  summary = Utils.USER_PATH_OPERATION_SUMMARY,  description = Utils.USER_PATH_OPERATION_DESCRIPTION,
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody( description = Utils.USER_PATH_REQUEST_BODY_DESCRIPTION, required = true, content = @Content( schema = @Schema(implementation = UserRequestDTO.class), examples = { @ExampleObject( name = Utils.USER_TEXT_VALID_USER, value = Utils.USER_EXAMPLE_VALID_USER)})),
            responses = {
                @ApiResponse( responseCode = Utils.CREATE_CODE, description = Utils.USER_CREATE_MESSAGE, content = @Content( schema = @Schema(implementation = ApiResponse.class), examples = { @ExampleObject(name = Utils.USER_TEXT_VALIDATION_CORRECT, value = Utils.USER_EXAMPLE_VALIDATION_CORRECT )})),
                @ApiResponse( responseCode = Utils.VALIDATION_CODE,  description = Utils.VALIDATION_MESSAGE, content = @Content( schema = @Schema(implementation = ApiResponse.class), examples = {@ExampleObject( name = Utils.USER_TEXT_ERROR_VALIDATION, value = Utils.USER_EXAMPLE_ERROR_VALIDATION)})),
                @ApiResponse( responseCode = Utils.CONFLICT_CODE,  description = Utils.USER_CONFLICT_MESSAGE, content = @Content( schema = @Schema(implementation = ApiResponse.class), examples = { @ExampleObject( name = Utils.USER_TEXT_VALIDATION_DUPLICATE, value = Utils.USER_EXAMPLE_VALIDATION_DUPLICATE)})),
                @ApiResponse( responseCode = Utils.INTERNAL_ERROR_CODE, description = Utils.INTERNAL_ERROR_MESSAGE, content = @Content( schema = @Schema(implementation = ApiResponse.class), examples = { @ExampleObject( name = Utils.USER_TEXT_INTERNAL_ERROR,  value = Utils.USER_EXAMPLE_INTERNAL_ERROR)}))
            }
        )
    )

    public RouterFunction<ServerResponse> userRoutes(UserHandler handler) {
        return route(POST(Utils.USER_PATH_API_USERS), handler::registerUser);
    }
}
