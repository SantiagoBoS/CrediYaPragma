package co.com.pragma.api.loan.router;

import co.com.pragma.api.loan.handler.LoanCapacityHandler;
import co.com.pragma.api.loan.handler.LoanHandler;
import co.com.pragma.api.loan.handler.LoanListHandler;
import co.com.pragma.api.loan.handler.LoanUpdateHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class LoanRouterTest {

    private LoanHandler loanHandler;
    private LoanListHandler loanListHandler;
    private LoanUpdateHandler loanUpdateHandler;
    private LoanCapacityHandler loanCapacityHandler;
    private WebTestClient webTestClient;

    private static final String BASE_URL_SOLICITUD = "/solicitud";
    private static final String BASE_URL_CAPACITY = "/calcular-capacidad";

    @BeforeEach
    void setUp() {
        loanHandler = Mockito.mock(LoanHandler.class);
        loanListHandler = Mockito.mock(LoanListHandler.class);
        loanUpdateHandler = Mockito.mock(LoanUpdateHandler.class);
        loanCapacityHandler = Mockito.mock(LoanCapacityHandler.class);

        LoanRouter router = new LoanRouter();
        webTestClient = WebTestClient.bindToRouterFunction(
                        router.loanRoutes(loanHandler, loanListHandler, loanUpdateHandler, loanCapacityHandler)
                )
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

    @Test
    void shouldRouteToUpdateLoanStatus() {
        when(loanUpdateHandler.updateLoanStatus(any())).thenReturn(ServerResponse.ok().bodyValue("updated"));

        webTestClient.put()
                .uri(BASE_URL_SOLICITUD + "/123e4567-e89b-12d3-a456-426614174000")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo("updated");
    }

    @Test
    void shouldRouteToCalculateCapacity() {
        when(loanCapacityHandler.calculateCapacity(any())).thenReturn(ServerResponse.ok().bodyValue("capacity-ok"));

        webTestClient.post()
                .uri(BASE_URL_CAPACITY)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo("capacity-ok");
    }
}
