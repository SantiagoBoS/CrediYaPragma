package co.com.pragma.model.loan.gateways;

import co.com.pragma.model.loan.constants.RequestStatus;
import co.com.pragma.model.loan.LoanRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class LoanUpdateRepositoryTest {

    private LoanUpdateRepository loanUpdateRepository;

    @BeforeEach
    void setUp() {
        loanUpdateRepository = mock(LoanUpdateRepository.class);
    }

    @Test
    void shouldUpdateLoanStatusSuccessfully() {
        //Para actualizar el estado del prestamo
        UUID publicId = UUID.randomUUID();
        LoanRequest loanRequest = LoanRequest.builder()
                .publicId(publicId)
                .clientDocument("CC123")
                .loanType("CAR")
                .status(RequestStatus.APPROVED) // enum
                .advisorId("advisor-1")
                .build();

        when(loanUpdateRepository.updateStatus(anyString(), anyString(), anyString())).thenReturn(Mono.just(loanRequest));
        Mono<LoanRequest> result = loanUpdateRepository.updateStatus(publicId.toString(), "APPROVED", "advisor-1");

        StepVerifier.create(result)
                .expectNextMatches(lr ->
                        lr.getStatus() == RequestStatus.APPROVED &&
                                "advisor-1".equals(lr.getAdvisorId()) &&
                                "CAR".equals(lr.getLoanType()) &&
                                "CC123".equals(lr.getClientDocument())
                )
                .verifyComplete();

        verify(loanUpdateRepository, times(1)).updateStatus(publicId.toString(), "APPROVED", "advisor-1");
    }

    @Test
    void shouldReturnErrorWhenUpdateFails() {
        //Cuando falla la actualizacion
        when(loanUpdateRepository.updateStatus(anyString(), anyString(), anyString())).thenReturn(Mono.error(new RuntimeException("DB error")));
        Mono<LoanRequest> result = loanUpdateRepository.updateStatus("123", "APPROVED", "advisor-1");

        StepVerifier.create(result)
                .expectErrorMatches(err -> err.getMessage().equals("DB error"))
                .verify();

        verify(loanUpdateRepository, times(1)).updateStatus("123", "APPROVED", "advisor-1");
    }
}
