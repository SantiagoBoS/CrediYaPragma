package co.com.pragma.api.loan;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class LoanRouterTest {

    private LoanHandler handler;
    private WebTestClient webTestClient;
    private final String BASE_URL_SOLICITUD = "/solicitud";

    @BeforeEach
    void setUp() {
        handler = Mockito.mock(LoanHandler.class);
        LoanRouter router = new LoanRouter();

        webTestClient = WebTestClient.bindToRouterFunction(router.loanRequestRoutes(handler))
                .configureClient()
                .baseUrl("/api/v1")
                .build();
    }

    @Test
    void shouldRouteToCreateLoan() {
        when(handler.createLoan(any())).thenReturn(ServerResponse.ok().bodyValue("created"));
        webTestClient.post()
                .uri(BASE_URL_SOLICITUD)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo("created");
    }
}
