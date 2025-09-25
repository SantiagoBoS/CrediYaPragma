package co.com.pragma.model.reports.gateways;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

class ReportRepositoryTest {

    private ReportRepository reportRepository;

    @BeforeEach
    void setUp() {
        reportRepository = mock(ReportRepository.class);
    }

    @Test
    void shouldReturnTotalApprovedLoans() {
        when(reportRepository.getTotalApprovedLoans()).thenReturn(Mono.just(5L));

        StepVerifier.create(reportRepository.getTotalApprovedLoans())
                .expectNext(5L)
                .verifyComplete();

        verify(reportRepository).getTotalApprovedLoans();
    }

    @Test
    void shouldIncrementCounter() {
        when(reportRepository.incrementCounter()).thenReturn(Mono.empty());

        StepVerifier.create(reportRepository.incrementCounter())
                .verifyComplete();

        verify(reportRepository).incrementCounter();
    }

    @Test
    void shouldAddApprovedAmount() {
        when(reportRepository.addApprovedAmount(1000.0)).thenReturn(Mono.empty());

        StepVerifier.create(reportRepository.addApprovedAmount(1000.0))
                .verifyComplete();

        verify(reportRepository).addApprovedAmount(1000.0);
    }

    @Test
    void shouldReturnTotalApprovedAmount() {
        when(reportRepository.getTotalApprovedAmount()).thenReturn(Mono.just(5000.0));

        StepVerifier.create(reportRepository.getTotalApprovedAmount())
                .expectNext(5000.0)
                .verifyComplete();

        verify(reportRepository).getTotalApprovedAmount();
    }
}
