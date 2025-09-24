package co.com.pragma.r2dbc.loan.loanupdate.customrepository;

import co.com.pragma.model.loan.constants.RequestStatus;
import co.com.pragma.r2dbc.loan.loan.entity.LoanEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.mockito.Mockito.*;

class LoanUpdateCustomRepositoryTest {

    private LoanUpdateCustomRepository repository;

    @BeforeEach
    void setUp() {
        repository = mock(LoanUpdateCustomRepository.class);
    }

    @Test
    void shouldUpdateLoanStatusSuccessfully() {
        UUID publicId = UUID.randomUUID();
        String advisorId = "ADVISOR_1";

        LoanEntity updatedLoan = new LoanEntity();
        updatedLoan.setId(1L);
        updatedLoan.setPublicId(publicId);
        updatedLoan.setStatus(RequestStatus.APPROVED);
        updatedLoan.setAdvisorId(advisorId);

        // Mockeamos la llamada al custom repository
        when(repository.updateLoanStatus(publicId, RequestStatus.APPROVED, advisorId))
                .thenReturn(Mono.just(updatedLoan));

        StepVerifier.create(repository.updateLoanStatus(publicId, RequestStatus.APPROVED, advisorId))
                .expectNextMatches(found ->
                        found.getId().equals(1L) &&
                                found.getPublicId().equals(publicId) &&
                                found.getStatus() == RequestStatus.APPROVED &&
                                found.getAdvisorId().equals(advisorId)
                )
                .verifyComplete();

        verify(repository, times(1)).updateLoanStatus(publicId, RequestStatus.APPROVED, advisorId);
    }

    @Test
    void shouldReturnEmptyWhenLoanNotFound() {
        UUID publicId = UUID.randomUUID();
        String advisorId = "ADVISOR_2";

        when(repository.updateLoanStatus(publicId, RequestStatus.REJECTED, advisorId))
                .thenReturn(Mono.empty());

        StepVerifier.create(repository.updateLoanStatus(publicId, RequestStatus.REJECTED, advisorId))
                .verifyComplete();

        verify(repository, times(1))
                .updateLoanStatus(publicId, RequestStatus.REJECTED, advisorId);
    }
}
