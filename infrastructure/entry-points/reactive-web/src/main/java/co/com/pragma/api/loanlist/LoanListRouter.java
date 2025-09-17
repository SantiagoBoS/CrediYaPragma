package co.com.pragma.api.loanlist;

import co.com.pragma.model.constants.ApiPaths;
import co.com.pragma.model.constants.SwaggerConstants;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;

@Configuration
public class LoanListRouter {
    @Bean
    @RouterOperations({@RouterOperation(path = ApiPaths.LOAN_BASE, beanClass = LoanListHandler.class, beanMethod = SwaggerConstants.LOAN_ROUTER_OPERATION_GET, method = GET)})
    public RouterFunction<ServerResponse> loanListRoutes(LoanListHandler handler) {
        return RouterFunctions.route(GET(ApiPaths.LOAN_BASE), handler::getLoanList);
    }
}
