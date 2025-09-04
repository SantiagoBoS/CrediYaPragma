package co.com.pragma.api;

import co.com.pragma.api.dto.LoanDTO;
import co.com.pragma.api.exception.LoanUtils;
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
        @RouterOperation( path = "/api/v1/solicitud", beanClass = LoanHandler.class, beanMethod = "createLoanRequest", method = POST,
            operation = @Operation( operationId = "createLoanRequest", summary = "Registrar una nueva solicitud de préstamo", description = "Permite registrar una solicitud de préstamo proporcionando documento del cliente, monto, plazo y tipo de préstamo",
                requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody( description = "Datos de la nueva solicitud de préstamo", required = true, content = @Content( schema = @Schema(implementation = LoanDTO.class), examples = { @ExampleObject( name = LoanUtils.TEXT_VALID_LOAN, value = LoanUtils.EXAMPLE_VALID_LOAN )})),
                responses = {
                    @ApiResponse( responseCode = LoanUtils.CREATE_CODE, description = LoanUtils.CREATE_MESSAGE, content = @Content( schema = @Schema(implementation = ApiResponse.class), examples = { @ExampleObject( name = LoanUtils.TEXT_VALIDATION_CORRECT, value = LoanUtils.EXAMPLE_VALIDATION_CORRECT)})),
                    @ApiResponse( responseCode = LoanUtils.VALIDATION_CODE, description = LoanUtils.VALIDATION_MESSAGE, content = @Content(schema = @Schema(implementation = ApiResponse.class), examples = { @ExampleObject( name = LoanUtils.TEXT_ERROR_VALIDATION, value = LoanUtils.EXAMPLE_ERROR_VALIDATION)})),
                    @ApiResponse( responseCode = LoanUtils.CONFLICT_CODE, description = LoanUtils.CONFLICT_MESSAGE, content = @Content(schema = @Schema(implementation = ApiResponse.class), examples = { @ExampleObject( name = LoanUtils.CONFLICT_MESSAGE, value = LoanUtils.EXAMPLE_VALIDATION_DUPLICATE)})),
                    @ApiResponse(responseCode = LoanUtils.INTERNAL_ERROR_CODE, description = LoanUtils.INTERNAL_ERROR_MESSAGE, content = @Content(schema = @Schema(implementation = ApiResponse.class), examples = { @ExampleObject( name = LoanUtils.INTERNAL_ERROR_MESSAGE, value = LoanUtils.EXAMPLE_INTERNAL_ERROR)}))
                }
            )
        )
    })

    public RouterFunction<ServerResponse> loanRequestRoutes(LoanHandler handler) {
        return RouterFunctions
                .route(POST("/api/v1/solicitud"), handler::createLoanRequest)
                .andRoute(RequestPredicates.GET("/api/v1/solicitud"), handler::getAllLoanRequests);
    }
}
