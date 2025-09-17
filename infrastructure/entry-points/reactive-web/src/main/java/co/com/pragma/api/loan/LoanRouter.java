package co.com.pragma.api.loan;

import co.com.pragma.api.loan.dto.LoanDTO;
import co.com.pragma.model.constants.SwaggerConstants;
import co.com.pragma.model.constants.ApiPaths;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;

@Configuration
public class LoanRouter {
    @Bean
    @RouterOperations({
        @RouterOperation( path = ApiPaths.LOAN_BASE, beanClass = LoanHandler.class, beanMethod = SwaggerConstants.LOAN_ROUTER_OPERATION_POST, method = POST,
            operation = @Operation( operationId = SwaggerConstants.LOAN_ROUTER_OPERATION_POST, summary = SwaggerConstants.LOAN_ROUTER_SUMMARY, description = SwaggerConstants.LOAN_ROUTER_DESCRIPTION,
                requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody( description = SwaggerConstants.LOAN_ROUTER_REQUEST_DESCRIPTION, required = true, content = @Content( schema = @Schema(implementation = LoanDTO.class), examples = { @ExampleObject( name = SwaggerConstants.LOAN_TEXT_VALID_LOAN, value = SwaggerConstants.LOAN_EXAMPLE_VALID_LOAN )})),
                responses = {
                    @ApiResponse( responseCode = SwaggerConstants.CREATE_CODE, description = SwaggerConstants.LOAN_CREATE_MESSAGE, content = @Content( schema = @Schema(implementation = ApiResponse.class), examples = { @ExampleObject( name = SwaggerConstants.LOAN_TEXT_VALIDATION_CORRECT, value = SwaggerConstants.LOAN_EXAMPLE_VALIDATION_CORRECT)})),
                    @ApiResponse( responseCode = SwaggerConstants.VALIDATION_CODE, description = SwaggerConstants.VALIDATION_MESSAGE, content = @Content(schema = @Schema(implementation = ApiResponse.class), examples = { @ExampleObject( name = SwaggerConstants.LOAN_TEXT_ERROR_VALIDATION, value = SwaggerConstants.LOAN_EXAMPLE_ERROR_VALIDATION)})),
                    @ApiResponse( responseCode = SwaggerConstants.CONFLICT_CODE, description = SwaggerConstants.LOAN_CONFLICT_MESSAGE, content = @Content(schema = @Schema(implementation = ApiResponse.class), examples = { @ExampleObject( name = SwaggerConstants.LOAN_CONFLICT_MESSAGE, value = SwaggerConstants.LOAN_EXAMPLE_VALIDATION_DUPLICATE)})),
                    @ApiResponse( responseCode = SwaggerConstants.INTERNAL_ERROR_CODE, description = SwaggerConstants.INTERNAL_ERROR_MESSAGE, content = @Content(schema = @Schema(implementation = ApiResponse.class), examples = { @ExampleObject( name = SwaggerConstants.INTERNAL_ERROR_MESSAGE, value = SwaggerConstants.LOAN_EXAMPLE_INTERNAL_ERROR)}))
                }
            )
        )
    })

    public RouterFunction<ServerResponse> loanRequestRoutes(LoanHandler handler) {
        return RouterFunctions.route(POST(ApiPaths.LOAN_BASE), handler::createLoan);
    }

}
