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
    void shouldReturnTotalApprovedLoansAndAmount() {
        when(reportRepository.getTotalApprovedLoans()).thenReturn(Mono.just(5L));
        when(reportRepository.getTotalApprovedAmount()).thenReturn(Mono.just(1000.0));

        Mono<ReportResponse> result = reportUseCase.getTotalApprovedLoans();

        StepVerifier.create(result)
                .expectNextMatches(response ->
                        response.getTotalApprovedLoans() == 5L &&
                                response.getTotalApprovedAmount() == 1000.0
                )
                .verifyComplete();

        verify(reportRepository).getTotalApprovedLoans();
        verify(reportRepository).getTotalApprovedAmount();
    }

    @Test
    void shouldReturnZeroWhenRepositoryFails() {
        when(reportRepository.getTotalApprovedLoans()).thenReturn(Mono.error(new RuntimeException("DB error")));
        when(reportRepository.getTotalApprovedAmount()).thenReturn(Mono.error(new RuntimeException("DB error")));

        Mono<ReportResponse> result = reportUseCase.getTotalApprovedLoans();

        StepVerifier.create(result)
                .expectNextMatches(response ->
                        response.getTotalApprovedLoans() == 0L &&
                                response.getTotalApprovedAmount() == 0.0
                )
                .verifyComplete();

        verify(reportRepository).getTotalApprovedLoans();
        verify(reportRepository).getTotalApprovedAmount();
    }

    @Test
    void shouldReturnZeroForAmountWhenAmountFails() {
        when(reportRepository.getTotalApprovedLoans()).thenReturn(Mono.just(5L));
        when(reportRepository.getTotalApprovedAmount()).thenReturn(Mono.error(new RuntimeException("Amount DB error")));

        Mono<ReportResponse> result = reportUseCase.getTotalApprovedLoans();

        StepVerifier.create(result)
                .expectNextMatches(response ->
                        response.getTotalApprovedLoans() == 0L &&
                                response.getTotalApprovedAmount() == 0.0
                )
                .verifyComplete();

        verify(reportRepository).getTotalApprovedLoans();
        verify(reportRepository).getTotalApprovedAmount();
    }
}
