package co.com.pragma.api.reports;

import co.com.pragma.model.reports.ReportResponse;
import co.com.pragma.usecase.reports.ReportUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class ReportHandlerTest {

    private ReportUseCase reportUseCase;
    private ReportHandler reportHandler;
    private ServerRequest serverRequest;

    @BeforeEach
    void setUp() {
        reportUseCase = mock(ReportUseCase.class);
        reportHandler = new ReportHandler(reportUseCase);
        serverRequest = mock(ServerRequest.class);
    }

    @Test
    void testGetTotalApprovedLoansReturnsValue() {
        ReportResponse expected = new ReportResponse(5L);
        when(reportUseCase.getTotalApprovedLoans()).thenReturn(Mono.just(expected));
        Mono<ServerResponse> responseMono = reportHandler.getTotalApprovedLoans(serverRequest);
        ServerResponse response = responseMono.block();
        assertNotNull(response);
    }

    @Test
    void testGetTotalApprovedLoansHandlesError() {
        when(reportUseCase.getTotalApprovedLoans()).thenReturn(Mono.error(new RuntimeException("fail")));
        Mono<ServerResponse> responseMono = reportHandler.getTotalApprovedLoans(serverRequest);
        ServerResponse response = responseMono.block();
        assertNotNull(response);
    }
}
