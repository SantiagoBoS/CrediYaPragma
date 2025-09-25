package co.com.pragma.api.reports;

import co.com.pragma.model.constants.ApiPaths;
import co.com.pragma.model.constants.SwaggerConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.RouterOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@RequiredArgsConstructor
public class ReportRouter {
    private final ReportHandler handler;

    @Bean
    @RouterOperation(path = ApiPaths.REPORT_BASE, beanClass = ReportHandler.class, beanMethod = SwaggerConstants.REPORT_PATH_OPERATION_ID, method = GET,
        operation = @Operation(operationId = SwaggerConstants.REPORT_PATH_OPERATION_ID, summary = SwaggerConstants.REPORT_PATH_OPERATION_SUMMARY, description = SwaggerConstants.REPORT_PATH_OPERATION_DESCRIPTION,
            responses = {
                @ApiResponse(responseCode = SwaggerConstants.OK_CODE, description = SwaggerConstants.REPORT_PATH_REQUEST_BODY_DESCRIPTION, content = @Content(schema = @Schema(implementation = Long.class), examples = @ExampleObject(name = "Ejemplo", value = "{\"totalApprovedLoans\": 3}"))),
                @ApiResponse(responseCode = SwaggerConstants.INTERNAL_ERROR_CODE, description = SwaggerConstants.INTERNAL_ERROR_MESSAGE, content = @Content(schema = @Schema(implementation = String.class), examples = @ExampleObject(name = SwaggerConstants.INTERNAL_ERROR_MESSAGE, value = SwaggerConstants.REPORT_INTERNAL_ERROR_VALUE)))
            }
        )
    )
    public RouterFunction<ServerResponse> reportRoutes() {
        return route(GET(ApiPaths.REPORT_BASE), handler::getTotalApprovedLoans);
    }
}
