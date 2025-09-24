package co.com.pragma.r2dbc.loan.loantype;

import co.com.pragma.model.loan.LoanType;
import co.com.pragma.r2dbc.loan.loantype.entity.LoanTypeEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.reactivecommons.utils.ObjectMapper;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

class LoanTypeRepositoryAdapterTest {

    private LoanTypeRepositoryAdapter repositoryAdapter;

    @Mock
    private LoanTypeReactiveRepository repository;

    @Mock
    private ObjectMapper mapper;

    private LoanTypeEntity loanTypeEntity;
    private LoanType loanType;

    @BeforeEach
    void setUp() {
        repository = mock(LoanTypeReactiveRepository.class);
        mapper = mock(ObjectMapper.class);

        repositoryAdapter = new LoanTypeRepositoryAdapter(repository, mapper);

        loanTypeEntity = new LoanTypeEntity();
        loanTypeEntity.setId(1L);
        loanTypeEntity.setCode("HOME");
        loanTypeEntity.setDescription("Home Loan");
        loanTypeEntity.setInterestRate("12");
        loanTypeEntity.setAutomaticValidation(true);

        // Creamos el LoanType usando el Builder
        loanType = LoanType.builder()
                .id(1L)
                .code("HOME")
                .description("Home Loan")
                .interestRate(12.0)
                .automaticValidation(true)
                .build();

        // Mock del mapBuilder para que retorne el Builder
        when(mapper.mapBuilder(any(), eq(LoanType.LoanTypeBuilder.class)))
                .thenAnswer(invocation -> LoanType.builder());
    }

    @Test
    void shouldFindByCodeSuccessfully() {
        when(repository.findByCode("HOME")).thenReturn(Mono.just(loanTypeEntity));

        Mono<LoanType> result = repositoryAdapter.findByCode("HOME");

        StepVerifier.create(result)
                .expectNextMatches(found ->
                        found.getId().equals(1L) &&
                                found.getCode().equals("HOME") &&
                                found.getDescription().equals("Home Loan") &&
                                found.getInterestRate().equals(12.0) &&
                                found.getAutomaticValidation()
                )
                .verifyComplete();

        verify(repository).findByCode("HOME");
    }

    @Test
    void shouldReturnEmptyWhenLoanTypeNotFound() {
        when(repository.findByCode("NOT_FOUND")).thenReturn(Mono.empty());

        Mono<LoanType> result = repositoryAdapter.findByCode("NOT_FOUND");

        StepVerifier.create(result)
                .verifyComplete();

        verify(repository).findByCode("NOT_FOUND");
    }
}
