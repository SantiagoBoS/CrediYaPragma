package co.com.pragma.r2dbc.loan.loanupdate;

import co.com.pragma.model.constants.AppMessages;
import co.com.pragma.model.exceptions.BusinessException;
import co.com.pragma.model.loan.LoanRequest;
import co.com.pragma.model.loan.constants.RequestStatus;
import co.com.pragma.model.user.User;
import co.com.pragma.model.user.gateways.UserRepository;
import co.com.pragma.r2dbc.loan.loan.entity.LoanEntity;
import co.com.pragma.r2dbc.loan.loanupdate.customrepository.LoanUpdateCustomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class LoanUpdateRepositoryAdapterTest {

    private LoanUpdateReactiveRepository repository;
    private ObjectMapper mapper;
    private TransactionalOperator tsOperator;
    private UserRepository userRepository;
    private LoanUpdateCustomRepository customRepository;

    private LoanUpdateRepositoryAdapter adapter;

    private LoanEntity loanEntity;
    private UUID publicId;

    @BeforeEach
    void setUp() {
        repository = mock(LoanUpdateReactiveRepository.class);
        mapper = mock(ObjectMapper.class);
        tsOperator = mock(TransactionalOperator.class);
        userRepository = mock(UserRepository.class);
        customRepository = mock(LoanUpdateCustomRepository.class);

        adapter = new LoanUpdateRepositoryAdapter(repository, mapper, tsOperator, userRepository, customRepository);

        publicId = UUID.randomUUID();

        loanEntity = new LoanEntity();
        loanEntity.setPublicId(publicId);
        loanEntity.setClientDocument("123");
        loanEntity.setStatus(RequestStatus.PENDING_REVIEW);

        when(mapper.map(any(), eq(LoanRequest.class))).thenReturn(new LoanRequest());
        when(tsOperator.transactional(any(Mono.class))).thenAnswer(inv -> inv.getArgument(0));
    }

    @Test
    void updateStatus_success() {
        when(repository.findByPublicId(publicId)).thenReturn(Mono.just(loanEntity));
        when(userRepository.findByDocumentNumber("123")).thenReturn(Mono.just(new User()));
        when(customRepository.updateLoanStatus(any(), any(), any())).thenReturn(Mono.just(loanEntity));

        StepVerifier.create(adapter.updateStatus(publicId.toString(), "APPROVED", "advisor1"))
                .expectNextCount(1) // esperamos un LoanRequest
                .verifyComplete();

        verify(repository).findByPublicId(publicId);
        verify(userRepository).findByDocumentNumber("123");
        verify(customRepository).updateLoanStatus(publicId, RequestStatus.APPROVED, "advisor1");
    }

    @Test
    void updateStatus_whenLoanNotFound_returnsError() {
        when(repository.findByPublicId(publicId)).thenReturn(Mono.empty());

        StepVerifier.create(adapter.updateStatus(publicId.toString(), "APPROVED", "advisor1"))
                .expectError() // con que lance error es suficiente
                .verify();

        verify(repository).findByPublicId(publicId);
    }
}