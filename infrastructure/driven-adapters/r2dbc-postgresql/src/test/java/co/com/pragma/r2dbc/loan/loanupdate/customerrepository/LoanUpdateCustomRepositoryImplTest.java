package co.com.pragma.r2dbc.loan.loanupdate.customerrepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import co.com.pragma.model.constants.AppMessages;
import co.com.pragma.model.exceptions.BusinessException;
import co.com.pragma.model.loan.constants.RequestStatus;
import co.com.pragma.r2dbc.loan.loan.entity.LoanEntity;
import co.com.pragma.r2dbc.loan.loanupdate.customrepository.LoanUpdateCustomRepositoryImpl;
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
        when(entityTemplate.select(LoanEntity.class)
                .matching(any(Query.class))
                .one()).thenReturn(Mono.just(loanEntity));

        when(entityTemplate.update(any(LoanEntity.class)))
                .thenReturn(Mono.just(loanEntity));

        StepVerifier.create(repository.updateLoanStatus(publicId, RequestStatus.APPROVED, "advisor1"))
                .assertNext(updated -> {
                    assertEquals(RequestStatus.APPROVED, updated.getStatus());
                    assertEquals("advisor1", updated.getAdvisorId());
                    assertTrue(updated.getUpdatedAt() != null);
                })
                .verifyComplete();

        verify(entityTemplate).update(any(LoanEntity.class));
    }

    @Test
    void updateLoanStatus_notFound() {
        when(entityTemplate.select(LoanEntity.class)
                .matching(any(Query.class))
                .one()).thenReturn(Mono.empty());

        StepVerifier.create(repository.updateLoanStatus(publicId, RequestStatus.APPROVED, "advisor1"))
                .expectErrorSatisfies(error -> {
                    assertTrue(error instanceof BusinessException);
                    assertEquals(AppMessages.LOAN_NOT_FOUND.getMessage(), error.getMessage());
                })
                .verify();
    }


    @Test
    void updateLoanStatus_unexpectedError() {
        when(entityTemplate.select(LoanEntity.class)
                .matching(any(Query.class))
                .one()).thenReturn(Mono.just(loanEntity));

        when(entityTemplate.update(any(LoanEntity.class)))
                .thenReturn(Mono.error(new RuntimeException("DB error")));

        StepVerifier.create(repository.updateLoanStatus(publicId, RequestStatus.APPROVED, "advisor1"))
                .expectErrorSatisfies(error -> {
                    assertTrue(error instanceof BusinessException);
                    assertEquals(AppMessages.LOAN_UPDATE_ERROR.getMessage(), error.getMessage());
                })
                .verify();
    }
}