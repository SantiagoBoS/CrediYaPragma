package co.com.pragma.usecase.reports;

import co.com.pragma.model.reports.ReportResponse;
import co.com.pragma.model.reports.gateways.ReportRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

class ReportUseCaseTest {

    private ReportRepository reportRepository;
    private ReportUseCase reportUseCase;

    @BeforeEach
    void setUp() {
        reportRepository = mock(ReportRepository.class);
        reportUseCase = new ReportUseCase(reportRepository);
    }

    @Test
    void shouldReturnTotalApprovedLoans() {
        when(reportRepository.getTotalApprovedLoans()).thenReturn(Mono.just(5L));

        Mono<ReportResponse> result = reportUseCase.getTotalApprovedLoans();

        StepVerifier.create(result)
                .expectNextMatches(response -> response.getTotalApprovedLoans() == 5L)
                .verifyComplete();

        verify(reportRepository).getTotalApprovedLoans();
    }

    @Test
    void shouldReturnZeroWhenRepositoryFails() {
        when(reportRepository.getTotalApprovedLoans()).thenReturn(Mono.error(new RuntimeException("DB error")));

        Mono<ReportResponse> result = reportUseCase.getTotalApprovedLoans();

        StepVerifier.create(result)
                .expectNextMatches(response -> response.getTotalApprovedLoans() == 0L)
                .verifyComplete();

        verify(reportRepository).getTotalApprovedLoans();
    }
}
