package co.com.pragma.api.reports;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.junit.jupiter.api.Assertions.*;

class ReportRouterTest {

    private ReportHandler reportHandler;
    private ReportRouter reportRouter;

    @BeforeEach
    void setUp() {
        reportHandler = new ReportHandler(null);
        reportRouter = new ReportRouter(reportHandler);
    }

    @Test
    void testRouterFunctionNotNull() {
        RouterFunction<ServerResponse> routerFunction = reportRouter.reportRoutes();
        assertNotNull(routerFunction, "El RouterFunction no debe ser null");
    }
}
