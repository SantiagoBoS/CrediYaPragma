package co.com.pragma.api.loan.router;

import co.com.pragma.api.loan.dto.LoanListResponseDTO;
import co.com.pragma.api.loan.dto.LoanStatusUpdateDTO;
import co.com.pragma.api.loan.handler.LoanCapacityHandler;
import co.com.pragma.api.loan.handler.LoanHandler;
import co.com.pragma.api.loan.dto.LoanDTO;
import co.com.pragma.api.loan.handler.LoanListHandler;
import co.com.pragma.api.loan.handler.LoanUpdateHandler;
import co.com.pragma.model.constants.SwaggerConstants;
import co.com.pragma.model.constants.ApiPaths;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.bind.annotation.RequestMethod.*;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;

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
                    @ApiResponse( responseCode = SwaggerConstants.INTERNAL_ERROR_CODE, description = SwaggerConstants.INTERNAL_ERROR_MESSAGE, content = @Content(schema = @Schema(implementation = ApiResponse.class), examples = { @ExampleObject( name = SwaggerConstants.INTERNAL_ERROR_MESSAGE, value = SwaggerConstants.LOAN_EXAMPLE_INTERNAL_ERROR)}))
                }
            )
        ),
        @RouterOperation(path = ApiPaths.LOAN_BASE, beanClass = LoanListHandler.class, beanMethod = SwaggerConstants.LOAN_ROUTER_OPERATION_GET, method = GET,
            operation = @Operation(operationId = SwaggerConstants.LOAN_ROUTER_OPERATION_GET, summary = SwaggerConstants.LOAN_GET_SUMMARY, description = SwaggerConstants.LOAN_GET_DESCRIPTION, parameters = {@Parameter(name = "page", description = SwaggerConstants.LOAN_GET_PARAM_PAGE, in = ParameterIn.QUERY, schema = @Schema(type = "integer", defaultValue = "1", minimum = "1")), @Parameter(name = "size", description = SwaggerConstants.LOAN_GET_PARAM_SIZE, in = ParameterIn.QUERY, schema = @Schema(type = "integer", defaultValue = "10", minimum = "1", maximum = "100"))},
                security = @SecurityRequirement(name = SwaggerConstants.TOKEN_DESCRIPTION),
                responses = {
                    @ApiResponse(responseCode = SwaggerConstants.OK_CODE, description = SwaggerConstants.LOAN_TEXT_GET_LOAN_LIST, content = @Content(schema = @Schema(implementation = LoanListResponseDTO.class), examples = @ExampleObject(name = SwaggerConstants.TEXT_VALIDATION_CORRECT, value = SwaggerConstants.LOAN_EXAMPLE_GET_RESPONSE)))
                }
            )
        ),
        @RouterOperation(path = ApiPaths.LOAN_BASE + "/{publicId}", beanClass = LoanUpdateHandler.class, beanMethod = SwaggerConstants.LOAN_PUT_OPERATION_ID, method = PUT,
                operation = @Operation(operationId = SwaggerConstants.LOAN_PUT_OPERATION_ID, summary = SwaggerConstants.LOAN_PUT_SUMMARY, description = SwaggerConstants.LOAN_PUT_DESCRIPTION, parameters = {@Parameter(name = "publicId", description = SwaggerConstants.LOAN_PUT_PARAM_PUBLIC_ID, in = ParameterIn.PATH, required = true, schema = @Schema(type = "string", format = "uuid"))},
                        security = @SecurityRequirement(name = SwaggerConstants.TOKEN_DESCRIPTION),
                        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = SwaggerConstants.LOAN_PUT_RB_DESCRIPTION, required = true, content = @Content(schema = @Schema(implementation = LoanStatusUpdateDTO.class))),
                        responses = {
                                @ApiResponse(responseCode = SwaggerConstants.OK_CODE, description = SwaggerConstants.LOAN_PUT_SUCCESS, content = @Content(schema = @Schema(implementation = ApiResponse.class))),
                                @ApiResponse(responseCode = SwaggerConstants.VALIDATION_CODE, description = SwaggerConstants.VALIDATION_MESSAGE, content = @Content(schema = @Schema(implementation = ApiResponse.class))),
                                @ApiResponse(responseCode = SwaggerConstants.NOT_FOUND, description = SwaggerConstants.LOAN_NOT_FOUND, content = @Content(schema = @Schema(implementation = ApiResponse.class)))
                        }
                )
        )
    })
    public RouterFunction<ServerResponse> loanRoutes(
            LoanHandler loanHandler,
            LoanListHandler loanListHandler,
            LoanUpdateHandler loanUpdateHandler,
            LoanCapacityHandler loanCapacityHandler
    ){
        return RouterFunctions
                .route(POST(ApiPaths.LOAN_BASE), loanHandler::createLoan)
                .andRoute(GET(ApiPaths.LOAN_BASE), loanListHandler::getLoanList)
                .andRoute(PUT(ApiPaths.LOAN_BASE + "/{publicId}"), loanUpdateHandler::updateLoanStatus)
                .andRoute(POST(ApiPaths.LOAN_BASE + "/{publicId}/status"), loanCapacityHandler::calculateCapacity);
    }

}
