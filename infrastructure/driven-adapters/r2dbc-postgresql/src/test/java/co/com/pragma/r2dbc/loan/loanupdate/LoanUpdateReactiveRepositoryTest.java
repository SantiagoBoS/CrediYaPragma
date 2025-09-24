package co.com.pragma.r2dbc.loan.loanupdate;

import co.com.pragma.model.loan.constants.RequestStatus;
import co.com.pragma.r2dbc.loan.loan.entity.LoanEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.mockito.Mockito.*;

class LoanUpdateReactiveRepositoryTest {

    private LoanUpdateReactiveRepository repository;

    @BeforeEach
    void setUp() {
        repository = mock(LoanUpdateReactiveRepository.class);
    }

    @Test
    void shouldFindByPublicIdSuccessfully() {
        UUID publicId = UUID.randomUUID();

        LoanEntity loan = new LoanEntity();
        loan.setId(1L);
        loan.setPublicId(publicId);
        loan.setAmount(1000.0);
        loan.setStatus(RequestStatus.valueOf("APPROVED"));

        when(repository.findByPublicId(publicId)).thenReturn(Mono.just(loan));

        StepVerifier.create(repository.findByPublicId(publicId))
                .expectNextMatches(found ->
                        found.getId().equals(1L) &&
                                found.getPublicId().equals(publicId) &&
                                found.getAmount().equals(1000.0) &&
                                found.getStatus().name().equals("APPROVED")
                )
                .verifyComplete();

        verify(repository, times(1)).findByPublicId(publicId);
    }

    @Test
    void shouldReturnEmptyWhenPublicIdNotFound() {
        UUID publicId = UUID.randomUUID();
        when(repository.findByPublicId(publicId)).thenReturn(Mono.empty());
        StepVerifier.create(repository.findByPublicId(publicId)).verifyComplete();
        verify(repository, times(1)).findByPublicId(publicId);
    }
}
