package co.com.pragma.r2dbc.loantype;

import co.com.pragma.model.loan.LoanType;
import co.com.pragma.r2dbc.loantype.entity.LoanTypeEntity;
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

        loanType = new LoanType();
        loanType.setId(1L);
        loanType.setCode("HOME");
        loanType.setDescription("Home Loan");

        when(mapper.map(loanTypeEntity, LoanType.class)).thenReturn(loanType);
    }

    @Test
    void shouldFindByCodeSuccessfully() {
        //Valida que se encuentre correctamente un LoanType por su código
        when(repository.findByCode("HOME")).thenReturn(Mono.just(loanTypeEntity));
        Mono<LoanType> result = repositoryAdapter.findByCode("HOME");
        StepVerifier.create(result)
                .expectNextMatches(found -> found.getId().equals(1L) &&
                        found.getCode().equals("HOME") && found.getDescription().equals("Home Loan"))
                .verifyComplete();
        verify(repository).findByCode("HOME");
    }

    @Test
    void shouldReturnEmptyWhenLoanTypeNotFound() {
        //Valida que se retorne vacío cuando no se encuentra un LoanType por su código
        when(repository.findByCode("NOT_FOUND")).thenReturn(Mono.empty());
        Mono<LoanType> result = repositoryAdapter.findByCode("NOT_FOUND");
        StepVerifier.create(result).verifyComplete();
        verify(repository).findByCode("NOT_FOUND");
    }
}
