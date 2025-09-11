package co.com.pragma.r2dbc;

import co.com.pragma.model.loan.LoanType;
import co.com.pragma.r2dbc.loantype.LoanTypeReactiveRepository;
import co.com.pragma.r2dbc.loantype.LoanTypeRepositoryAdapter;
import co.com.pragma.r2dbc.loantype.entity.LoanTypeEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.reactivecommons.utils.ObjectMapper;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

class LoanTypeRepositoryAdapterTest {

    @Mock
    private LoanTypeReactiveRepository repository;

    @Mock
    private ObjectMapper mapper;

    @InjectMocks
    private LoanTypeRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        adapter = new LoanTypeRepositoryAdapter(repository, mapper);
    }

    @Test
    void testFindByCode_Success() {
        String code = "PERSONAL";
        LoanTypeEntity entity = new LoanTypeEntity(1L, "PERSONAL", "Préstamo personal");
        LoanType loanType = new LoanType(1L, "PERSONAL", "Préstamo personal");

        when(repository.findByCode(code)).thenReturn(Mono.just(entity));
        when(mapper.map(entity, LoanType.class)).thenReturn(loanType);

        StepVerifier.create(adapter.findByCode(code))
                .expectNextMatches(result ->
                        result.getId().equals(1L) &&
                                result.getCode().equals("PERSONAL") &&
                                result.getDescription().equals("Préstamo personal"))
                .verifyComplete();
    }

    @Test
    void testFindByCode_NotFound() {
        String code = "UNKNOWN";
        when(repository.findByCode(code)).thenReturn(Mono.empty());

        StepVerifier.create(adapter.findByCode(code))
                .verifyComplete();
    }
}