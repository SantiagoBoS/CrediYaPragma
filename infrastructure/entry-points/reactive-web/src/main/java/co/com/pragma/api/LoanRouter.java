package co.com.pragma.api;

import co.com.pragma.api.dto.LoanDTO;
import co.com.pragma.api.util.LoanUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;

@Configuration
public class LoanRouter {
    @Bean
    @RouterOperations({
        @RouterOperation( path = LoanUtils.ROUTER_BASE_PATH, beanClass = LoanHandler.class, beanMethod = LoanUtils.ROUTER_OPERATION_ID, method = POST,
            operation = @Operation( operationId = LoanUtils.ROUTER_OPERATION_ID, summary = LoanUtils.ROUTER_SUMMARY, description = LoanUtils.ROUTER_DESCRIPTION,
                requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody( description = LoanUtils.ROUTER_REQUEST_DESCRIPTION, required = true, content = @Content( schema = @Schema(implementation = LoanDTO.class), examples = { @ExampleObject( name = LoanUtils.TEXT_VALID_LOAN, value = LoanUtils.EXAMPLE_VALID_LOAN )})),
                responses = {
                    @ApiResponse( responseCode = LoanUtils.CREATE_CODE, description = LoanUtils.CREATE_MESSAGE, content = @Content( schema = @Schema(implementation = ApiResponse.class), examples = { @ExampleObject( name = LoanUtils.TEXT_VALIDATION_CORRECT, value = LoanUtils.EXAMPLE_VALIDATION_CORRECT)})),
                    @ApiResponse( responseCode = LoanUtils.VALIDATION_CODE, description = LoanUtils.VALIDATION_MESSAGE, content = @Content(schema = @Schema(implementation = ApiResponse.class), examples = { @ExampleObject( name = LoanUtils.TEXT_ERROR_VALIDATION, value = LoanUtils.EXAMPLE_ERROR_VALIDATION)})),
                    @ApiResponse( responseCode = LoanUtils.CONFLICT_CODE, description = LoanUtils.CONFLICT_MESSAGE, content = @Content(schema = @Schema(implementation = ApiResponse.class), examples = { @ExampleObject( name = LoanUtils.CONFLICT_MESSAGE, value = LoanUtils.EXAMPLE_VALIDATION_DUPLICATE)})),
                    @ApiResponse( responseCode = LoanUtils.INTERNAL_ERROR_CODE, description = LoanUtils.INTERNAL_ERROR_MESSAGE, content = @Content(schema = @Schema(implementation = ApiResponse.class), examples = { @ExampleObject( name = LoanUtils.INTERNAL_ERROR_MESSAGE, value = LoanUtils.EXAMPLE_INTERNAL_ERROR)}))
                }
            )
        )
    })

    public RouterFunction<ServerResponse> loanRequestRoutes(LoanHandler handler) {
        return RouterFunctions
                .route(POST(LoanUtils.ROUTER_BASE_PATH), handler::createLoan)
                .andRoute(RequestPredicates.GET(LoanUtils.ROUTER_BASE_PATH), handler::getAllLoanRequests);
    }
}
