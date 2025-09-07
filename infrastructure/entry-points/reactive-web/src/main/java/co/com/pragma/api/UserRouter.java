package co.com.pragma.api;

import co.com.pragma.api.dto.UserRequestDTO;
import co.com.pragma.api.util.UserUtils;
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
    @RouterOperation( path = UserUtils.PATH_API_USERS, beanClass = UserHandler.class, beanMethod = UserUtils.PATH_OPERATION_ID, method = POST,
        operation = @Operation( operationId = UserUtils.PATH_OPERATION_ID,  summary = UserUtils.PATH_OPERATION_SUMMARY,  description = UserUtils.PATH_OPERATION_DESCRIPTION,
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody( description = UserUtils.PATH_REQUEST_BODY_DESCRIPTION, required = true, content = @Content( schema = @Schema(implementation = UserRequestDTO.class), examples = { @ExampleObject( name = UserUtils.TEXT_VALID_USER, value = UserUtils.EXAMPLE_VALID_USER)})),
            responses = {
                @ApiResponse( responseCode = UserUtils.CREATE_CODE, description = UserUtils.CREATE_MESSAGE, content = @Content( schema = @Schema(implementation = ApiResponse.class), examples = { @ExampleObject(name = UserUtils.TEXT_VALIDATION_CORRECT, value = UserUtils.EXAMPLE_VALIDATION_CORRECT )})),
                @ApiResponse( responseCode = UserUtils.VALIDATION_CODE,  description = UserUtils.VALIDATION_MESSAGE, content = @Content( schema = @Schema(implementation = ApiResponse.class), examples = {@ExampleObject( name = UserUtils.TEXT_ERROR_VALIDATION, value = UserUtils.EXAMPLE_ERROR_VALIDATION)})),
                @ApiResponse( responseCode = UserUtils.CONFLICT_CODE,  description = UserUtils.CONFLICT_MESSAGE, content = @Content( schema = @Schema(implementation = ApiResponse.class), examples = { @ExampleObject( name = UserUtils.TEXT_VALIDATION_DUPLICATE, value = UserUtils.EXAMPLE_VALIDATION_DUPLICATE)})),
                @ApiResponse( responseCode = UserUtils.INTERNAL_ERROR_CODE, description = UserUtils.INTERNAL_ERROR_MESSAGE, content = @Content( schema = @Schema(implementation = ApiResponse.class), examples = { @ExampleObject( name = UserUtils.TEXT_INTERNAL_ERROR,  value = UserUtils.EXAMPLE_INTERNAL_ERROR)}))
            }
        )
    )

    public RouterFunction<ServerResponse> userRoutes(UserHandler handler) {
        return route(POST(UserUtils.PATH_API_USERS), handler::registerUser);
    }
}
