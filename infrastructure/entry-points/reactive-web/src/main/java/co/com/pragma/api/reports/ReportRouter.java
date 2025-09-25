package co.com.pragma.api.reports;

import co.com.pragma.model.constants.ApiPaths;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@RequiredArgsConstructor
public class ReportRouter {
    private final ReportHandler handler;

    @Bean
    public RouterFunction<ServerResponse> reportRoutes() {
        return route(GET(ApiPaths.REPORT_BASE), handler::getTotalApprovedLoans);
    }
}
