package co.com.pragma.api;

import co.com.pragma.api.dto.LoanRequestDTO;
import co.com.pragma.api.exception.LoanRequestUtils;
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
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class LoanRequestRouter {
    @Bean
    @RouterOperations({
        @RouterOperation( path = "/api/v1/solicitud", beanClass = LoanRequestHandler.class, beanMethod = "createLoanRequest", method = POST,
            operation = @Operation( operationId = "createLoanRequest", summary = "Registrar una nueva solicitud de préstamo", description = "Permite registrar una solicitud de préstamo proporcionando documento del cliente, monto, plazo y tipo de préstamo",
                requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody( description = "Datos de la nueva solicitud de préstamo", required = true, content = @Content( schema = @Schema(implementation = LoanRequestDTO.class), examples = { @ExampleObject( name = LoanRequestUtils.TEXT_VALID_LOAN, value = LoanRequestUtils.EXAMPLE_VALID_LOAN )})),
                responses = {
                    @ApiResponse( responseCode = LoanRequestUtils.CREATE_CODE, description = LoanRequestUtils.CREATE_MESSAGE, content = @Content( schema = @Schema(implementation = ApiResponse.class), examples = { @ExampleObject( name = LoanRequestUtils.TEXT_VALIDATION_CORRECT, value = LoanRequestUtils.EXAMPLE_VALIDATION_CORRECT)})),
                    @ApiResponse( responseCode = LoanRequestUtils.VALIDATION_CODE, description = LoanRequestUtils.VALIDATION_MESSAGE, content = @Content(schema = @Schema(implementation = ApiResponse.class), examples = { @ExampleObject( name = LoanRequestUtils.TEXT_ERROR_VALIDATION, value = LoanRequestUtils.EXAMPLE_ERROR_VALIDATION)})),
                    @ApiResponse( responseCode = LoanRequestUtils.CONFLICT_CODE, description = LoanRequestUtils.CONFLICT_MESSAGE, content = @Content(schema = @Schema(implementation = ApiResponse.class), examples = { @ExampleObject( name = LoanRequestUtils.CONFLICT_MESSAGE, value = LoanRequestUtils.EXAMPLE_VALIDATION_DUPLICATE)})),
                    @ApiResponse(responseCode = LoanRequestUtils.INTERNAL_ERROR_CODE, description = LoanRequestUtils.INTERNAL_ERROR_MESSAGE, content = @Content(schema = @Schema(implementation = ApiResponse.class), examples = { @ExampleObject( name = LoanRequestUtils.INTERNAL_ERROR_MESSAGE, value = LoanRequestUtils.EXAMPLE_INTERNAL_ERROR)}))
                }
            )
        )
    })

    public RouterFunction<ServerResponse> loanRequestRoutes(LoanRequestHandler handler) {
        return RouterFunctions
                .route(POST("/api/v1/solicitud"), handler::createLoanRequest)
                .andRoute(RequestPredicates.GET("/api/v1/solicitud"), handler::getAllLoanRequests);
    }
}
