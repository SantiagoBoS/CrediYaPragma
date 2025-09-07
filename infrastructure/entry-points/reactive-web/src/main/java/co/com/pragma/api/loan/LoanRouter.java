package co.com.pragma.api.loan;

import co.com.pragma.api.loan.dto.LoanDTO;
import co.com.pragma.api.util.Utils;
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
        @RouterOperation( path = Utils.LOAN_ROUTER_BASE_PATH, beanClass = LoanHandler.class, beanMethod = Utils.LOAN_ROUTER_OPERATION_ID, method = POST,
            operation = @Operation( operationId = Utils.LOAN_ROUTER_OPERATION_ID, summary = Utils.LOAN_ROUTER_SUMMARY, description = Utils.LOAN_ROUTER_DESCRIPTION,
                requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody( description = Utils.LOAN_ROUTER_REQUEST_DESCRIPTION, required = true, content = @Content( schema = @Schema(implementation = LoanDTO.class), examples = { @ExampleObject( name = Utils.LOAN_TEXT_VALID_LOAN, value = Utils.LOAN_EXAMPLE_VALID_LOAN )})),
                responses = {
                    @ApiResponse( responseCode = Utils.CREATE_CODE, description = Utils.LOAN_CREATE_MESSAGE, content = @Content( schema = @Schema(implementation = ApiResponse.class), examples = { @ExampleObject( name = Utils.LOAN_TEXT_VALIDATION_CORRECT, value = Utils.LOAN_EXAMPLE_VALIDATION_CORRECT)})),
                    @ApiResponse( responseCode = Utils.VALIDATION_CODE, description = Utils.VALIDATION_MESSAGE, content = @Content(schema = @Schema(implementation = ApiResponse.class), examples = { @ExampleObject( name = Utils.LOAN_TEXT_ERROR_VALIDATION, value = Utils.LOAN_EXAMPLE_ERROR_VALIDATION)})),
                    @ApiResponse( responseCode = Utils.CONFLICT_CODE, description = Utils.LOAN_CONFLICT_MESSAGE, content = @Content(schema = @Schema(implementation = ApiResponse.class), examples = { @ExampleObject( name = Utils.LOAN_CONFLICT_MESSAGE, value = Utils.LOAN_EXAMPLE_VALIDATION_DUPLICATE)})),
                    @ApiResponse( responseCode = Utils.INTERNAL_ERROR_CODE, description = Utils.INTERNAL_ERROR_MESSAGE, content = @Content(schema = @Schema(implementation = ApiResponse.class), examples = { @ExampleObject( name = Utils.INTERNAL_ERROR_MESSAGE, value = Utils.LOAN_EXAMPLE_INTERNAL_ERROR)}))
                }
            )
        )
    })

    public RouterFunction<ServerResponse> loanRequestRoutes(LoanHandler handler) {
        return RouterFunctions
                .route(POST(Utils.LOAN_ROUTER_BASE_PATH), handler::createLoan)
                .andRoute(RequestPredicates.GET(Utils.LOAN_ROUTER_BASE_PATH), handler::getAllLoanRequests);
    }
}
