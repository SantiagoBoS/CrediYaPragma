package co.com.pragma.r2dbc.loan.loantype.customerrepository;

import co.com.pragma.model.constants.AppMessages;
import co.com.pragma.model.exceptions.BusinessException;
import co.com.pragma.model.loan.constants.RequestStatus;
import co.com.pragma.r2dbc.loan.loan.entity.LoanEntity;
import co.com.pragma.r2dbc.loan.loanupdate.customrepository.LoanUpdateCustomRepositoryImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Query;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanUpdateCustomRepositoryImplTest {

    private R2dbcEntityTemplate entityTemplate;
    private LoanUpdateCustomRepositoryImpl repository;

    private UUID publicId;
    private LoanEntity loanEntity;

    @BeforeEach
    void setUp() {
        entityTemplate = mock(R2dbcEntityTemplate.class, RETURNS_DEEP_STUBS);
        repository = new LoanUpdateCustomRepositoryImpl(entityTemplate);

        publicId = UUID.randomUUID();
        loanEntity = new LoanEntity();
        loanEntity.setPublicId(publicId);
        loanEntity.setStatus(RequestStatus.PENDING_REVIEW);
    }

    @Test
    void updateLoanStatus_success() {
        //Para actualizar el estado
        when(entityTemplate.select(LoanEntity.class)
                .matching(any(Query.class))
                .one()).thenReturn(Mono.just(loanEntity));

        when(entityTemplate.update(any(LoanEntity.class)))
                .thenReturn(Mono.just(loanEntity));

        StepVerifier.create(repository.updateLoanStatus(publicId, RequestStatus.APPROVED, "advisor1"))
                .expectNext(loanEntity)
                .verifyComplete();

        verify(entityTemplate).update(any(LoanEntity.class));
    }
}