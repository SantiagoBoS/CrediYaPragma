package co.com.pragma.api.loan.handler;

import co.com.pragma.model.loan.loanlist.LoanList;
import co.com.pragma.model.loan.constants.RequestStatus;
import co.com.pragma.usecase.loan.LoanListUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

class LoanListHandlerTest {

    private LoanListUseCase loanListUseCase;
    private LoanListHandler loanListHandler;
    private ServerRequest request;

    @BeforeEach
    void setUp() {
        loanListUseCase = Mockito.mock(LoanListUseCase.class);
        loanListHandler = new LoanListHandler(loanListUseCase);

        request = Mockito.mock(ServerRequest.class);
        when(request.queryParam("statuses")).thenReturn(Optional.empty());
        when(request.queryParam("page")).thenReturn(Optional.of("0"));
        when(request.queryParam("size")).thenReturn(Optional.of("10"));
    }

    @Test
    void testGetLoanListReturnsData() {
        LoanList loan = LoanList.builder()
                .amount(BigDecimal.valueOf(2000))
                .term(24)
                .email("test@mail.com")
                .fullName("Juan Perez")
                .loanType("HOME")
                .interestRate(BigDecimal.valueOf(12))
                .requestStatus(RequestStatus.APPROVED)
                .baseSalary(BigDecimal.valueOf(3000))
                .monthlyPayment(BigDecimal.valueOf(120))
                .build();

        when(loanListUseCase.list(anyList(), anyInt(), anyInt()))
                .thenReturn(Flux.just(loan));
        when(loanListUseCase.countByStatuses(anyList()))
                .thenReturn(Mono.just(1L));

        Mono<ServerResponse> responseMono = loanListHandler.getLoanList(request);

        StepVerifier.create(responseMono)
                .expectNextMatches(r -> r != null)
                .verifyComplete();
    }

    @Test
    void testGetLoanListReturnsEmpty() {
        when(loanListUseCase.list(anyList(), anyInt(), anyInt()))
                .thenReturn(Flux.empty());
        when(loanListUseCase.countByStatuses(anyList()))
                .thenReturn(Mono.just(0L));

        Mono<ServerResponse> responseMono = loanListHandler.getLoanList(request);

        StepVerifier.create(responseMono)
                .expectNextMatches(r -> r != null)
                .verifyComplete();
    }
}
