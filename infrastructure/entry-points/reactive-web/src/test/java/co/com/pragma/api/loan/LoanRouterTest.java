package co.com.pragma.api.loan;

import co.com.pragma.api.loan.handler.LoanHandler;
import co.com.pragma.api.loan.handler.LoanListHandler;
import co.com.pragma.api.loan.router.LoanRouter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class LoanRouterTest {

    private LoanHandler loanHandler;
    private LoanListHandler loanListHandler;
    private WebTestClient webTestClient;
    private final String BASE_URL_SOLICITUD = "/solicitud";

    @BeforeEach
    void setUp() {
        loanHandler = Mockito.mock(LoanHandler.class);
        loanListHandler = Mockito.mock(LoanListHandler.class);

        LoanRouter router = new LoanRouter();

        webTestClient = WebTestClient.bindToRouterFunction(router.loanRoutes(loanHandler, loanListHandler))
                .configureClient()
                .baseUrl("/api/v1")
                .build();
    }

    @Test
    void shouldRouteToCreateLoan() {
        when(loanHandler.createLoan(any())).thenReturn(ServerResponse.ok().bodyValue("created"));
        webTestClient.post()
                .uri(BASE_URL_SOLICITUD)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo("created");
    }

    @Test
    void shouldRouteToGetLoanList() {
        when(loanListHandler.getLoanList(any())).thenReturn(ServerResponse.ok().bodyValue("list"));

        webTestClient.get()
                .uri(BASE_URL_SOLICITUD)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo("list");
    }
}
